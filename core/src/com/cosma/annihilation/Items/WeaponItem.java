package com.cosma.annihilation.Items;

import com.badlogic.gdx.graphics.Texture;


public class WeaponItem extends InventoryItem {


    private int damage;

    public WeaponItem(){}

    public WeaponItem(String textureName,ItemID itemID,int damage,int itemAttributes, int itemUseType, int itemValue, String name,boolean stackable){
        super(textureName,itemID,itemAttributes,itemUseType,itemValue,name,stackable);
        this.damage = damage;
    }

    public WeaponItem(WeaponItem weaponItem){
        super(weaponItem);
        this.damage = weaponItem.getDamage();
    }

    public int getDamage() {
        return damage;
    }
}
