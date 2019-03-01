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

import java.awt.*;

public class ObjectPanel extends VisWindow implements InputProcessor {

    private MapEditor mapEditor;
    private TabbedPane tabbedPane;
    private VisTextButton createRectangleButton;
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
    private VisLabel  boxSize;

    public void setObjectListWindowOpen(boolean objectListWindowOpen) {
        isObjectListWindowOpen = objectListWindowOpen;
    }

    public ObjectPanel(final MapEditor mapEditor) {
        super("Object");
        this.mapEditor = mapEditor;
        this.shapeRenderer = mapEditor.getShapeRenderer();
        this.camera = mapEditor.getCamera();

        TableUtils.setSpacingDefaults(this);
        columnDefaults(0).left();

        createRectangleButton = new VisTextButton("Box");
        addCircleButton = new VisTextButton("Circle");
        VisTextButton openObjectListWindowButton = new VisTextButton("Obj. list");

        boxSize = new VisLabel();

        add(createRectangleButton).top();
        add(addCircleButton).top();
        add(openObjectListWindowButton).top();
        add().expand().fill();

        createRectangleButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                canCreateBox = true;
                getStage().addActor(boxSize);
                createCircle = false;
            }
        });

        openObjectListWindowButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(!getStage().getActors().contains(objectListWindow,true) && !mapEditor.getMap().getLayers().getByType(ObjectMapLayer.class).isEmpty()){
                    objectListWindow = new ObjectListWindow(mapEditor,camera);
                    getStage().addActor(objectListWindow);
                    mapEditor.getInputMultiplexer().addProcessor(0,objectListWindow);
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
            boxSize.setPosition(Gdx.input.getX(),Gdx.input.getY());
            Vector3 worldCoordinates = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            Vector3 vec = camera.unproject(worldCoordinates);
            x2 = vec.x;
            y2 = vec.y;
            drawBox();
        }
    }

    public void setPanelButtonsDisable(Boolean status){
        createRectangleButton.setDisabled(status);
        addCircleButton.setDisabled(status);
    }

    private void createBoxObject(float x, float y, float w, float h){
        if(mapEditor.isObjectLayerSelected()){
            mapEditor.layersPanel.getSelectedLayer(ObjectMapLayer.class).createBoxObject(x,y,w,h);
            canCreateBox = false;
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

            float x = x1;
            float y = y1;
            float width = x2-x1;
            float height = y2-y1;

            if(width<0){
                x = x- -width;
                width = -width;
            }
            if(height<0 && width<0){
                x = x- -width;
                width = -width;
                y = y- -height;
                height = -height;
            }
            if(height<0){
                y = y- -height;
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
        boxSize.setText("height: " + (y2 - y1) + " width: " + (x2 - x1) );
    }


}
