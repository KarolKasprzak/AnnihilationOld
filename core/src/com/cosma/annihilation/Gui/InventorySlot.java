package com.cosma.annihilation.Gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.SnapshotArray;
import com.cosma.annihilation.Items.InventoryItem;
import com.cosma.annihilation.Utils.AssetsLoader;

public class InventorySlot extends Stack {
    private int itemValue = 0;
    private int itemType = 0;
    private int itemTypeFilter = 0;
    private Image backgroundImage;
    private Label itemValueLabel;
    private Stack stack;
    private Label itemsNumberLabel;

    public InventorySlot(){
        stack = new Stack();
        backgroundImage = new Image();
        Image backgroundImageStandard = new Image( (Texture) AssetsLoader.getResource("stack_default"));
        stack.add(backgroundImageStandard);
        stack.setName("background");
        this.add(stack);
        Skin skin = new Skin(Gdx.files.internal("UI/skin/pixthulhu-ui.json"));
        itemsNumberLabel = new Label(String.valueOf(itemValue),skin);
        itemsNumberLabel.setAlignment(Align.bottomRight);
        itemsNumberLabel.setVisible(true);
        this.add(itemsNumberLabel);
    }
    public InventorySlot(int itemTypeFilter,Image backgroundImage) {
        this();
        itemTypeFilter = itemTypeFilter;
        this.backgroundImage = backgroundImage;
        stack.add(backgroundImage);

    }

    public boolean doesAcceptItemUseType(int itemType){
        if( itemTypeFilter == 0 ){
            return true;
        }else {
            return (( itemTypeFilter & itemType) == itemType);
        }
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



    @Override
    public void add(Actor actor) {
        super.add(actor);

        if( itemsNumberLabel == null ){
            return;
        }

//        if( !actor.equals(defaultStack) && !actor.equals(itemsNumberLabel) ) {
//            incrementItemCount(true);
//        }
    }


}
