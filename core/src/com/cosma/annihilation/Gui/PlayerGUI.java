package com.cosma.annihilation.Gui;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.cosma.annihilation.Components.BodyComponent;
import com.cosma.annihilation.Components.PlayerComponent;
import com.cosma.annihilation.Items.InventoryItem;
import com.cosma.annihilation.Items.ItemFactory;
import com.cosma.annihilation.Items.WeaponItem;
import com.cosma.annihilation.Utils.StateManager;

public class PlayerGUI implements Screen {
    private Stage stage;
    private Viewport viewport;
    private Camera camera;
    private Skin skin;
    private Label fpslabel;
    private String fpsnumber;
    private ImageButton actionButtonR1;
    private ImageButton actionButtonR2;
    private ImageButton actionButtonUp;
    private ImageButton actionButtonDown;
    private ImageButton actionButtonLeft;
    private ImageButton actionButtonRight;
    private TextButton debugButton;
    private TextButton saveButton;
    private TextButton loadButton;
    private TextButton menuButton;
    private InventoryWindow inventoryWindow;
    private CharacterWindow characterWindow;
    private CraftingWindow craftingWindow;
    private MenuWindow menuWindow;
    private ProgressBar healthBar;
    private Engine engine;
    private Entity player;


    public  PlayerGUI(Engine engine){

        this.engine = engine;
        camera = new OrthographicCamera();
        camera.update();
        viewport = new ScreenViewport(camera);
        stage = new Stage(viewport);
        viewport.apply(true);
        skin = new Skin(Gdx.files.internal("UI/skin/pixthulhu-ui.json"));
        //Create HUD
        createActionButton();
        createHUD();
        createInventoryWindow();
        //Get player entity
        player = engine.getEntitiesFor(Family.all(PlayerComponent.class).get()).first();
    }

    private void createInventoryWindow(){
        inventoryWindow = new InventoryWindow("Inventory", skin);
        inventoryWindow.setDebug(true);
//        inventoryWindow.setFillParent(true);
        float x = stage.getWidth() * 0.9f;
        float y = stage.getHeight() * 0.9f;
        inventoryWindow.setSize(x,y);
        inventoryWindow.setPosition(stage.getWidth()/2-(x/2),stage.getHeight()/2-(y/2));
        inventoryWindow.setZIndex(10);
        inventoryWindow.setMovable(false);
        inventoryWindow.setVisible(false);
        stage.addActor(inventoryWindow);

    }

    //TODO
    private void getArmourParameter(){}

    private void createActionButton(){
        fpslabel = new Label(fpsnumber,skin);
        actionButtonR1 = new ImageButton(skin,"default");
        actionButtonR1.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(inventoryWindow.getActiveWeapon() == null){
                }else {
                    System.out.println(inventoryWindow.getActiveWeapon().isAutomatic());
                    System.out.println(inventoryWindow.getActiveWeapon().getAccuracy());
                    System.out.println(inventoryWindow.getActiveWeapon().getMaxAmmoInMagazine());
                    System.out.println("dmg" + inventoryWindow.getActiveWeapon().getDamage());

                }

                return true;
            }
        });
        actionButtonR2 = new ImageButton(skin,"default");

        actionButtonUp = new ImageButton(skin,"default");
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

        //Table actor
        saveButton = new TextButton("save",skin);
        saveButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                inventoryWindow.saveInventory();
                return true;
            }
        });
        loadButton = new TextButton("load",skin);
        loadButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                inventoryWindow.loadInventory();
                return true;
            }
        });

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
        table.add(saveButton).padTop(10).padLeft(10).left().width(150).height(50).expandX();
        table.row();
        table.add(loadButton).padTop(10).padLeft(10).left().width(150).height(50).expandX();
        table.row();
        table.add(fpslabel).padTop(10).padLeft(10).left().width(150).height(50);
        table.row();
        //-------------------Action button table------------------------------------------------
        table.add(bTable).left().bottom().expandY().padBottom(15).padLeft(15).size(300);
        bTable.add(actionButtonUp).width(150).height(150).center().colspan(3);
        bTable.add(actionButtonR1).expandX().right().padBottom(15).padLeft(15).size(150).padRight(25);
        bTable.row();
        bTable.add(actionButtonLeft).width(150).height(150).left().pad(10);
        bTable.add(actionButtonDown).width(150).height(150).left().pad(10);
        bTable.add(actionButtonRight).width(150).height(150).left().pad(10);
        bTable.add(actionButtonR2).expandX().right().padBottom(15).padLeft(15).size(150).padRight(50);
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
    float fpss = Gdx.graphics.getFramesPerSecond();
    fpsnumber = Float.toString(fpss);
    fpslabel.setText(fpsnumber);
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
        stage.dispose();

    }
}
