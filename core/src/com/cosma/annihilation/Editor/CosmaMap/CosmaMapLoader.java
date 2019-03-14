package com.cosma.annihilation.Editor.CosmaMap;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Json;
import com.cosma.annihilation.Editor.CosmaMap.CosmaEditorLights.MapPointLight;
import com.cosma.annihilation.Editor.CosmaMap.CosmaEditorObject.RectangleObject;
import com.cosma.annihilation.Utils.Utilities;

public class CosmaMapLoader {
    private GameMap map;

    public CosmaMapLoader(String mapPath) {
        loadMap(mapPath);
    }

    public CosmaMapLoader(String mapPath, World world, RayHandler rayHandler) {


        loadMap(mapPath);
        for(ObjectMapLayer layer: map.getLayers().getByType(ObjectMapLayer.class)){
            for(RectangleObject object: layer.getObjects().getByType(RectangleObject.class)){
                Utilities.createBox2dObject(world,object.getX(),object.getY(),object.getWidth(),object.getHeight(),object.getBodyType(),object.getName());
            }
        }

        for(LightsMapLayer layer: map.getLayers().getByType(LightsMapLayer.class)){
            for(MapPointLight mapLight: layer.getLights().getByType(MapPointLight.class)){
                PointLight light = new PointLight(rayHandler,mapLight.getRaysNumber(),mapLight.getColor(),mapLight.getLightDistance(),mapLight.getX(),mapLight.getY());
                light.setStaticLight(true);
            }
        }

    }


    private void loadMap(String mapPath) {
        FileHandle mapFile = Gdx.files.local(mapPath);
        Json json = new Json();
        json.setUsePrototypes(false);
        map = json.fromJson(GameMap.class, mapFile);
    }

    public GameMap getMap() {
        return map;
    }
}
