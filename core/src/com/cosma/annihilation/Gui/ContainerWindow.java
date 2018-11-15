package com.cosma.annihilation.Gui;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.cosma.annihilation.Components.ContainerComponent;
import com.cosma.annihilation.Components.PlayerComponent;
import com.cosma.annihilation.Components.PlayerDateComponent;
import com.cosma.annihilation.Gui.Inventory.InventoryItemLocation;
import com.cosma.annihilation.Gui.Inventory.InventorySlot;
import com.cosma.annihilation.Gui.Inventory.InventorySlotTarget;
import com.badlogic.gdx.utils.Array;
import com.cosma.annihilation.Utils.Utilities;


class ContainerWindow extends Window {
    DragAndDrop dragAndDrop;
    Table containerSlotsTable;
    private int itemSlotNumber;
    private TextButton takeAllButton;
    private TextButton closeButton;
    private ContainerWindow containerWindow;
    private ActorGestureListener listener;
    private Engine engine;
    private Skin skin;
    ContainerWindow(String title, Skin skin, int itemSlotNumber, final Engine engine) {
        super(title, skin);
        this.skin = skin;
        this.itemSlotNumber = itemSlotNumber;
        this.engine = engine;

        containerWindow = this;

        dragAndDrop = new DragAndDrop();
        takeAllButton = new TextButton("Take all", skin);
        Utilities.setButtonColor(takeAllButton);
        closeButton = new TextButton("Close", skin);
        Utilities.setButtonColor(closeButton);
        listener = new ActorGestureListener(){
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                if(count >= 2){
                    InventorySlot slot = ( InventorySlot) event.getListenerActor();
                    moveItemToPlayerEquipment(slot);
                }
            }
        };

        createContainerTable();
    }

    private void moveItemToPlayerEquipment(InventorySlot inventorySlot){
        if(engine.getEntitiesFor(Family.all(PlayerComponent.class).get()).first().getComponent(PlayerDateComponent.class).inventoryItem.size <= 24){

            InventoryItemLocation inventoryItemLocation = new InventoryItemLocation();
                inventoryItemLocation.setTableIndex(findEmptySlotInEquipment());
                inventoryItemLocation.setItemsAmount(inventorySlot.getItemsNumber());
                inventoryItemLocation.setItemID(inventorySlot.getInventoryItem().getItemID().toString());

            engine.getEntitiesFor(Family.all(PlayerComponent.class).get()).first().getComponent(PlayerDateComponent.class).inventoryItem.add(inventoryItemLocation);

            System.out.println(engine.getEntitiesFor(Family.all(PlayerComponent.class).get()).first().getComponent(PlayerDateComponent.class).inventoryItem.get(0).getTableIndex());

            for(InventoryItemLocation item: engine.getEntitiesFor(Family.all(PlayerComponent.class).get()).first().getComponent(PlayerComponent.class)
                    .processedEntity.getComponent(ContainerComponent.class).itemLocations){
                if(item.getItemID().equals(inventorySlot.getInventoryItem().getItemID().toString())){
                    if(item.getItemsAmount() == inventorySlot.getItemsNumber()){
                        engine.getEntitiesFor(Family.all(PlayerComponent.class).get()).first().getComponent(PlayerComponent.class)
                                .processedEntity.getComponent(ContainerComponent.class).itemLocations.removeValue(item,true);
                    }
                }
            }
            inventorySlot.clearAllItems();

        }
    }



    private int findEmptySlotInEquipment() {
        int n = 0;
        Array<InventoryItemLocation> inventoryItem = engine.getEntitiesFor(Family.all(PlayerComponent.class).get()).first().getComponent(PlayerDateComponent.class).inventoryItem;

        Array<Integer> numbers = new Array<Integer>();

        for (InventoryItemLocation item : inventoryItem) {
            numbers.add(item.getTableIndex());
        }

        for(int i = 0; i <= 24; i++){
            if(!numbers.contains(i,true)){
                n = i;
                break;
            }
        }
        return  n;
    }

    private void createContainerTable() {
        containerSlotsTable = new Table();
        containerSlotsTable.setDebug(false);
        containerSlotsTable.setFillParent(false);

        for (int i = 0; i < itemSlotNumber; i++) {
            InventorySlot inventorySlot = new InventorySlot();
            inventorySlot.addListener(listener);
            dragAndDrop.addTarget(new InventorySlotTarget(inventorySlot));
            containerSlotsTable.add(inventorySlot).size(Utilities.setWindowHeight(0.1f), Utilities.setWindowHeight(0.1f)).pad(Utilities.setWindowHeight(0.005f));
            if (i == 6 || i == 12 || i == 18) containerSlotsTable.row();
        }

        this.add(containerSlotsTable).center().fillX().colspan(2).pad(80);
        this.row();
        this.add(takeAllButton).bottom().center().size(Utilities.setButtonWidth(2), Utilities.setButtonHeight(2));
        this.add(closeButton).bottom().center().size(Utilities.setButtonWidth(2), Utilities.setButtonHeight(2));
        closeButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                containerWindow.setVisible(false);
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }
}




