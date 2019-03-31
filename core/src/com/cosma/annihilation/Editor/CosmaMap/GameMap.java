package com.cosma.annihilation.Editor.CosmaMap;

import box2dLight.Light;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.OrderedMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class GameMap{

    private int width;
    private int height;
    private int tileSize;
    private MapLayers layers = new MapLayers();
    private ArrayList<Entity> entityList;
    transient private OrderedMap<String, Light> lightMap;

    public Light getLight(String name) {
        return lightMap.get(name);
    }

    public void putLight(String name,Light light) {
        lightMap.put(name,light);
    }

    public GameMap(int width, int height, int tileSize) {
        this.width = width;
        this.height = height;
        this.tileSize = tileSize;
        lightMap = new OrderedMap<>();
        entityList = new ArrayList<>();
    }

    public GameMap() {
        lightMap = new OrderedMap<>();
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

    public void addEntity(Entity entity){
        entityList.add(entity);
    }

    public void removeEntity(Entity entity){
        entityList.remove(entity);
    }

    public ArrayList<Entity> getAllEntity(){
        return entityList;
    }

    public void createTileMapLayer(){
        String name = "Tiles_layer" +(layers.getCount()+1);
        TileMapLayer mapLayer = new TileMapLayer(width,height,name);
        layers.add(mapLayer);
    }

    public void createObjectMapLayer(){
        String name = "Objects_layer" +(layers.getCount()+1);
        ObjectMapLayer mapLayer = new ObjectMapLayer(width,height,name);
        layers.add(mapLayer);
    }

    public void createLightsLayer(){
        String name = "Lights_layer" +(layers.getCount()+1);
        LightsMapLayer mapLayer = new LightsMapLayer(width,height,name);
        layers.add(mapLayer);
    }
}
