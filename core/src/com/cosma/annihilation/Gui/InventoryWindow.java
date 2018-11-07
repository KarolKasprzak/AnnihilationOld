package com.cosma.annihilation.Gui;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Array;
import com.cosma.annihilation.Components.PlayerComponent;
import com.cosma.annihilation.Components.PlayerDateComponent;
import com.cosma.annihilation.Gui.Inventory.*;
import com.cosma.annihilation.Items.InventoryItem;
import com.cosma.annihilation.Items.ItemFactory;
import com.cosma.annihilation.Items.WeaponItem;
import com.cosma.annihilation.Utils.LoaderOLD;


public class InventoryWindow extends Window implements InventorySlotObserver {
    private Engine engine;
    private DragAndDrop dragAndDrop;
    private Table inventorySlotsTable;
    private Table equipmentSlotsTable;
    private Table statsTable;
    private Skin skin;
    private InventorySlot weaponInventorySlot;
    private Label dmgLabel;
    private Label defLabel;


    InventoryWindow(String title, Skin skin, Engine engine) {
        super(title, skin);
        dragAndDrop = new DragAndDrop();
        this.engine = engine;
        this.skin = skin;


        createStatsTable();
        createEquipmentTable();
        createInventoryTable();

    }

   private void createStatsTable(){
       statsTable = new Table();
       statsTable.center();
       dmgLabel = new Label("dmg:",skin);
       defLabel = new Label("def:",skin);
       statsTable.add(dmgLabel).expand().bottom().fillX().padRight(50);
       statsTable.add(defLabel).right();
       this.add(statsTable);
       this.row();
   }

    private void createEquipmentTable() {
        equipmentSlotsTable = new Table();

        InventorySlot headInventorySlot = new InventorySlot();
        InventorySlot bodyInventorySlot = new InventorySlot();
        InventorySlot legsInventorySlot = new InventorySlot();

        weaponInventorySlot = new InventorySlot(InventoryItem.ItemUseType.WEAPON_DISTANCE.getValue(), new Image((Texture) LoaderOLD.getResource("stack_default")));
        weaponInventorySlot.register(this);

        InventorySlot rightInventorySlot = new InventorySlot();
        dragAndDrop.addTarget(new InventorySlotTarget(headInventorySlot));
        dragAndDrop.addTarget(new InventorySlotTarget(bodyInventorySlot));
        dragAndDrop.addTarget(new InventorySlotTarget(legsInventorySlot));
        dragAndDrop.addTarget(new InventorySlotTarget(weaponInventorySlot));
        dragAndDrop.addTarget(new InventorySlotTarget(rightInventorySlot));
        equipmentSlotsTable.add();
        equipmentSlotsTable.add(headInventorySlot).size(50, 50).pad(2);
        equipmentSlotsTable.add();
        equipmentSlotsTable.row();
        equipmentSlotsTable.add(weaponInventorySlot).size(50, 50).pad(2);
        equipmentSlotsTable.add(bodyInventorySlot).size(50, 50).pad(2);
        equipmentSlotsTable.add(rightInventorySlot).size(50, 50).pad(2);
        equipmentSlotsTable.row();
        equipmentSlotsTable.add();
        equipmentSlotsTable.add(legsInventorySlot).size(50, 50).pad(2);
        equipmentSlotsTable.add();
        equipmentSlotsTable.center();
        equipmentSlotsTable.pad(20);
        this.add(equipmentSlotsTable);
        this.row();
    }

    private void createInventoryTable() {
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

    public static void clearItemsTable(Table targetTable){
        Array<Cell> cells = targetTable.getCells();
        for( int i = 0; i < cells.size; i++){
            InventorySlot inventorySlot = (InventorySlot)cells.get(i).getActor();
            if( inventorySlot == null ) continue;
            inventorySlot.clearAllItems();
        }
    }

    public static Array<InventoryItemLocation> getItemsTable(Table targetTable){
        Array<Cell> cells = targetTable.getCells();
        Array<InventoryItemLocation> items = new Array<InventoryItemLocation>();
        for(int i = 0; i < cells.size; i++){
            InventorySlot inventorySlot = ((InventorySlot)cells.get(i).getActor());
            if(inventorySlot == null) continue;

            int itemNumber = inventorySlot.getItemsNumber();
            if(itemNumber> 0){

                items.add(new InventoryItemLocation(i,inventorySlot.getInventoryItem().getItemID().toString(),itemNumber));
            }
        }
        return items;
    }
    public static  void fillInventory(Table targetTable, Array<InventoryItemLocation> inventoryItems, DragAndDrop dragAndDrop ){
        clearItemsTable(targetTable);
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

    private void setDmgLabel(){
        if(getActiveWeapon() != null){
            dmgLabel.setText("dmg " + getActiveWeapon().getDamage());
        }else
            dmgLabel.setText("no weapon");
    }

    void saveInventory(Engine engine) {
        Entity player = engine.getEntitiesFor(Family.all(PlayerComponent.class).get()).first();
        player.getComponent(PlayerDateComponent.class).inventoryItem = getItemsTable(inventorySlotsTable);
        player.getComponent(PlayerDateComponent.class).equippedItem = getItemsTable(equipmentSlotsTable);
    }

    void loadInventory(Engine engine) {
        clearItemsTable(equipmentSlotsTable);
        clearItemsTable(inventorySlotsTable);
        Entity player = engine.getEntitiesFor(Family.all(PlayerComponent.class).get()).first();
        if (player.getComponent(PlayerDateComponent.class).equippedItem != null) {
            Array equipment = player.getComponent(PlayerDateComponent.class).equippedItem;
            fillInventory(equipmentSlotsTable, equipment, dragAndDrop);
        }
        if (player.getComponent(PlayerDateComponent.class).inventoryItem != null) {
            Array inventory = player.getComponent(PlayerDateComponent.class).inventoryItem;
            fillInventory(inventorySlotsTable, inventory, dragAndDrop);
        }
        setDmgLabel();
    }

    private WeaponItem getActiveWeapon(){
        if (weaponInventorySlot.getInventoryItem() == null) {
            return null;
        }
        return (WeaponItem) weaponInventorySlot.getInventoryItem();
    }

    private void setActivePlayerWeapon(){
        engine.getEntitiesFor(Family.all(PlayerComponent.class).get()).first()
                .getComponent(PlayerComponent.class).activeWeapon = getActiveWeapon();
        if(!weaponInventorySlot.hasItem()){
            engine.getEntitiesFor(Family.all(PlayerComponent.class).get()).first()
                    .getComponent(PlayerComponent.class).activeWeapon = null;

        }
    }

    private void removeActivePlayerWeapon(){
        if(weaponInventorySlot.hasItem()){
            engine.getEntitiesFor(Family.all(PlayerComponent.class).get()).first()
                    .getComponent(PlayerComponent.class).activeWeapon = null;
        }
    }


    @Override
    public void onNotify(InventorySlot inventorySlot, InventorySlotEvent event) {
        if(event == InventorySlotEvent.ADDED_ITEM){
            setDmgLabel();
            setActivePlayerWeapon();
        }
        if(event == InventorySlotEvent.REMOVED_ITEM){
            dmgLabel.setText("no weapon");
            removeActivePlayerWeapon();
        }
    }
}
