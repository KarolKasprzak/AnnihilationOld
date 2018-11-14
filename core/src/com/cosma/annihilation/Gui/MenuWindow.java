package com.cosma.annihilation.Gui;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.cosma.annihilation.Utils.LoaderOLD;
import com.cosma.annihilation.Utils.Serialization.Serializer;

public class MenuWindow extends Window {

    private Skin skin;
    private InventoryWindow inventoryWindow;
    private OptionsMenuWindow optionsMenuWindow;
    private Table windowTable;
    private TextButton exitButton;
    private TextButton inventoryButton;
    private TextButton menuButton;
    private Engine engine;
    private Serializer serializer;

    MenuWindow(String title, Skin skin, World world, Engine engine,float x , float y ) {
        super(title, skin);
        this.setFillParent(false);
        this.skin = skin;
        this.setVisible(false);
        this.engine = engine;
        this.getTitleLabel().setColor(0,82,0,255);
        this.getTitleLabel().setFontScale(0.8f);
        this.debugTable();
        this.setSize(x,y);
        windowTable = new Table();
        windowTable.setFillParent(false);
        System.out.println(this.getX());
        System.out.println(this.getWidth());
        System.out.println(this.getOriginX());

        BackgroundColor backgroundColor = new BackgroundColor("white_color_texture.png");
        backgroundColor.setColor(2, 179, 228, 255);
        windowTable.setBackground(backgroundColor);
        windowTable.debugTable();
        this.add(windowTable).center().size(this.getWidth()*0.95f,this.getHeight()*0.95f);

        serializer = new Serializer(engine,world);

//        inventoryWindow = new InventoryWindow("",skin,engine);
//        inventoryWindow.setVisible(false);
//
//        createButtons(this);
//        addButtonsAction();

//        this.add(windowTable).size(Gdx.graphics.getWidth()*0.67f,Gdx.graphics.getHeight()*0.59f)
//                .padLeft(Gdx.graphics.getWidth()*0.065f).padRight(Gdx.graphics.getWidth()*0.265f).padBottom(Gdx.graphics.getHeight()*0.065f);

    }

    private void createButtons(final MenuWindow menu){
        exitButton = new TextButton("Exit",skin);
        exitButton.getLabel().setColor(0,82,0,255);

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
        final MenuWindow menu = this;

        menuButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
             clearAndAddWindow(optionsMenuWindow);
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
