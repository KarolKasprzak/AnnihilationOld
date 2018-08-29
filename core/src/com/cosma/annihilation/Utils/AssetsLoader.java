package com.cosma.annihilation.Utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
        files.put("box", new AssetsHelper("box.png", Texture.class));
        files.put("pl_1", new AssetsHelper("character/player_front1.png", Texture.class));
        files.put("pl_2", new AssetsHelper("character/player_front2.png", Texture.class));
        files.put("pl_3", new AssetsHelper("character/player_front3.png", Texture.class));
        files.put("pl_4", new AssetsHelper("character/player_front4.png", Texture.class));
        files.put("pl_5", new AssetsHelper("character/player_front5.png", Texture.class));
        files.put("hand_icon", new AssetsHelper("gfx/icon/handicon.png", Texture.class));
        files.put("mp44", new AssetsHelper("ui/icon/stg44.png", Texture.class));
        files.put("p38", new AssetsHelper("ui/icon/p38.png", Texture.class));
        files.put("stack_default", new AssetsHelper("UI/icon/stack_default.png", Texture.class));
        files.put("player_move", new AssetsHelper("gfx/player_move.atlas", TextureAtlas.class));
        files.put("player_move_start", new AssetsHelper("gfx/player.atlas", TextureAtlas.class));
        files.put("player_stand", new AssetsHelper("gfx/Player1.png", Texture.class));






        //Load
        for(AssetsHelper asset : files.values()){
            manager.load(asset.path,asset.type);
        }
        manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        manager.load("Map/2/map1.tmx", TiledMap.class);
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
