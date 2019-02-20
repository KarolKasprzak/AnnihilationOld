package com.cosma.annihilation.Editor;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

public class GameMap {

    private int width;
    private int height;
    private int tileSize;
    private MapLayers layers = new MapLayers();

    public GameMap(int width, int height, int tileSize) {
        this.width = width;
        this.height = height;
        this.tileSize = tileSize;

    }

    public GameMap() {

    }


    public MapLayers getLayers () {
        return layers;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getTileSize() {
        return tileSize;
    }

    public void createTileMapLayer(){
        String name = "Layer" +(layers.getCount()+1);
        TileMapLayer mapLayer = new TileMapLayer(width,height,name);
        layers.add(mapLayer);
    }

    public void createObjectMapLayer(){
        String name = "Object_layer" +(layers.getCount()+1);
        ObjectMapLayer mapLayer = new ObjectMapLayer(width,height,name);
        layers.add(mapLayer);
    }

    public String createMapLayerAndReturnName(){
        String name = "Layer" +(layers.getCount()+1);
        MapLayer mapLayer = new MapLayer(width,height,name);
        layers.add(mapLayer);
        return name;
    }
}
