package com.cosma.annihilation.Items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.cosma.annihilation.Utils.AssetsLoader;


public class InventoryItem extends Image {
    private ItemID itemID;
    private float weight;
    private String name;
    private String shortDescription;
    private String textureName;
    private Texture texture;

    public enum ItemID {
        MP44,BOX;
    }

    public InventoryItem(){}

    public InventoryItem(String textureName,ItemID itemID){
           this.textureName = textureName;
           this.itemID = itemID;
           texture = (Texture)AssetsLoader.getResource(textureName);
           this.setDrawable(new TextureRegionDrawable(new TextureRegion(texture)));
    }
    public InventoryItem(InventoryItem inventoryItem){
        this.textureName = inventoryItem.getTextureName();
        this.itemID = inventoryItem.getItemID();
        texture = (Texture)AssetsLoader.getResource(textureName);
        this.setDrawable(new TextureRegionDrawable(new TextureRegion(texture)));
    }


    public String getTextureName() {
        return textureName;
    }

    public ItemID getItemID(){
        return this.itemID;
    }

    public float getItemWeight(){
        return weight;
    }
    public void setItemWeight(float weight){
        this.weight = weight;
    }
    public String getItemShortDescription() {
        return shortDescription;
    }
}
