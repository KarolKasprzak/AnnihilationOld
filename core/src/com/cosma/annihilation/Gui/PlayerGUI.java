package com.cosma.annihilation.Gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.cosma.annihilation.Utils.StateManager;

public class PlayerGUI implements Screen {
    private Stage stage;
    private Viewport viewport;
    private Camera camera;
    private Skin skin;
    private ImageButton actionButtonUp;
    private ImageButton actionButtonDown;
    private ImageButton actionButtonLeft;
    private ImageButton actionButtonRight;
    private TextButton debugButton;
    private TextButton debugButtonGui;
    private TextButton menuButton;
    private TextButton stateCheckButton;
    private InventoryWindow inventoryWindow;
    public  PlayerGUI(){

        camera = new OrthographicCamera();
        camera.update();
        viewport = new ScreenViewport(camera);
        stage = new Stage(viewport);
        viewport.apply(true);
        skin = new Skin(Gdx.files.internal("UI/skin/pixthulhu-ui.json"));
        createHUD();
        createActionButton();
        createInventoryWindow();
    }

    private void createInventoryWindow(){
        inventoryWindow = new InventoryWindow("Inventory", skin);
        float x = stage.getWidth() * 0.6f;
        float y = stage.getHeight() * 0.8f;
        inventoryWindow.setSize(x,y);
        inventoryWindow.setPosition(stage.getWidth()/2-(x/2),stage.getHeight()/2-(y/2));
        inventoryWindow.setZIndex(10);
        inventoryWindow.setMovable(false);
        inventoryWindow.setVisible(false);
        stage.addActor(inventoryWindow);
    }
    private void createActionButton(){
        actionButtonUp = new ImageButton(skin,"default");
        actionButtonUp.setZIndex(5);
        actionButtonUp.addListener(new InputListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                StateManager.goUp = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                StateManager.goUp = false;
            }

        });
        actionButtonDown = new ImageButton(skin,"default");
        actionButtonDown.setZIndex(5);
        actionButtonDown.addListener(new InputListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                StateManager.goDown = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                StateManager.goDown = false;
            }

        });
        actionButtonLeft = new ImageButton(skin,"default");
        actionButtonLeft.addListener(new InputListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                StateManager.goLeft = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                StateManager.goLeft = false;
            }

        });
        actionButtonRight = new ImageButton(skin,"default");
        actionButtonRight.addListener(new InputListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                StateManager.goRight = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                StateManager.goRight = false;
            }

        });

    }
    private void createHUD(){
        Table table = new Table();
        table.center();
        table.setFillParent(true);
        Table bTable = new Table();
        bTable.bottom().left();
        bTable.setFillParent(true);
        stage.addActor(table);
        stage.addActor(bTable);

        //Table
        menuButton = new TextButton("Character menu", skin);
        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (menuButton.isChecked()) {
                    inventoryWindow.setVisible(true);
                } else
                    inventoryWindow.setVisible(false);
            }
        });
        debugButtonGui = new TextButton("Debug mode GUI", skin);
        debugButtonGui.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(debugButtonGui.isChecked()){
                    debugButtonGui.setText("Debug mode - enabled");
                    StateManager.debugModeGui = true;
                }
                else {
                    debugButtonGui.setText("Debug mode GUI");
                    StateManager.debugModeGui = false;
                }
            }

        });
        debugButton = new TextButton("Debug mode ", skin);

        debugButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(debugButton.isChecked()){
                    debugButton.setText("Debug mode - enabled");
                    StateManager.debugMode = true;
                }
                else {
                    debugButton.setText("Debug mode");
                    StateManager.debugMode = false;
                }
            }

        });


        table.add(debugButton).padTop(10).padLeft(10).left().width(150).height(50).expandX();
        table.add(menuButton).padTop(10).padRight(10).right().width(150).height(50);
        table.row();
        table.add(debugButtonGui).padTop(10).padLeft(10).left().width(150).height(50);
        table.row();
        table.add(stateCheckButton).padTop(10).padLeft(10).left().width(150).height(50);
        table.row();
        table.add(bTable).left().bottom().expandY().padBottom(15).padLeft(15).size(300);
        bTable.add(actionButtonUp).width(150).height(150).center().colspan(3);
        bTable.row();
        bTable.add(actionButtonLeft).width(150).height(150).left().pad(10);
        bTable.add(actionButtonDown).width(150).height(150).left().pad(10);
        bTable.add(actionButtonRight).width(150).height(150).left().pad(10);
        table.row();



    }

    public Stage getStage() {
        return stage;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
    stage.act(delta);
    stage.draw();
    }

    @Override
    public void resize(int width, int height) {
    stage.getViewport().update(width,height,true);
    updateInventoryWindowSize();

    }
    private void updateInventoryWindowSize(){
        float x = stage.getWidth() * 0.6f;
        float y = stage.getHeight() * 0.8f;
        inventoryWindow.setSize(x,y);
        inventoryWindow.setPosition(stage.getWidth()/2-(x/2),stage.getHeight()/2-(y/2));
    }
    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
