package com.cosma.annihilation.Editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.cosma.annihilation.Editor.CosmaMap.ObjectMapLayer;
import com.cosma.annihilation.Screens.MapEditor;
import com.cosma.annihilation.Utils.Utilities;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.widget.*;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPane;

public class ObjectPanel extends VisWindow implements InputProcessor {

    private MapEditor mapEditor;
    private TabbedPane tabbedPane;
    private VisTextButton createRectangleButton, createJointButton,openObjectListWindowButton;
    private float x1, y1, x2, y2;
    private BodyDef.BodyType bodyType = BodyDef.BodyType.StaticBody;
    private VisCheckBox setStaticBox, setKinematicBox, setDynamicBox;
    private boolean canCreateBox = false;
    private ShapeRenderer shapeRenderer;
    private boolean canDraw = false;
    private OrthographicCamera camera;
    private ObjectsListWindow objectsListWindow;
    private boolean isObjectListWindowOpen = false;

    public void setObjectListWindowOpen(boolean objectListWindowOpen) {
        isObjectListWindowOpen = objectListWindowOpen;
    }

    public ObjectPanel(final MapEditor mapEditor) {
        super("Objects:");
        this.mapEditor = mapEditor;
        this.shapeRenderer = mapEditor.getShapeRenderer();
        this.camera = mapEditor.getCamera();

        TableUtils.setSpacingDefaults(this);
        columnDefaults(0).left();

        createRectangleButton = new VisTextButton("add box");
        createJointButton = new VisTextButton("add joint");
        setStaticBox = new VisCheckBox("Static", true);
        setStaticBox.setFocusBorderEnabled(false);
        setDynamicBox = new VisCheckBox("Dynamic");
        setDynamicBox.setFocusBorderEnabled(false);
        setKinematicBox = new VisCheckBox("Kinematic");
        openObjectListWindowButton = new VisTextButton("Obj. list");

        add(setDynamicBox);
        add(setStaticBox);
        add(setKinematicBox);
        row();
        add(createRectangleButton).top();
        add(createJointButton).top();
        add(openObjectListWindowButton).top();
        add().expand().fill();

        setStaticBox.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                bodyType = BodyDef.BodyType.StaticBody;
                setDynamicBox.setChecked(false);
                setKinematicBox.setChecked(false);
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        setDynamicBox.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                bodyType = BodyDef.BodyType.DynamicBody;
                setStaticBox.setChecked(false);
                setKinematicBox.setChecked(false);
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        setKinematicBox.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                bodyType = BodyDef.BodyType.KinematicBody;
                setDynamicBox.setChecked(false);
                setStaticBox.setChecked(false);
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        createRectangleButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                canCreateBox = true;
            }
        });
        openObjectListWindowButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!getStage().getActors().contains(objectsListWindow, true) && !mapEditor.getMap().getLayers().getByType(ObjectMapLayer.class).isEmpty() && mapEditor.isObjectLayerSelected()) {
                    objectsListWindow = new ObjectsListWindow(mapEditor, camera);
                    getStage().addActor(objectsListWindow);
                    mapEditor.getInputMultiplexer().addProcessor(0, objectsListWindow);
                    setObjectListWindowOpen(true);
                }
            }
        });

        setPanelButtonsDisable(true);
        pack();
        setSize(getWidth(), getHeight());
        setPosition(1900, 200);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (canDraw) {
            Vector3 worldCoordinates = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            Vector3 vec = camera.unproject(worldCoordinates);
            x2 = vec.x;
            y2 = vec.y;
            drawBox();
        }
    }

    public void setPanelButtonsDisable(Boolean status) {
        createRectangleButton.setDisabled(status);
        setKinematicBox.setDisabled(status);
        setDynamicBox.setDisabled(status);
        setStaticBox.setDisabled(status);
        openObjectListWindowButton.setDisabled(status);
        createJointButton.setDisabled(status);
    }

    private void createBoxObject(float x, float y, float w, float h) {
        if (mapEditor.isObjectLayerSelected()) {
            Utilities.createBox2dObject(mapEditor.getWorld(), x, y, w, h,bodyType, mapEditor.layersPanel.getSelectedLayer(ObjectMapLayer.class).createBoxObject(x, y, w, h,bodyType));
            canCreateBox = false;
            if (isObjectListWindowOpen) {
                objectsListWindow.rebuildView();
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT && canCreateBox() && mapEditor.isObjectLayerSelected()) {
            Vector3 worldCoordinates = new Vector3(screenX, screenY, 0);
            Vector3 vec = camera.unproject(worldCoordinates);
            canDraw = true;
            x1 = vec.x;
            y1 = vec.y;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT && canCreateBox()) {
            canDraw = false;

            float x = x1;
            float y = y1;
            float width = x2 - x1;
            float height = y2 - y1;

            if (width < 0) {
                x = x - -width;
                width = -width;
            }
            if (height < 0 && width < 0) {
                x = x - -width;
                width = -width;
                y = y - -height;
                height = -height;
            }
            if (height < 0) {
                y = y - -height;
                height = -height;
            }

            createBoxObject(x, y, width, height);
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    public boolean canCreateBox() {
        return canCreateBox;
    }

    public void setCanCreateBox(boolean canCreateBox) {
        this.canCreateBox = canCreateBox;
    }

    private void drawBox() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(x1, y1, x2 - x1, y2 - y1);
        shapeRenderer.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 0.2f);
        shapeRenderer.rect(x1, y1, x2 - x1, y2 - y1);
        shapeRenderer.end();
    }


}
