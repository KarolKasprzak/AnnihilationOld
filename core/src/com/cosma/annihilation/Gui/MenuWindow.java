package com.cosma.annihilation.Gui;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.cosma.annihilation.Utils.Serialization.Serializer;
import com.cosma.annihilation.Utils.Utilities;

public class MenuWindow extends Window {

    private Skin skin;
    private InventoryWindow inventoryWindow;
    private OptionsMenuWindow optionsMenuWindow;
    private Table windowTable;
    private TextButton exitButton;
    private TextButton inventoryButton;
    private TextButton menuButton;
    private TextButton characterButton;

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
        this.add(windowTable).center().size(this.getWidth()*0.95f ,this.getHeight()*0.91f);

        inventoryWindow = new InventoryWindow("",skin,engine);
        inventoryWindow.setVisible(false);

        optionsMenuWindow = new OptionsMenuWindow("",skin,this);
        optionsMenuWindow.setVisible(false);

        createButtons(this);
        buttonsLogic();

        windowTable.add(inventoryButton).size(Utilities.getButtonWidth(1.8f), Utilities.getButtonHeight(1.8f)).top();
        windowTable.add(characterButton).size(Utilities.getButtonWidth(1.8f), Utilities.getButtonHeight(1.8f));
        windowTable.add(menuButton).size(Utilities.getButtonWidth(1.8f), Utilities.getButtonHeight(1.8f));
        windowTable.add(exitButton).size(Utilities.getButtonWidth(1.8f), Utilities.getButtonHeight(1.8f));

        serializer = new Serializer(engine, world);

        inventoryWindow = new InventoryWindow("", skin, engine);
        inventoryWindow.setVisible(false);

    }

    private void createButtons(final MenuWindow menu){
        exitButton = new TextButton("Exit",skin);
        Utilities.setButtonColor(exitButton);

        menuButton = new TextButton("Menu", skin);
        Utilities.setButtonColor(menuButton);

        inventoryButton = new TextButton("Inventory",skin);
        Utilities.setButtonColor(inventoryButton);

        characterButton = new TextButton("Character",skin);
        Utilities.setButtonColor(characterButton);
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



    private void buttonsLogic(){
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
