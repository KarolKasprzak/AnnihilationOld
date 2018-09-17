package com.cosma.annihilation.Utils;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.cosma.annihilation.Components.ActionComponent;
import com.cosma.annihilation.Components.ContainerComponent;
import com.cosma.annihilation.Components.HealthComponent;
import com.cosma.annihilation.Components.PlayerComponent;
import com.cosma.annihilation.Entities.PlayerEntity;
import com.cosma.annihilation.Gui.InventoryItemLocation;

public class Serializer{

    private Engine engine;



    public Serializer(Engine engine){
        this.engine = engine;


    }

    public void save(){
        Json json = new Json();
        json.setUsePrototypes(false);
        System.out.println(json.prettyPrint(engine.getEntitiesFor(Family.all(PlayerComponent.class).get()).first().getComponent(HealthComponent.class).hp));
        FileHandle file = Gdx.files.local("save/save.json");
        file.writeString(json.prettyPrint(engine.getEntitiesFor(Family.all(PlayerComponent.class).get()).first().getComponent(HealthComponent.class)),false);
//        file.writeString(json.prettyPrint(getInventory(equipmentSlotsTable)),false);
//        file1.writeString(json.prettyPrint(getInventory(inventorySlotsTable)),false);
    }

    public void load(){
        Json json = new Json();
        engine.getEntitiesFor(Family.all(PlayerComponent.class).get()).first().add(json.fromJson(HealthComponent.class, Gdx.files.internal("save/save.json")));
        System.out.println(json.prettyPrint(engine.getEntitiesFor(Family.all(PlayerComponent.class).get()).first().getComponent(HealthComponent.class).hp));
    }



    public void removeComponents(Entity entity){
        entity.remove(ContainerComponent.class);
        entity.remove(ActionComponent.class);
        entity.remove(HealthComponent.class);
    }





}
