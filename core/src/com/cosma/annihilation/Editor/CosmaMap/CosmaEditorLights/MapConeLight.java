package com.cosma.annihilation.Editor.CosmaMap.CosmaEditorLights;

import com.badlogic.gdx.graphics.Color;

public class MapConeLight extends MapLight {
    private float direction;
    private float coneDegree;


    public MapConeLight() {
    }

    public MapConeLight(float x, float y, Color color, int raysNumber, float maxDistance,float direction,float coneDegree) {
    super(x,y,color,raysNumber,maxDistance);
    this.direction = direction;
    this.coneDegree = coneDegree;
    }
}
