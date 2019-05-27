package com.cosma.annihilation.Editor.CosmaMap;

import box2dLight.ConeLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Json;
import com.cosma.annihilation.Editor.CosmaMap.CosmaEditorLights.MapConeLight;
import com.cosma.annihilation.Editor.CosmaMap.CosmaEditorLights.MapPointLight;
import com.cosma.annihilation.Editor.CosmaMap.CosmaEditorObject.RectangleObject;
import com.cosma.annihilation.Utils.Serialization.EntitySerializer;
import com.cosma.annihilation.Utils.Serialization.GameEntitySerializer;
import com.cosma.annihilation.Utils.Util;


public class CosmaMapLoader {
    private GameMap map;
    private World world;
    private Engine engine;
    private RayHandler rayHandler;

//    public CosmaMapLoader(String mapPath) {
//        loadMap(mapPath);
//    }

    public CosmaMapLoader(World world, RayHandler rayHandler, Engine engine) {
        this.world = world;
        this.engine = engine;
        this.rayHandler = rayHandler;
    }

    public void loadMap(String mapPath) {
        FileHandle mapFile = Gdx.files.local(mapPath);
        Json json = new Json();
        json.setUsePrototypes(false);
        json.setSerializer(Entity.class,new GameEntitySerializer(world,engine));
        map = json.fromJson(GameMap.class, mapFile);
        createMapObject();
    }

    private void createMapObject(){
        for (ObjectMapLayer layer : map.getLayers().getByType(ObjectMapLayer.class)) {
            for (RectangleObject object : layer.getObjects().getByType(RectangleObject.class)) {
                Util.createBox2dObject(world, object.getX(), object.getY(), object.getWidth(), object.getHeight(), object.getBodyType(), object.getName(), object.getRotation() * MathUtils.degreesToRadians);
            }
        }

        for (LightsMapLayer layer : map.getLayers().getByType(LightsMapLayer.class)) {
            for (MapPointLight light : layer.getLights().getByType(MapPointLight.class)) {
                PointLight point = new PointLight(rayHandler, light.getRaysNumber(), light.getColor(), light.getLightDistance(), light.getX(), light.getY());
                point.setStaticLight(light.isStaticLight());
                point.setSoft(light.isSoftLight());
                point.setSoftnessLength(light.getSoftLength());
                Filter filter = new Filter();
                filter.maskBits = light.getMaskBit();
                filter.categoryBits = light.getCategoryBit();
                point.setContactFilter(filter);
                map.putLight(light.getName(), point);
            }
            for (MapConeLight light : layer.getLights().getByType(MapConeLight.class)) {
                ConeLight cone = new ConeLight(rayHandler, light.getRaysNumber(), light.getColor(), light.getLightDistance(), light.getX(), light.getY(), light.getDirection(), light.getConeDegree());
                cone.setStaticLight(light.isStaticLight());
                cone.setSoft(light.isSoftLight());
                cone.setSoftnessLength(light.getSoftLength());
                Filter filter = new Filter();
                filter.maskBits = light.getMaskBit();
                filter.categoryBits = light.getCategoryBit();
                cone.setContactFilter(filter);
                map.putLight(light.getName(), cone);
            }
        }

    }



    public GameMap getMap() {
        return map;
    }
}
