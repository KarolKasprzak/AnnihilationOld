package com.cosma.annihilation.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Pool;


public class BodyComponent implements Component, Json.Serializable, Pool.Poolable {
    public Body body;
    public float sizeX = 0;
    public float sizeY = 0;
    private Vector2 position;
    private float angle;
    public boolean hasHitSomething = false;

    @Override
    public void write(Json json) {
        position = body.getPosition();
        angle = body.getAngle();
        json.writeValue("position",position);
        json.writeValue("angle",angle);


    }
    @Override
    public void read(Json json, JsonValue jsonData) {
        position = json.readValue("position",Vector2.class,jsonData);
        angle = jsonData.getFloat("angle");


    }

    @Override
    public void reset() {
        Body body;
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getAngle() {
        return angle;
    }
}

