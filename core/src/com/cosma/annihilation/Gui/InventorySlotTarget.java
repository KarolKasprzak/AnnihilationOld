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

//        if( sourceItem == null ) {
//            return;
//        }
        targetSlot.add(sourceItem);

    }
}
