package com.cosma.annihilation.Gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;
import com.cosma.annihilation.Items.InventoryItem;
import com.cosma.annihilation.Utils.AssetsLoader;

public class InventorySlot extends Stack {
    private int itemsAmount = 0;
    private int itemTypeFilter;
    private Image backgroundImage;
    private Label itemsAmountLabel;
    private Stack stack;


    public InventorySlot(){

        stack = new Stack();
        backgroundImage = new Image();
        Image backgroundImageStandard = new Image( (Texture) AssetsLoader.getResource("stack_default"));
        stack.add(backgroundImageStandard);
        stack.setName("background");
        this.add(stack);
        Skin skin = new Skin(Gdx.files.internal("interface/skin/pixthulhu-ui.json"));
        itemsAmountLabel = new Label(String.valueOf(itemsAmount),skin);
        itemsAmountLabel.setFontScale(0.5f);
        itemsAmountLabel.setAlignment(Align.bottomRight);
        itemsAmountLabel.setVisible(false);
        this.add(itemsAmountLabel);
    }
    public InventorySlot(int itemTypeFilter,Image backgroundImage) {
        this();
        this.itemTypeFilter = itemTypeFilter;
        this.backgroundImage = backgroundImage;
        stack.add(backgroundImage);

    }

    public boolean doesAcceptItemUseType(int itemType){
        if( itemTypeFilter == 0 ){
            return true;
        }else
            {
            return (( itemTypeFilter & itemType) == itemType);
        }
    }

    public boolean hasItem(){
        if( hasChildren() ){
            SnapshotArray<Actor> items = this.getChildren();
            if( items.size > 2 ){
                return true;
            }
        }
        return false;
    }

    private void checkVisibilityOfItemCount(){
        if( itemsAmount < 2){
            itemsAmountLabel.setVisible(false);
        }else{
            itemsAmountLabel.setVisible(true);
        }
    }

    public void decrementItemCount() {
        itemsAmount--;
       itemsAmountLabel.setText(String.valueOf(itemsAmount));
//        if( _defaultBackground.getChildren().size == 1 ){
//            _defaultBackground.add(_customBackgroundDecal);
//        }
        checkVisibilityOfItemCount();


    }

    public void incrementItemCount() {
        itemsAmount++;
        itemsAmountLabel.setText(String.valueOf(itemsAmount));
//        if( _defaultBackground.getChildren().size > 1 ){
//            _defaultBackground.getChildren().pop();
//        }
        checkVisibilityOfItemCount();

    }

    public InventoryItem getInventoryItem(){
        InventoryItem actor = null;
        if( this.hasChildren() ){
            SnapshotArray<Actor> items = this.getChildren();
            if( items.size > 2 ){
                actor = (InventoryItem) items.peek();
            }
        }
        return actor;
    }
    public Array<Actor> getAllInventoryItems() {
        Array<Actor> items = new Array<Actor>();
        if( hasItem() ){
            SnapshotArray<Actor> arrayChildren = this.getChildren();
            int numInventoryItems =  arrayChildren.size - 2;
            for(int i = 0; i < numInventoryItems; i++) {
                decrementItemCount();
                items.add(arrayChildren.pop());
            }
        }
        return items;
    }

    public int getItemsNumber(){
        if( hasChildren() ){
            SnapshotArray<Actor> items = this.getChildren();
            return items.size - 2;
        }
        return 0;
    }


    public void clearAllInventoryItems() {
        if( hasItem() ){
            SnapshotArray<Actor> arrayChildren = this.getChildren();
            int numInventoryItems =  getItemsNumber();
            for(int i = 0; i < numInventoryItems; i++) {

                arrayChildren.pop();
            }
        }
    }


    @Override
    public void add(Actor actor) {
        super.add(actor);

        if( itemsAmountLabel == null ){
            return;
        }

        if( !actor.equals(stack) && !actor.equals(itemsAmountLabel) ) {
            incrementItemCount();
        }
    }

}