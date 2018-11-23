package com.cosma.annihilation.Gui;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Array;
import com.cosma.annihilation.Components.PlayerComponent;
import com.cosma.annihilation.Components.PlayerDateComponent;
import com.cosma.annihilation.Gui.Inventory.*;
import com.cosma.annihilation.Items.InventoryItem;
import com.cosma.annihilation.Items.ItemFactory;
import com.cosma.annihilation.Items.WeaponItem;
import com.cosma.annihilation.Systems.ActionSystem;
import com.cosma.annihilation.Utils.LoaderOLD;
import com.cosma.annihilation.Utils.Utilities;


public class InventoryWindow extends Window implements InventorySlotObserver {
    private Engine engine;
    private DragAndDrop dragAndDrop;
    private Table leftTable;
    private Table rightTable;

    private Table inventorySlotsTable;
    private Table equipmentSlotsTable;
    private Table statsTable;
    private Skin skin;
    private InventorySlot weaponInventorySlot;
    private Label dmgLabel;
    private Label defLabel;
    private int inventorySize = 16;
    private float slotSize = Utilities.setWindowHeight(0.1f);
    private ActorGestureListener listener;
    private ItemStatsWindow itemStatsWindow;
    private InventoryWindow inventoryWindow;

    InventoryWindow(String title, Skin skin, final Engine engine, float x, float y) {
        super(title, skin);
        this.engine = engine;
        this.skin = skin;
        this.debugAll();
        this.setSize(x, y);



        inventoryWindow = this;
        dragAndDrop = new DragAndDrop();

        leftTable = new Table(skin);
        leftTable.setDebug(true);
        rightTable = new Table(skin);
        rightTable.setDebug(true);

        itemStatsWindow = new ItemStatsWindow("", skin);
        itemStatsWindow.setVisible(false);
        itemStatsWindow.setMovable(false);


        listener = new ActorGestureListener() {
            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {

                InventorySlot inventorySlot = (InventorySlot) event.getListenerActor();
                if (inventorySlot.hasItem()) {
                    if (inventorySlot.getInventoryItem() instanceof WeaponItem) {
                        itemStatsWindow.setDmglabel((WeaponItem) inventorySlot.getInventoryItem());
                        itemStatsWindow.setVisible(true);
                    }
                }

                super.touchDown(event, x, y, pointer, button);
            }
        };
        this.add(leftTable).width(x*0.575f);
        this.add(rightTable).width(x*0.375f);

        createStatsTable();
        createEquipmentTable();
        createInventoryTable();

        leftTable.add(equipmentSlotsTable);
        leftTable.row();
        leftTable.add(inventorySlotsTable.bottom());

        rightTable.add(itemStatsWindow).expandY().expandX().fill(true);



//
//        this.add(equipmentSlotsTable);
//        this.add(statsTable);
//        this.row();
//        this.add(inventorySlotsTable.bottom());
//        this.add(itemStatsWindow).expandY().expandX().fill(true);

    }

    private void createStatsTable() {
        statsTable = new Table();
        statsTable.center();
        dmgLabel = new Label("dmg:", skin);
        dmgLabel.setColor(0, 82, 0, 255);
        defLabel = new Label("def:", skin);
        defLabel.setColor(0, 82, 0, 255);
        statsTable.add(dmgLabel).expand().bottom().fillX().padRight(50);
        statsTable.add(defLabel).right();
    }

    private void createEquipmentTable() {
        equipmentSlotsTable = new Table();

        InventorySlot armourInventorySlot = new InventorySlot();


        weaponInventorySlot = new InventorySlot(InventoryItem.ItemUseType.WEAPON_DISTANCE.getValue(), new Image((Texture) LoaderOLD.getResource("stack_default")));
        weaponInventorySlot.register(this);


        dragAndDrop.addTarget(new InventorySlotTarget(armourInventorySlot));
        dragAndDrop.addTarget(new InventorySlotTarget(weaponInventorySlot));

        equipmentSlotsTable.add(weaponInventorySlot).size(Utilities.setWindowWidth(0.1f), Utilities.setWindowHeight(0.1f)).pad(Utilities.setWindowHeight(0.006f));
        equipmentSlotsTable.row();
        equipmentSlotsTable.add(armourInventorySlot).size(Utilities.setWindowWidth(0.1f), Utilities.setWindowHeight(0.1f)).pad(Utilities.setWindowHeight(0.006f));


        equipmentSlotsTable.center();
        equipmentSlotsTable.pad(20);
    }

