package com.cosma.annihilation.Gui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.cosma.annihilation.Gui.Inventory.InventorySlot;
import com.cosma.annihilation.Gui.Inventory.InventorySlotTarget;

class ContainerWindow extends Window {
    DragAndDrop dragAndDrop;
    Table containerSlotsTable;
    private Skin skin;
    private int itemSlotNumber;
    private TextButton takeAllButton;
    private TextButton closeButton;
    private ContainerWindow containerWindow;
    private  ActorGestureListener listener;
    ContainerWindow(String title, Skin skin, int itemSlotNumber) {
        super(title, skin);
        this.skin = skin;
        this.itemSlotNumber = itemSlotNumber;
        containerWindow = this;

        dragAndDrop = new DragAndDrop();
        takeAllButton = new TextButton("Take all", skin);
        closeButton = new TextButton("Close", skin);

        listener = new ActorGestureListener(){
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                if(count >= 2){
                    InventorySlot actor = ( InventorySlot) event.getListenerActor();

                }
            }
        };


        createContainerTable();

    }

    private void createContainerTable() {
        containerSlotsTable = new Table();
        containerSlotsTable.setDebug(false);
        containerSlotsTable.setFillParent(false);

        for (int i = 0; i < itemSlotNumber; i++) {
            InventorySlot inventorySlot = new InventorySlot();
            inventorySlot.addListener(listener);
            dragAndDrop.addTarget(new InventorySlotTarget(inventorySlot));
            containerSlotsTable.add(inventorySlot).size(50, 50).pad(2);
            if (i == 6 || i == 12 || i == 18) containerSlotsTable.row();
        }

        this.add(containerSlotsTable).center().fillX().colspan(2);
        this.row();
        this.add(takeAllButton).bottom().center().size(150, 50);
        this.add(closeButton).bottom().center().size(150, 50);
        closeButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                containerWindow.setVisible(false);
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }



}




