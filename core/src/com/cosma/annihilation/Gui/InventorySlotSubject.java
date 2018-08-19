package com.cosma.annihilation.Gui;

public interface InventorySlotSubject {

    public void addObserver(InventorySlot inventorySlot);
    public void removeObserver(InventorySlot inventorySlot);
    public void removeAllObservers();
    public void notify(final InventorySlot slot);
    public InventorySlot getUpdate(InventorySlot inventorySlot);


}
