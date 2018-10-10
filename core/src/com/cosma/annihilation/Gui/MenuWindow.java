package com.cosma.annihilation.Gui;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.cosma.annihilation.Utils.Serialization.Serializer;

public class MenuWindow  extends Window {
    private Engine engine;
    private World world;
    private Serializer serializer;
    private TextButton saveButton;
    private TextButton loadButton;
    private TextButton pauseButton;
    private Skin skin;

    public MenuWindow(String title, Skin skin) {
        super(title, skin);
        this.skin = skin;

        addButtons();
        addAction();
    }
    private void addButtons(){
        saveButton = new TextButton("save",skin);
        this.add(saveButton).size(150,50);
        this.row();
        loadButton = new TextButton("load",skin);
        this.add(loadButton).size(150,50);
    }

    private void addAction(){

        saveButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//                player.getComponent(PlayerComponent.class).inventoryItem = InventoryWindow.getInventory(inventoryWindow.inventorySlotsTable);
//                player.getComponent(PlayerComponent.class).equippedItem = InventoryWindow.getInventory(inventoryWindow.equipmentSlotsTable);
                System.out.println("save");
                serializer.save();
                return true;
            }
        });

        loadButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//                inventoryWindow.loadInventory();
                System.out.println("load");
                serializer.load();
                return true;
            }
        });
    }

    public void setEngineAndWorld(Engine engine,World world){
        this.engine = engine;
        this.world = world;
    }
}



