package com.cosma.annihilation.Utils.Serialization;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.cosma.annihilation.Components.ActionComponent;
import com.cosma.annihilation.Components.AnimationComponent;
import com.cosma.annihilation.Components.ContainerComponent;
import com.cosma.annihilation.Components.HealthComponent;
import com.cosma.annihilation.Gui.Inventory.InventoryItemLocation;

public class GameEntitySerializer implements Json.Serializer<Entity>  {
    @Override
    public void write(Json json, Entity object, Class knownType) {
        json.writeObjectStart();
         for (Component component : object.getComponents()) {
             if (component instanceof HealthComponent) {
                 json.writeValue("hp", ((HealthComponent) component).hp);
                 json.writeValue("maxHP", ((HealthComponent) component).maxHP);
             }
             if (component instanceof ActionComponent) {
                 json.writeValue("action", ((ActionComponent) component).action.name());
             }
             if (component instanceof AnimationComponent) {
                 json.writeValue("id",((AnimationComponent) component).animationId.name());
             }
             if (component instanceof ContainerComponent) {
                 json.writeValue("name", ((ContainerComponent) component).name);
                 json.writeArrayStart("itemList");
                 for (InventoryItemLocation location : ((ContainerComponent) component).itemLocations) {
                     json.writeObjectStart();
                     json.writeValue("tableIndex", location.getTableIndex());
                     json.writeValue("itemID", location.getItemID());
                     json.writeValue("itemsAmount", location.getItemsAmount());
                     json.writeObjectEnd();
                 }
                 json.writeArrayEnd();
             }
         }
         json.writeObjectEnd();
    }

    @Override
    public Entity read(Json json, JsonValue jsonData, Class type) {
        return null;
    }
}
