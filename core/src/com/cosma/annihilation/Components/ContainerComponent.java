package com.cosma.annihilation.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Array;
import com.cosma.annihilation.Gui.InventoryItemLocation;
import com.cosma.annihilation.Items.InventoryItem;

import java.util.Hashtable;

public class ContainerComponent implements Component {
  public String name;
  public Array<InventoryItemLocation> itemLocations;
}
