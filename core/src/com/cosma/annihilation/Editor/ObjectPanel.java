package com.cosma.annihilation.Editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.cosma.annihilation.Editor.CosmaMap.ObjectMapLayer;
import com.cosma.annihilation.Screens.MapEditor;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.widget.*;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPane;

public class ObjectPanel extends VisWindow implements InputProcessor {

    private MapEditor mapEditor;
    private TabbedPane tabbedPane;
    private VisTextButton addBoxButton;
    private VisTextButton addCircleButton;
    private float x1;
    private float y1;
    private float x2;
    private float y2;
    private boolean canCreateBox = false;
    private boolean createCircle = false;
    private ShapeRenderer shapeRenderer;
    private boolean canDraw = false;
    private OrthographicCamera camera;
    private ObjectListWindow objectListWindow;
    private boolean isObjectListWindowOpen = false;

    public void setObjectListWindowOpen(boolean objectListWndowOpen) {
        isObjectListWindowOpen = objectListWndowOpen;
    }

    public ObjectPanel(final MapEditor mapEditor) {
        super("Object");
        this.mapEditor = mapEditor;
        this.shapeRenderer = mapEditor.getShapeRenderer();
        this.camera = mapEditor.getCamera();

        TableUtils.setSpacingDefaults(this);
        columnDefaults(0).left();

        addBoxButton = new VisTextButton("Box");
        addCircleButton = new VisTextButton("Circle");
        VisTextButton openObjectListWindowButton = new VisTextButton("Obj. list");

        add(addBoxButton).top();
        add(addCircleButton).top();
        add(openObjectListWindowButton).top();
        add().expand().fill();

        addBoxButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                canCreateBox = true;
                createCircle = false;
            }
        });

        openObjectListWindowButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(!getStage().getActors().contains(objectListWindow,true)){
                    objectListWindow = new ObjectListWindow(mapEditor);
                    getStage().addActor(objectListWindow);
                    setObjectListWindowOpen(true);
                }
            }
        });

        addCircleButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                canCreateBox = false;
                createCircle = true;
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

    private void createObject(float x, float y, float w, float h){
        if(canCreateBox){
            createBoxObject(x,y,w,h);
            canCreateBox = false;
            addBoxButton.focusGained();
        }
    }

    public void setPanelButtonsDisable(Boolean status){
        addBoxButton.setDisabled(status);
        addCircleButton.setDisabled(status);
    }

    private void createBoxObject(float x, float y, float w, float h){
        if(mapEditor.isObjectLayerSelected()){
            mapEditor.layersPanel.getSelectedLayer(ObjectMapLayer.class).createBoxObject(x,y,w,h);
            if(isObjectListWindowOpen){
                objectListWindow.rebuildView();
            }
        }
    }

    public void createCircleObject(float x, float y, float w, float h){
//       //TODO
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
            createObject(x1, y1, x2 - x1, y2 - y1);
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return true;
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
