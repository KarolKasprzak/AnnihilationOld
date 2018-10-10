package com.cosma.annihilation.Gui;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.cosma.annihilation.Utils.AssetsLoader;

public class TabletWindow extends Window {

    private Skin skin;
    private InventoryWindow inventoryWindow;
    private MenuWindow menuWindow;
    private Table screenTable;
    private Table windowTable;
    private TextButton exitButton;
    private TextButton inventoryButton;
    private TextButton menuButton;
    private Engine engine;
    private World world;

    public TabletWindow(String title, Skin skin) {
        super(title, skin);
        TextureRegionDrawable background = new TextureRegionDrawable(new TextureRegion((Texture) AssetsLoader.getResource("tablet")));
        this.setBackground(background);
        this.setFillParent(true);
        this.skin = skin;
        this.setVisible(false);
        this.debugAll();

        inventoryWindow = new InventoryWindow("",skin);
        inventoryWindow.setVisible(false);
        inventoryWindow.setZIndex(10);

        menuWindow = new MenuWindow("",skin);
        menuWindow.setVisible(false);

        createButons(this);
        addAction();

        screenTable = new Table();
        screenTable.setDebug(true);
        this.add(screenTable);

        windowTable = new Table();

        VerticalGroup verticalGroup = new VerticalGroup();
        verticalGroup.addActor(inventoryButton);
        verticalGroup.addActor(menuButton);
        verticalGroup.addActor(exitButton);


        screenTable.add(windowTable).size(800,700);
        screenTable.add(verticalGroup);
//        screenTable.row();
//        screenTable.add(inventoryButton).size(100,50);
    }

    private void createButons(final TabletWindow menu){
        exitButton = new TextButton("Exit",skin);

        menuButton = new TextButton("Menu", skin);

        inventoryButton = new TextButton("Inventory",skin);

    }
    private void addAction(){
        final TabletWindow menu = this;

        menuButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
             clearAndAddWindow(menuWindow);
                return true;
            }
        });

        exitButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                menu.setVisible(false);
                return true;
            }
        });

        inventoryButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            clearAndAddWindow(inventoryWindow);
                return true;
            }
        });
    }

    private void clearAndAddWindow(Window window){
        if(windowTable.hasChildren()){
            windowTable.clearChildren();
            windowTable.add(window);
            window.setVisible(true);
        }else {
            windowTable.add(window);
            window.setVisible(true);
        }
    }


    public void setEngineAndWorld(Engine engine,World world){
        this.engine = engine;
        this.world = world;
    }

    public void openinventoryWindow() {
        //        inventoryWindow = new InventoryWindow("Inventory", skin);
//        inventoryWindow = new MenuWindow("Inventory", skin);
        //Load inventory
//        if(engine.getEntities().size() == 0){
//            InventoryWindow.fillInventory(inventoryWindow.inventorySlotsTable,player.getComponent(PlayerComponent.class).inventoryItem,inventoryWindow.dragAndDrop);
//            InventoryWindow.fillInventory(inventoryWindow.equipmentSlotsTable,player.getComponent(PlayerComponent.class).equippedItem,inventoryWindow.dragAndDrop);
//        }
//        inventoryWindow.setDebug(true);
////        inventoryWindow.setFillParent(true);
//        float x = stage.getWidth() * 0.9f;
//        float y = stage.getHeight() * 0.9f;
//        inventoryWindow.setSize(x,y);
////        inventoryWindow.setPosition(stage.getWidth()/2-(x/2),stage.getHeight()/2-(y/2));
//        inventoryWindow.setFillParent(true);
//        inventoryWindow.setZIndex(10);
//        inventoryWindow.setMovable(false);
//        inventoryWindow.setVisible(false);
//        stage.addActor(inventoryWindow);

    }
}
