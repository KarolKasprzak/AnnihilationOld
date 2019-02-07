package com.cosma.annihilation.Editor;

import java.util.ArrayList;

public class Map {

    private int width;
    private int height;
    private int tileSize;
    private ArrayList<MapLayer> mapLayers ;


    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getTileSize() {
        return tileSize;
    }

    public Map(int width, int height, int tileSize) {
        this.width = width;
        this.height = height;
        this.tileSize = tileSize;
        mapLayers = new ArrayList<MapLayer>();
    }


    public void addMapLayer(){
        MapLayer mapLayer = new MapLayer(width,height);
        mapLayers.add(mapLayer);
    }


    public ArrayList<MapLayer> getMapLayers() {
        return mapLayers;
    }
}
