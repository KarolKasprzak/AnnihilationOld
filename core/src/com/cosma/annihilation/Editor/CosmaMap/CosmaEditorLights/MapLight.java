package com.cosma.annihilation.Editor.CosmaMap.CosmaEditorLights;

import com.badlogic.gdx.graphics.Color;

public abstract class MapLight {
    private String name;
    private boolean visible = true;
    private boolean isHighlighted = false;
    private int raysNumber;
    private Color color;
    private float x;

    public MapLight() {
    }
    public MapLight(float x, float y, Color color,int raysNumber) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.raysNumber = raysNumber;
    }

    public int getRaysNumber() {
        return raysNumber;
    }

    public void setRaysNumber(int raysNumber) {
        this.raysNumber = raysNumber;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

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

    private float y;


    public boolean isHighlighted() {
        return isHighlighted;
    }

    public void setHighlighted(boolean highlighted) {
        isHighlighted = highlighted;
    }

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public boolean isVisible () {
        return visible;
    }

    public void setVisible (boolean visible) {
        this.visible = visible;
    }
}
