package com.cosma.annihilation.Editor.CosmaMap.CosmaEditorObject;

public class RectangleObject extends MapObject {
    public float x;
    public float y;
    public float w;
    public float h;
    public float rotation;

    public RectangleObject() {
    }
    public RectangleObject(float x,float y, float w,float h, float rotation) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.rotation = rotation;
    }


}
