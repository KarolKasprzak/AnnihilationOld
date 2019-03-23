package com.cosma.annihilation.Editor.CosmaMap;

import box2dLight.ConeLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.cosma.annihilation.Editor.CosmaMap.CosmaEditorLights.MapConeLight;
import com.cosma.annihilation.Editor.CosmaMap.CosmaEditorLights.MapPointLight;
import com.cosma.annihilation.Editor.CosmaMap.CosmaEditorObject.RectangleObject;
import com.cosma.annihilation.Utils.GameEntity;
import com.cosma.annihilation.Utils.Utilities;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import javax.swing.text.html.parser.Entity;

public class CosmaMapLoader {
    private GameMap map;


    public CosmaMapLoader(String mapPath) {
        loadMap(mapPath);
    }

    public CosmaMapLoader(String mapPath, World world, RayHandler rayHandler) {
        loadMap(mapPath);
        loadEntity();
        for(ObjectMapLayer layer: map.getLayers().getByType(ObjectMapLayer.class)){
            for(RectangleObject object: layer.getObjects().getByType(RectangleObject.class)){
                Utilities.createBox2dObject(world,object.getX(),object.getY(),object.getWidth(),object.getHeight(),object.getBodyType(),object.getName());
            }
        }

        for(LightsMapLayer layer: map.getLayers().getByType(LightsMapLayer.class)){
            for(MapPointLight light: layer.getLights().getByType(MapPointLight.class)){
                PointLight point = new PointLight(rayHandler,light.getRaysNumber(),light.getColor(),light.getLightDistance(),light.getX(),light.getY());
                point.setStaticLight(light.isStaticLight());
                point.setSoft(light.isSoftLight());
                point.setSoftnessLength(light.getSoftLength());
                map.putLight(light.getName(),point);
            }
            for(MapConeLight light: layer.getLights().getByType(MapConeLight.class)){
                ConeLight cone = new ConeLight(rayHandler,light.getRaysNumber(),light.getColor(),light.getLightDistance(),light.getX(),light.getY(),light.getDirection(),light.getConeDegree());
                cone.setStaticLight(light.isStaticLight());
                cone.setSoft(light.isSoftLight());
                cone.setSoftnessLength(light.getSoftLength());
                map.putLight(light.getName(),cone);
            }
        }

    }


    private void loadMap(String mapPath) {
        FileHandle mapFile = Gdx.files.local(mapPath);
        Json json = new Json();
        json.setUsePrototypes(false);
        map = json.fromJson(GameMap.class, mapFile);
    }
    private void loadEntity(){
        FileHandle file = Gdx.files.local("entity/box.json");
        Json json = new Json();
        json.setSerializer(GameEntity.class, new Json.Serializer<GameEntity>() {
            @Override
            public void write(Json json, GameEntity object, Class knownType) {

            }

            @Override
            public GameEntity read(Json json, JsonValue jsonData, Class type) {
                GameEntity entity = new GameEntity(jsonData.getString("name"));
                return entity;
            }
        });
        GameEntity entity = json.fromJson(GameEntity.class,file);
        System.out.println(entity.getName());
    }

    private void loadEntity2(){
        Json json = new Json();
        GameEntity entity = new GameEntity("asf");
        System.out.println(json.prettyPrint(entity));
    }

    public GameMap getMap() {
        return map;
    }
}
