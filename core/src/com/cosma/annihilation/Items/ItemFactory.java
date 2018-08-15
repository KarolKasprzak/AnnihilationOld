package com.cosma.annihilation.Items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.cosma.annihilation.Items.InventoryItem;
import com.cosma.annihilation.Items.InventoryItem.ItemID;
import java.util.ArrayList;
import java.util.Hashtable;


public class ItemFactory {
    private Json json = new Json();
    private Hashtable<ItemID, Object> weaponItemMap;
//    private Hashtable<ItemID, ArmourItem> armourItemMap;
//    private Hashtable<ItemID, StandardItem> standardItemMap;
    private final String WEAPON_ITEM_PATH = "json/items/weapons/weapons.json";
    private final String ARMOUR_ITEM_PATH = "json/items/armours/armours.json";
    private final String STANDARD_ITEM_PATH = "json/items/standard/standard.json";

    public ItemFactory() {
        ArrayList<JsonValue> list = json.fromJson(ArrayList.class, Gdx.files.internal(WEAPON_ITEM_PATH));

        weaponItemMap = new Hashtable<ItemID, Object>();

        for (JsonValue jsonValue : list) {
            if(jsonValue.getString("type").equals("weapon")){

            }
            WeaponItem inventoryItem = json.readValue(WeaponItem.class, jsonValue);
//            InventoryItem inventoryItem = json.readValue(InventoryItem.class, jsonValue);
            weaponItemMap.put(inventoryItem.getItemID(), inventoryItem);

        }

    }

    public InventoryItem getItem(ItemID itemID){
         InventoryItem item = new InventoryItem()

        return item;

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








