package com.cosma.annihilation.Gui;

public class InventoryItemLocation {
    private int tableIndex;
    private String itemID;
    private int itemsAmount;

    public InventoryItemLocation(){
    }

    public InventoryItemLocation(int tableIndex, String itemID, int itemsAmount){
        this.tableIndex = tableIndex;
        this.itemID = itemID;
        this.itemsAmount = itemsAmount;
    }

    public int getTableIndex() {
        return tableIndex;
    }

    public String getItemID() {
        return itemID;
    }

    public int getItemsAmount() {
        return itemsAmount;
    }
}