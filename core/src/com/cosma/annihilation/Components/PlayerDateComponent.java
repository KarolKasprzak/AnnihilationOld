package com.cosma.annihilation.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Array;
import com.cosma.annihilation.Gui.Inventory.InventoryItemLocation;

public class PlayerDateComponent implements Component {
    public Array<InventoryItemLocation> equippedItem;
    public Array<InventoryItemLocation> inventoryItem;
}
