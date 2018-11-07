package com.cosma.annihilation.Items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.cosma.annihilation.Utils.LoaderOLD;


public class InventoryItem extends Image {
    private int itemAttributes;
    private int itemUseType;
    private int itemValue;
    private ItemID itemID;
    private float weight;
    private String itemName;
    private String itemShortDescription;
    private String textureName;
    private Texture texture;
    private boolean stackable;

    public enum ItemID {
        MP44,BOX,P38;
    }



    public enum ItemAttribute{
        CONSUMABLE(1),
        EQUIPPABLE(2);

        private int attribute;

        ItemAttribute(int attribute){
            this.attribute = attribute;
        }

        public int getValue(){
            return attribute;
        }

    }

    public enum ItemUseType{
        ITEM_RESTORE_HEALTH(1),
        ITEM_RESTORE_MP(2),
        ITEM_DAMAGE(4),
        WEAPON_CLOSE(8),
        WEAPON_DISTANCE(16),
        WAND_ONEHAND(32),
        WAND_TWOHAND(64),
        ARMOR_SHIELD(128),
        ARMOR_HELMET(256),
        ARMOR_CHEST(512),
        ARMOR_FEET(1024);

        private int itemUseType;

        ItemUseType(int itemUseType){
            this.itemUseType = itemUseType;
        }

        public int getValue(){
            return itemUseType;
        }
    }



    public InventoryItem(){}

    public InventoryItem(String textureName,ItemID itemID,int itemAttributes, int itemUseType, int itemValue,String itemName,boolean stackable,String itemShortDescription){
           this.textureName = textureName;
           this.itemID = itemID;
           texture = (Texture)LoaderOLD.getResource(textureName);
           this.setDrawable(new TextureRegionDrawable(new TextureRegion(texture)));
           this.itemAttributes = itemAttributes;
           this.itemUseType = itemUseType;
           this.itemValue = itemValue;
           this.itemName = itemName;
           this.stackable = stackable;
           this.itemShortDescription = itemShortDescription;

    }
    public InventoryItem(InventoryItem inventoryItem){
        this.textureName = inventoryItem.getTextureName();
        this.itemID = inventoryItem.getItemID();
        this.itemAttributes = inventoryItem.getItemAttributes();
        this.itemUseType = inventoryItem.getItemUseType();
        this.itemValue = inventoryItem.getItemValue();
        this.itemName = inventoryItem.getItemName();
        texture = (Texture)LoaderOLD.getResource(textureName);
        this.setDrawable(new TextureRegionDrawable(new TextureRegion(texture)));
        this.stackable = inventoryItem.isStackable();
        this.itemShortDescription = inventoryItem.getItemShortDescription();
    }
    public int getItemValue() {
        return itemValue;
    }
    public ItemID getItemID(){
        return this.itemID;
    }
    public String getTextureName() {
        return textureName;
    }
    public int getItemAttributes() {
        return itemAttributes;
    }
    public int getItemUseType() {
        return itemUseType;
    }
    public float getItemWeight(){
        return weight;
    }
    public String getItemName() {return itemName;}
    public void setItemWeight(float weight){
        this.weight = weight;
    }
    public String getItemShortDescription() {
        return itemShortDescription;
    }
    public boolean isStackable(){
       return stackable;
    }

    public boolean isSameItemType(InventoryItem candidateInventoryItem){
        return itemID == candidateInventoryItem.getItemID();
    }

    public int getTradeValue(){
        return MathUtils.floor(itemValue*.33f)+2;
    }


}
