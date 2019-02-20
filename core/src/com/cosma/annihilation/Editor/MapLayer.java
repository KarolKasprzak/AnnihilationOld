package com.cosma.annihilation.Editor;

import com.badlogic.gdx.graphics.Color;

public class MapLayer {
    private int width;
    private int height;
    private String name = "";
    private boolean visible = true;;

    public MapLayer(int width, int height, String name) {
        this.width = width;
        this.height = height;
        this.name = name;
    }

    public MapLayer() {

    }

    public String getName() {
        return name;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isLayerVisible(){
        return visible;
    }

}
