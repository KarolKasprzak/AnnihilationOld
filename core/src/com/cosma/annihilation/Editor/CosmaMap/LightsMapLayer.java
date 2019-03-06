package com.cosma.annihilation.Editor.CosmaMap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.cosma.annihilation.Editor.CosmaMap.CosmaEditorLights.MapLight;
import com.cosma.annihilation.Editor.CosmaMap.CosmaEditorLights.MapLights;
import com.cosma.annihilation.Editor.CosmaMap.CosmaEditorLights.MapPointLight;
import com.cosma.annihilation.Editor.CosmaMap.CosmaEditorObject.MapObjects;
import com.cosma.annihilation.Editor.CosmaMap.CosmaEditorObject.RectangleObject;

public class LightsMapLayer extends MapLayer {
    private int width;
    private int height;
    private MapLights lights = new MapLights();

    public LightsMapLayer(int width, int height, String name) {
        super(width, height, name);
    }

    public LightsMapLayer() {
    }

    public MapLights geLights() {
        return lights;
    }

    public String createPointLight(float x, float y, Color color, int raysNumber, float maxDistance) {
        String name = "PointLight_" + (lights.getCount() + 1);
        MapPointLight light = new MapPointLight(x,y,color,raysNumber,maxDistance);
        light.setName(name);
        lights.add(light);
        return name;
    }
}
