package com.cosma.annihilation.Utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.HashMap;

public class LoaderOLD {

    public  static final AssetManager manager = new AssetManager();
    private static HashMap<String, AssetsHelper> files;

    public void load(){

        files = new HashMap<String, AssetsHelper>();
        //Textures
        files.put("ladder", new AssetsHelper("gfx/map_util/ladders_wood.png", Texture.class));
        files.put("ladder2", new AssetsHelper("tiles/ladder_mid.png", Texture.class));
        files.put("box", new AssetsHelper("gfx/box.png", Texture.class));
        files.put("pl_1", new AssetsHelper("character/player_front1.png", Texture.class));
        files.put("pl_2", new AssetsHelper("character/player_front2.png", Texture.class));
        files.put("pl_3", new AssetsHelper("character/player_front3.png", Texture.class));
        files.put("pl_4", new AssetsHelper("character/player_front4.png", Texture.class));
        files.put("pl_5", new AssetsHelper("character/player_front5.png", Texture.class));
        files.put("mp44", new AssetsHelper("interface/icon/stg.png", Texture.class));
        files.put("p38", new AssetsHelper("interface/icon/p38.png", Texture.class));
        files.put("stack_default", new AssetsHelper("interface/icon/stack_default.png", Texture.class));
        files.put("player_move", new AssetsHelper("gfx/player_move.atlas", TextureAtlas.class));
        files.put("player_move_start", new AssetsHelper("gfx/player.atlas", TextureAtlas.class));
        files.put("player_stand", new AssetsHelper("gfx/Player1.png", Texture.class));
        files.put("skin", new AssetsHelper("interface/comadore/uiskin.json", Skin.class));
        files.put("player_weapon", new AssetsHelper("gfx/player_weapons_stand.png", Texture.class));





        //Load
        for(AssetsHelper asset : files.values()){
            manager.load(asset.path,asset.type);
        }

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
