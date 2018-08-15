package com.cosma.annihilation.Items;

import com.badlogic.gdx.graphics.Texture;


public class WeaponItem extends InventoryItem {


    private int damage;

    public WeaponItem(){}

    public WeaponItem(String textureName,ItemID itemID,int damage){
        super(textureName,itemID);
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
