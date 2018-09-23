package com.cosma.annihilation.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

public class PlayerComponent implements Component,Json.Serializable {
    public float velocity = 2;
    public int numFootContacts = 0;
    public boolean hidde = false;
    public Entity collisionEntity;

    @Override
    public void write(Json json) {
//        json.writeValue(velocity,Float.class);
        json.writeValue("velocity",velocity);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
       velocity = jsonData.get("velocity").asFloat();
    }
}
