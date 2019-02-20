package com.cosma.annihilation.Editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

public class CosmaMapLoader {
    private GameMap map;
    public CosmaMapLoader(String mapPath){
        FileHandle mapFile = Gdx.files.local(mapPath);
        Json json = new Json();
        json.setUsePrototypes(false);
        map = json.fromJson(GameMap.class,mapFile);
    }
    public GameMap getMap(){
        return map;
    }
}
