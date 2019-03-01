package com.cosma.annihilation.Editor.CosmaMap.CosmaEditorObject;

public class RectangleObject extends MapObject {
    public float x;
    public float y;
    public float width;
    public float height;
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
        return width;
    }

    public void setWidth(float w) {
        this.width = w;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float h) {
        this.height = h;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public RectangleObject() {
    }
    public RectangleObject(float x, float y, float width, float height, float rotation) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.rotation = rotation;
    }


}
