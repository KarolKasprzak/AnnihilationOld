package com.cosma.annihilation.Editor.CosmaMap.CosmaEditorObject;

public class RectangleObject extends MapObject {
    public float x;
    public float y;
    public float w;
    public float h;
    public float rotation;

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return w;
    }

    public void setWidth(float w) {
        this.w = w;
    }

    public float getHeight() {
        return h;
    }

    public void setHeight(float h) {
        this.h = h;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

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
