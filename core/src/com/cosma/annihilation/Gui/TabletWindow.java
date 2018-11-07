package com.cosma.annihilation.Gui;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.cosma.annihilation.Utils.LoaderOLD;
import com.cosma.annihilation.Utils.Serialization.Serializer;

public class TabletWindow extends Window {

    private Skin skin;
    private InventoryWindow inventoryWindow;
    private MenuWindow menuWindow;
    private Table windowTable;
    private TextButton exitButton;
    private TextButton inventoryButton;
    private TextButton menuButton;
    private Engine engine;
    private Serializer serializer;

    TabletWindow(String title, Skin skin,World world, Engine engine) {
        super(title, skin);
        TextureRegionDrawable background = new TextureRegionDrawable(new TextureRegion((Texture) LoaderOLD.getResource("tablet")));
        this.setBackground(background);
        this.setFillParent(true);
        this.skin = skin;
        this.setVisible(false);
        this.debugAll();
        this.engine = engine;

        serializer = new Serializer(engine,world);

        inventoryWindow = new InventoryWindow("",skin,engine);
        inventoryWindow.setVisible(false);

        menuWindow = new MenuWindow("",skin,this);
        menuWindow.setVisible(false);

        createButtons(this);
        addButtonsAction();

        Table screenTable = new Table();
        screenTable.setDebug(true);
        this.add(screenTable);

        windowTable = new Table();

        VerticalGroup verticalGroup = new VerticalGroup();
        verticalGroup.addActor(inventoryButton);
        verticalGroup.addActor(menuButton);
        verticalGroup.addActor(exitButton);

        screenTable.add(windowTable).size(800,700);
        screenTable.add(verticalGroup);
    }

    private void createButtons(final TabletWindow menu){
        exitButton = new TextButton("Exit",skin);

        menuButton = new TextButton("Menu", skin);

        inventoryButton = new TextButton("Inventory",skin);

    }

    void saveGame(){
        inventoryWindow.saveInventory(engine);
        serializer.save();
    }


    void autoSave(){
        //TODO
    }

    void loadGame(){
        serializer.load();
        inventoryWindow.loadInventory(engine);
    }



    private void addButtonsAction(){
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
                windowTable.clearChildren();
                menu.setVisible(false);
                return true;
            }
        });

        inventoryButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                clearAndAddWindow(inventoryWindow);
                inventoryWindow.loadInventory(engine);
                return true;
            }
        });
    }

    private void clearAndAddWindow(Window window){
        if(windowTable.hasChildren()){
            windowTable.clearChildren();
            inventoryWindow.saveInventory(engine);
            windowTable.add(window);
            window.setVisible(true);
        }else {
            windowTable.add(window);
            window.setVisible(true);
        }
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
