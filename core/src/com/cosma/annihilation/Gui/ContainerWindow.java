package com.cosma.annihilation.Gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.cosma.annihilation.Items.InventoryItem;
import com.cosma.annihilation.Items.ItemFactory;
import com.cosma.annihilation.Items.WeaponItem;
import com.cosma.annihilation.Utils.AssetsLoader;

public class ContainerWindow extends Window {
    public DragAndDrop dragAndDrop;
    public Table  containerSlotsTable;
    private Skin skin;
    private int itemSlotNumber;
    private TextButton takeAllButton;

    public ContainerWindow(String title, Skin skin,int itemSlotNumber) {
        super(title, skin);
        this.skin = skin;
        this.itemSlotNumber = itemSlotNumber;
        dragAndDrop = new DragAndDrop();
        takeAllButton = new TextButton("Take all",skin);
        createContainerTable();

}

    public void createContainerTable() {
        containerSlotsTable = new Table();
        containerSlotsTable.center();
        containerSlotsTable.setDebug(false);
        containerSlotsTable.setFillParent(false);

        for(int i = 0; i < itemSlotNumber; i++){
            InventorySlot inventorySlot = new InventorySlot();
            dragAndDrop.addTarget(new InventorySlotTarget(inventorySlot));
            containerSlotsTable.add(inventorySlot).size(50,50).pad(2);
            if(i == 6 || i == 12 || i == 18  ) containerSlotsTable.row();
        }

        this.add( containerSlotsTable);
        this.row();
        this.add(takeAllButton).bottom().center().size(200,50);

    }






    }

//    public void loadInventory(){
//        Json json = new Json();
//        Json json1 = new Json();
//        Array<InventoryItemLocation> list = json.fromJson(Array.class, Gdx.files.internal("save/savePlayerEquip.json"));
//        Array<InventoryItemLocation> list1 = json1.fromJson(Array.class, Gdx.files.internal("save/saveInventory.json"));
//        fillInventory( containerSlotsTable,list,dragAndDrop);
//        fillInventory(inventorySlotsTable,list1,dragAndDrop);
//
//    }



