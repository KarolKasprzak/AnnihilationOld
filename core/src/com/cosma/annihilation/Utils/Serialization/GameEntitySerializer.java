package com.cosma.annihilation.Utils.Serialization;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.cosma.annihilation.Components.*;
import com.cosma.annihilation.Gui.Inventory.InventoryItemLocation;
import com.cosma.annihilation.Utils.Util;
import com.kotcrab.vis.ui.widget.VisLabel;

import java.util.HashMap;

public class GameEntitySerializer implements Json.Serializer<Entity>  {

    private World world;
    private Engine engine;
    private HashMap<String, FileHandle> jsonList;
    private Json entityJason;

    public GameEntitySerializer(Engine engine, World world) {
        this.world = world;
        this.engine = engine;

        entityJason = new Json();
        entityJason.setSerializer(Entity.class, new EntitySerializer(world));


        //Load all entity
        jsonList = new HashMap<>();
        FileHandle file = Gdx.files.local("entity");
        for (FileHandle rootDirectory : file.list()) {
            if (rootDirectory.isDirectory()) {
                for (FileHandle childrenDirectory : rootDirectory.list(".json")) {
                    jsonList.put(childrenDirectory.nameWithoutExtension(), childrenDirectory);
                }
            }
        }
    }


    @Override
    public void write(Json json, Entity object, Class knownType) {
        json.writeObjectStart();
         for (Component component : object.getComponents()) {
             if (component instanceof HealthComponent) {
                 json.writeValue("hp", ((HealthComponent) component).hp);
                 json.writeValue("maxHP", ((HealthComponent) component).maxHP);
             }
             if (component instanceof AiComponent) {
                 json.writeValue("startPosition", ((AiComponent) component).startPosition.x+","+((AiComponent) component).startPosition.y);
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
             if (component instanceof BodyComponent) {
                 json.writeValue("position",(((BodyComponent) component).body.getPosition().x)+","+((BodyComponent) component).body.getPosition().y);

             }
         }
         json.writeObjectEnd();
    }

    @Override
    public Entity read(Json json, JsonValue jsonData, Class type) {

        Entity entity = json.fromJson(Entity.class, jsonList.get(jsonData.get("entityName").asString()));
        for(Component component: entity.getComponents()){
            if(component instanceof BodyComponent){
                ((BodyComponent) component).body.setTransform(Util.jsonStringToVector2(jsonData.get("position").asString()),0);
                continue;
            }
            if(component instanceof AiComponent){
                if(jsonData.has("startPosition")){
                    ((AiComponent) component).startPosition = Util.jsonStringToVector2(jsonData.get("startPosition").asString());
                }
            }

        }
        engine.addEntity(entity);
        return null;
    }
}
