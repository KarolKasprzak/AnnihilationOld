package com.cosma.annihilation.Gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.cosma.annihilation.Items.InventoryItem;
import com.cosma.annihilation.Items.ItemFactory;
import com.cosma.annihilation.Items.WeaponItem;
import com.cosma.annihilation.Utils.AssetsLoader;

public class InventoryWindow extends Window {
    private DragAndDrop dragAndDrop;
    private Table inventorySlotsTable;
    private Table equipmentSlotsTable;
    private Skin skin;

    public InventoryWindow(String title, Skin skin) {
        super(title, skin);
        dragAndDrop = new DragAndDrop();
        this.skin = skin;
        equipmentSlotsTable = new Table();
        equipmentSlotsTable.setDebug(false);


//      InventoryItem inventoryItem = new InventoryItem(1,1,"ad");
        InventoryItem inventoryItem = new InventoryItem("bullet",InventoryItem.ItemID.BOX);

        ItemFactory itemFactory = new ItemFactory();


        InventorySlot headInventorySlot = new InventorySlot();
        InventorySlot bodyInventorySlot = new InventorySlot();
        InventorySlot legsInventorySlot = new InventorySlot();
        InventorySlot leftInventorySlot = new InventorySlot();
        InventorySlot rightInventorySlot = new InventorySlot();


        headInventorySlot.add(itemFactory.getItem(InventoryItem.ItemID.MP44));

        dragAndDrop.addSource(new InventorySlotSource(headInventorySlot,dragAndDrop));
        dragAndDrop.addTarget(new InventorySlotTarget(headInventorySlot));

//        dragAndDrop.addSource(new InventorySlotSource(legsInventorySlot,dragAndDrop));
        dragAndDrop.addTarget(new InventorySlotTarget(legsInventorySlot));

        equipmentSlotsTable.add();
        equipmentSlotsTable.add(headInventorySlot).size(50,50).pad(2);
        equipmentSlotsTable.add();
        equipmentSlotsTable.row();
        equipmentSlotsTable.add(leftInventorySlot).size(50,50).pad(2);
        equipmentSlotsTable.add(bodyInventorySlot).size(50,50).pad(2);
        equipmentSlotsTable.add(rightInventorySlot).size(50,50).pad(2);
        equipmentSlotsTable.row();
        equipmentSlotsTable.add();
        equipmentSlotsTable.add(legsInventorySlot).size(50,50).pad(2);
        equipmentSlotsTable.add();
        this.add(equipmentSlotsTable);
        equipmentSlotsTable.center();
        equipmentSlotsTable.pad(20);
        this.row();
        inventorySlotsTable = new Table();
        inventorySlotsTable.bottom();
        inventorySlotsTable.setDebug(false);
        inventorySlotsTable.setFillParent(false);

        for(int i = 1; i < 25; i++){
            InventorySlot inventorySlot = new InventorySlot();
            dragAndDrop.addTarget(new InventorySlotTarget(inventorySlot));
            inventorySlotsTable.add(inventorySlot).size(50,50).pad(2);
            if(i == 6 || i == 12 || i == 18  )inventorySlotsTable.row();

        }
        this.add(inventorySlotsTable);

    }

}
