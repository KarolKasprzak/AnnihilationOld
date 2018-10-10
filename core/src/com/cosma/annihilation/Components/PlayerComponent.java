package com.cosma.annihilation.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.cosma.annihilation.Gui.InventoryItemLocation;

public class PlayerComponent implements Component{
    public float velocity = 2;
    public int numFootContacts = 0;
    public boolean hidde = false;
    public Array<InventoryItemLocation> equippedItem;
    public Array<InventoryItemLocation> inventoryItem;

}
