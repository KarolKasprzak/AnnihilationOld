package com.cosma.annihilation.Gui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.cosma.annihilation.Items.InventoryItem;

public class InventorySlotTarget extends DragAndDrop.Target {

    InventorySlot targetSlot;

    public InventorySlotTarget(InventorySlot actor) {
        super(actor);
        targetSlot = actor;
    }

    @Override
    public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {

        return true;
    }

    @Override
    public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {

        InventoryItem sourceItem = (InventoryItem) payload.getDragActor();
        InventoryItem targetItem = targetSlot.getInventoryItem();
        InventorySlot sourceSlot = ((InventorySlotSource)source).getSourceSlot();

        if( sourceItem == null ) {
            return;
        }
        //First, does the slot accept the source item type?
        if( !targetSlot.doesAcceptItemUseType(sourceItem.getItemUseType()))  {
            //Put item back where it came from, slot doesn't accept item
            sourceSlot.add(sourceItem);
            return;
        }

        if( !targetSlot.hasItem()){
            targetSlot.add(sourceItem);
        }else{
            //If the same item and stackable, add
            if( sourceItem.isSameItemType(targetItem) && sourceItem.isStackable()){
                targetSlot.add(sourceItem);
            }else
                sourceSlot.add(sourceItem);
        }
    }
}
