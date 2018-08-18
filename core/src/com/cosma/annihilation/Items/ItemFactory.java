package com.cosma.annihilation.Items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.cosma.annihilation.Items.InventoryItem;
import com.cosma.annihilation.Items.InventoryItem.ItemID;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;


public class ItemFactory {
    private Json json = new Json();
    private Hashtable<ItemID, Object> itemMap;
    private static ItemFactory instance = null;
    private final String WEAPON_ITEM_PATH = "json/items/weapons/weapons.json";
    private final String ARMOUR_ITEM_PATH = "json/items/armours/armours.json";
    private final String STANDARD_ITEM_PATH = "json/items/standard/items.json";


    public static ItemFactory getInstance() {
        if (instance == null) {
            instance = new ItemFactory();
        }

        return instance;
    }

    public ItemFactory() {
        ArrayList<JsonValue> list = json.fromJson(ArrayList.class, Gdx.files.internal(WEAPON_ITEM_PATH));
        ArrayList<JsonValue> list1 = json.fromJson(ArrayList.class, Gdx.files.internal(STANDARD_ITEM_PATH));


        itemMap = new Hashtable<ItemID, Object>();
        json.setIgnoreUnknownFields(true);

        for (JsonValue jsonValue : list) {
            WeaponItem weaponItem = json.readValue(WeaponItem.class, jsonValue);
            itemMap.put(weaponItem.getItemID(), weaponItem);
        }
        for (JsonValue jsonValue : list1) {
            InventoryItem inventoryItem = json.readValue(InventoryItem.class, jsonValue);
            itemMap.put(inventoryItem.getItemID(), inventoryItem);
        }

    }


    public <T> T getItemT(ItemID itemID) {
        Class type = itemMap.get(itemID).getClass();
        if (type == InventoryItem.class) {
            InventoryItem item = new InventoryItem((InventoryItem) itemMap.get(itemID));
            return (T) (InventoryItem) item;
        }
        if (type == WeaponItem.class) {
            WeaponItem item = new WeaponItem((WeaponItem) itemMap.get(itemID));
            return (T) (WeaponItem) item;
        }
        return null;
    }

//    public InventoryItem getItem(ItemID itemID) {
//
//        InventoryItem item = new InventoryItem((InventoryItem) itemMap.get(itemID));
//        return  item;
//    }


    public InventoryItem getItem(ItemID itemID) {
        Class type = itemMap.get(itemID).getClass();
        if (type == InventoryItem.class) {
            InventoryItem item = new InventoryItem((InventoryItem) itemMap.get(itemID));
            return (InventoryItem) (InventoryItem) item;
        }
        if (type == WeaponItem.class) {
            WeaponItem item = new WeaponItem((WeaponItem) itemMap.get(itemID));
            return (InventoryItem) (WeaponItem) item;
        }
        return null;
    }

    public WeaponItem getWeapon(ItemID itemID) {
        Class type = itemMap.get(itemID).getClass();
        if (type == InventoryItem.class) {
            InventoryItem item = new InventoryItem((InventoryItem) itemMap.get(itemID));
            return (WeaponItem) (InventoryItem) item;
        }
        if (type == WeaponItem.class) {
            WeaponItem item = new WeaponItem((WeaponItem) itemMap.get(itemID));
            return (WeaponItem) item;
        }
        return  null;
    }

}

//        System.out.println(list);



//


//
//        public InventoryItem getInventoryItem(ItemID ){
//            return
//
//        ArrayList<JsonValue> list = _json.fromJson(ArrayList.class, Gdx.files.internal(INVENTORY_ITEM));
//        _inventoryItemList = new Hashtable<ItemTypeID, InventoryItem>();
//
//        for (JsonValue jsonVal : list) {
//            InventoryItem inventoryItem = _json.readValue(InventoryItem.class, jsonVal);
//            _inventoryItemList.put(inventoryItem.getItemTypeID(), inventoryItem);
//        }
//    }
//
//    public InventoryItem getInventoryItem(ItemTypeID inventoryItemType){
//        InventoryItem item = new InventoryItem(_inventoryItemList.get(inventoryItemType));
//        item.setDrawable(new TextureRegionDrawable(Utility.ITEMS_TEXTUREATLAS.findRegion(item.getItemTypeID().toString())));
//        item.setScaling(Scaling.none);
//        return item;
//    }
//}








