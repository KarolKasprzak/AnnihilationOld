package com.cosma.annihilation.Editor;

import box2dLight.RayHandler;
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
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPane;

public class LightsPanel extends VisWindow implements InputProcessor {

    private MapEditor mapEditor;
    private VisTextButton createLightButton;
    private float x1, y1, x2, y2;
    /**0 = point , 1 = cone, 2 = directional  **/
    private int lightType = 0;
    private VisCheckBox setPointLight, setConeLight, setDirectionalLight;
    private boolean canCreateBox = false;
    private ShapeRenderer shapeRenderer;
    private boolean canDraw = false;
    private OrthographicCamera camera;
    private ObjectsListWindow objectsListWindow;
    private boolean isObjectListWindowOpen = false;
    private RayHandler rayHandler;

    public void setObjectListWindowOpen(boolean objectListWindowOpen) {
        isObjectListWindowOpen = objectListWindowOpen;
    }

    public LightsPanel(final MapEditor mapEditor, RayHandler rayHandler) {
        super("Lights:");
        this.rayHandler = rayHandler;
        this.mapEditor = mapEditor;
        this.shapeRenderer = mapEditor.getShapeRenderer();
        this.camera = mapEditor.getCamera();

        TableUtils.setSpacingDefaults(this);
        columnDefaults(0).left();

        createLightButton = new VisTextButton("add light");
        setPointLight = new VisCheckBox("Point", true);
        setPointLight.setFocusBorderEnabled(false);
        setDirectionalLight = new VisCheckBox("Cone");
        setDirectionalLight.setFocusBorderEnabled(false);
        setConeLight = new VisCheckBox("Directional");
        VisTextButton openObjectListWindowButton = new VisTextButton("Obj. list");

        add(setDirectionalLight);
        add(setPointLight);
        add(setConeLight);
        row();
        add(createLightButton).top();
        add(openObjectListWindowButton).top();
        add().expand().fill();

        setPointLight.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                lightType = 0;
                setDirectionalLight.setChecked(false);
                setConeLight.setChecked(false);
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        setDirectionalLight.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                lightType = 2;
                setPointLight.setChecked(false);
                setConeLight.setChecked(false);
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        setConeLight.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                lightType = 1;
                setDirectionalLight.setChecked(false);
                setPointLight.setChecked(false);
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        createLightButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                canCreateBox = true;
            }
        });
        openObjectListWindowButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!getStage().getActors().contains(objectsListWindow, true) && !mapEditor.getMap().getLayers().getByType(ObjectMapLayer.class).isEmpty()) {
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
        createLightButton.setDisabled(status);
        setConeLight.setDisabled(status);
        setDirectionalLight.setDisabled(status);
        setPointLight.setDisabled(status);
    }

    private void createPointLight(float x, float y, float w, float h) {
//        if (mapEditor.isObjectLayerSelected()) {
//            Utilities.createBox2dObject(mapEditor.getWorld(), x, y, w, h,bodyType, mapEditor.layersPanel.getSelectedLayer(ObjectMapLayer.class).createBoxObject(x, y, w, h,bodyType));
//            canCreateBox = false;
//            if (isObjectListWindowOpen) {
//                objectsListWindow.rebuildView();
//            }
//        }
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
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//        shapeRenderer.setColor(Color.BLACK);
//        shapeRenderer.rect(x1, y1, x2 - x1, y2 - y1);
//        shapeRenderer.end();
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        shapeRenderer.setColor(0, 0, 0, 0.2f);
//        shapeRenderer.rect(x1, y1, x2 - x1, y2 - y1);
//        shapeRenderer.end();
    }


}
