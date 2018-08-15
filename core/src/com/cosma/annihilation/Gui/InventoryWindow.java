package com.cosma.annihilation.Gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.cosma.annihilation.Components.TransformComponent;
import com.cosma.annihilation.Items.InventoryItem;
import com.cosma.annihilation.Items.ItemFactory;

import java.util.ArrayList;

public class InventoryWindow extends Window {
    private DragAndDrop dragAndDrop;
    private Table inventorySlotsTable;
    private Table equipmentSlotsTable;
    private Skin skin;

    public InventoryWindow(String title, Skin skin) {
        super(title, skin);
        dragAndDrop = new DragAndDrop();
        this.skin = skin;
        createEquipementTable();
        createinventoryTable();
        loadInventory();
    }

    public void createEquipementTable() {
        equipmentSlotsTable = new Table();
        equipmentSlotsTable.setDebug(false);

        InventorySlot headInventorySlot = new InventorySlot();
        InventorySlot bodyInventorySlot = new InventorySlot();
        InventorySlot legsInventorySlot = new InventorySlot();
        InventorySlot leftInventorySlot = new InventorySlot();
        InventorySlot rightInventorySlot = new InventorySlot();

//        ItemFactory itemFactory = new ItemFactory();
//        rightInventorySlot.add(itemFactory.getItem(InventoryItem.ItemID.BOX));
//        legsInventorySlot.add(itemFactory.getWeapon(InventoryItem.ItemID.MP44));

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

    }

    public void createinventoryTable() {
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

    public static void clearInventoryItems(Table targetTable){
        Array<Cell> cells = targetTable.getCells();
        for( int i = 0; i < cells.size; i++){
            InventorySlot inventorySlot = (InventorySlot)cells.get(i).getActor();
            if( inventorySlot == null ) continue;
            inventorySlot.clearAllInventoryItems(false);
        }
    }

    public static Array<InventoryItemLocation> getInventory(Table targetTable){
        Array<Cell> cells = targetTable.getCells();
        Array<InventoryItemLocation> items = new Array<InventoryItemLocation>();
        for(int i = 0; i < cells.size; i++){
            InventorySlot inventorySlot = ((InventorySlot)cells.get(i).getActor());
            if(inventorySlot == null) continue;;
            int itemNumber = inventorySlot.getItemsNumber();
            if(itemNumber> 0){
                items.add(new InventoryItemLocation(i,inventorySlot.getInventoryItem().getItemID().toString(),itemNumber));
            }
        }
        return items;


    }
    public static  void fillInventory(Table targetTable, Array<InventoryItemLocation> inventoryItems, DragAndDrop dragAndDrop ){
        clearInventoryItems(targetTable);
        Array<Cell> cells = targetTable.getCells();
        for (int i =0; i < inventoryItems.size; i++){
            InventoryItemLocation itemLocation = inventoryItems.get(i);
            InventoryItem.ItemID itemID = InventoryItem.ItemID.valueOf(itemLocation.getItemID());
            InventorySlot inventorySlot = ((InventorySlot)cells.get(itemLocation.getTableIndex()).getActor());

            for( int index = 0; index < itemLocation.getItemsAmount(); index++){
                InventoryItem item = ItemFactory.getInstance().getItem(itemID);
                item.setName(targetTable.getName());
                inventorySlot.add(item);
                dragAndDrop.addSource(new InventorySlotSource(inventorySlot, dragAndDrop));
            }
        }

    }

    public void saveInventory(){
        Json json = new Json();
        FileHandle file = Gdx.files.local("save/saveInv.json");
        file.writeString(json.prettyPrint(getInventory(equipmentSlotsTable)),false);

    }
    public void loadInventory(){
        Json json = new Json();
        ArrayList<JsonValue> list = json.fromJson(ArrayList.class, Gdx.files.internal("save/saveInv.json"));
        Array<InventoryItemLocation> list1= json.fromJson(Array.class, Gdx.files.internal("save/saveInv.json"));
        System.out.println(list1);
        fillInventory(equipmentSlotsTable,list1,dragAndDrop);
       
    }









}
