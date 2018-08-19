package com.cosma.annihilation.Gui;

public interface InventorySlotObserver {


    void weponChange(final InventorySlot slot);
    void setSubject(InventorySlotSubject subject);
}