    public void setInventorySize(int size) {
        inventorySize = size;
    }


    private void createInventoryTable() {
        inventorySlotsTable = new Table();
        inventorySlotsTable.bottom();
        inventorySlotsTable.setDebug(false);
        inventorySlotsTable.setFillParent(false);

        for (int i = 1; i <= 24; i++) {
            InventorySlot inventorySlot = new InventorySlot();
            inventorySlot.addListener(listener);
            dragAndDrop.addTarget(new InventorySlotTarget(inventorySlot));
            inventorySlotsTable.add(inventorySlot).size(slotSize, slotSize).pad(Utilities.setWindowHeight(0.005f));
            if (i == 8 || i == 16) inventorySlotsTable.row();
        }
    }

    public static void clearItemsTable(Table targetTable) {
        Array<Cell> cells = targetTable.getCells();
        for (int i = 0; i < cells.size; i++) {
            InventorySlot inventorySlot = (InventorySlot) cells.get(i).getActor();
            if (inventorySlot == null) continue;
            inventorySlot.clearAllItems();
        }
    }

    public static Array<InventoryItemLocation> getItemsTable(Table targetTable) {
        Array<Cell> cells = targetTable.getCells();
        Array<InventoryItemLocation> items = new Array<InventoryItemLocation>();
        for (int i = 0; i < cells.size; i++) {
            InventorySlot inventorySlot = ((InventorySlot) cells.get(i).getActor());
            if (inventorySlot == null) continue;

            int itemNumber = inventorySlot.getItemsNumber();
            if (itemNumber > 0) {

                items.add(new InventoryItemLocation(i, inventorySlot.getInventoryItem().getItemID().toString(), itemNumber));
            }
        }
        return items;
    }

    public static void fillInventory(Table targetTable, Array<InventoryItemLocation> inventoryItems, DragAndDrop dragAndDrop) {
        clearItemsTable(targetTable);
        Array<Cell> cells = targetTable.getCells();
        for (int i = 0; i < inventoryItems.size; i++) {
            InventoryItemLocation itemLocation = inventoryItems.get(i);
            InventoryItem.ItemID itemID = InventoryItem.ItemID.valueOf(itemLocation.getItemID());
            InventorySlot inventorySlot = ((InventorySlot) cells.get(itemLocation.getTableIndex()).getActor());
            for (int index = 0; index < itemLocation.getItemsAmount(); index++) {
                InventoryItem item = ItemFactory.getInstance().getItem(itemID);
                item.setName(targetTable.getName());
                inventorySlot.add(item);
                dragAndDrop.addSource(new InventorySlotSource(inventorySlot, dragAndDrop));
            }
        }
    }

    private void setDmgLabel() {
        if (getActiveWeapon() != null) {
            dmgLabel.setText("dmg " + getActiveWeapon().getDamage());
        } else
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

    private WeaponItem getActiveWeapon() {
        if (weaponInventorySlot.getInventoryItem() == null) {
            return null;
        }
        return (WeaponItem) weaponInventorySlot.getInventoryItem();
    }

    private void setActivePlayerWeapon() {
        engine.getEntitiesFor(Family.all(PlayerComponent.class).get()).first()
                .getComponent(PlayerComponent.class).activeWeapon = getActiveWeapon();
        if (!weaponInventorySlot.hasItem()) {
            engine.getEntitiesFor(Family.all(PlayerComponent.class).get()).first()
                    .getComponent(PlayerComponent.class).activeWeapon = null;

        }
    }

    private void removeActivePlayerWeapon() {
        if (weaponInventorySlot.hasItem()) {
            engine.getEntitiesFor(Family.all(PlayerComponent.class).get()).first()
                    .getComponent(PlayerComponent.class).activeWeapon = null;
        }
    }


    @Override
    public void onNotify(InventorySlot inventorySlot, InventorySlotEvent event) {
        if (event == InventorySlotEvent.ADDED_ITEM) {
            setDmgLabel();
            setActivePlayerWeapon();
        }
        if (event == InventorySlotEvent.REMOVED_ITEM) {
            dmgLabel.setText("no weapon");
            removeActivePlayerWeapon();
        }
    }
}
