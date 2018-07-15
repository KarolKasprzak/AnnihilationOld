package com.cosma.annihilation.Utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

import java.util.HashMap;

public class AssetsLoader {

    public  static final AssetManager manager = new AssetManager();
    private static HashMap<String, AssetsHelper> files;

    public void load(){

        files = new HashMap<String, AssetsHelper>();
        //Textures
        files.put("bullet", new AssetsHelper("gfx/bullet/bullet.png", Texture.class));
        files.put("ladder", new AssetsHelper("gfx/map_util/ladders_wood.png", Texture.class));
        files.put("ladder2", new AssetsHelper("tiles/ladder_mid.png", Texture.class));
        files.put("hero", new AssetsHelper("idi.png", Texture.class));




        //Load
        for(AssetsHelper asset : files.values()){
            manager.load(asset.path,asset.type);
        }
        manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        manager.load("Map/1/bunker.tmx", TiledMap.class);
        manager.finishLoading();
        System.out.println("assets load");
    }
    public static Object getResource(String mapKey){
        return manager.get(files.get(mapKey).path,files.get(mapKey).type);
    }




    public  void dispose(){
        manager.dispose();
    }
}
