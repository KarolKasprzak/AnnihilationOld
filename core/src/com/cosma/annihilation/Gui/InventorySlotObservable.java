package com.cosma.annihilation.Gui;

public interface InventorySlotObservable {

    public void addObserver(InventorySlotObserver inventorySlotObserver);
    public void removeObserver(InventorySlotObserver inventorySlotObserver);
    public void removeAllObservers();
    public void notify(final InventorySlot slot, InventorySlotObserver.SlotEvent event);

}
