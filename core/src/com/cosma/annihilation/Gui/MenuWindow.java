package com.cosma.annihilation.Gui;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
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
    private PlayerGUI playerGUI;
    private Engine engine;
    private Serializer serializer;

    MenuWindow(String title, Skin skin, World world, Engine engine,float x , float y, PlayerGUI playerGUI) {
        super(title, skin);

        this.playerGUI = playerGUI;
        this.skin = skin;
        this.setVisible(false);
        this.engine = engine;
        this.getTitleLabel().setColor(0,82,0,255);
        this.setSize(x,y);




        serializer = new Serializer(engine, world);

        windowTable = new Table();
        inventoryWindow = new InventoryWindow("",skin,engine);
        inventoryWindow.setVisible(false);
        optionsMenuWindow = new OptionsMenuWindow("",skin,this);
        optionsMenuWindow.setVisible(false);
        inventoryWindow.setVisible(false);

        createButtons(this);

        this.add(inventoryButton).size(Utilities.setButtonWidth(1.8f), Utilities.setButtonHeight(1.8f));
        this.add(characterButton).size(Utilities.setButtonWidth(1.8f), Utilities.setButtonHeight(1.8f));
        this.add(menuButton).size(Utilities.setButtonWidth(1.8f), Utilities.setButtonHeight(1.8f));
        this.add(exitButton).size(Utilities.setButtonWidth(1.8f), Utilities.setButtonHeight(1.8f));
        this.row();
        this.add(windowTable).center().size(this.getWidth()*0.95f ,this.getHeight()*0.8f).colspan(4);

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

        buttonsController();
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



    private void buttonsController(){
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
                if(windowTable.getChildren().contains(inventoryWindow,true)){
                    inventoryWindow.saveInventory(engine);
                }
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

    private void clearAndAddWindow(Window window) {
        if (windowTable.getChildren().contains(inventoryWindow, true)) {
            inventoryWindow.saveInventory(engine);
        }
        windowTable.clearChildren();
        windowTable.add(window).size(this.getWidth() * 0.95f, this.getHeight() * 0.84f);
        window.setVisible(true);
    }


    private void setWindowSize(Window window){
        window.setSize(this.getWidth()*0.9f ,this.getHeight()*0.7f);
        window.setFillParent(true);
    }
}
