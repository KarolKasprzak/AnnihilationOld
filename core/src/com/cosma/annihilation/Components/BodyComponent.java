package com.cosma.annihilation.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Pool;


public class BodyComponent implements Component, Json.Serializable, Pool.Poolable {
    public Body body;
    public float SizeX = 0;
    public float SizeY = 0;

    @Override
    public void write(Json json) {
    }
    @Override
    public void read(Json json, JsonValue jsonData) {
    }

    @Override
    public void reset() {
        Body body;
    }
}
