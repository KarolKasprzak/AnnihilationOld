package com.cosma.annihilation.Utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class AssetLoader {
    public AssetManager manager;

    public AssetLoader() {
        manager = new AssetManager();

    }


    public void load() {
        manager.load(GfxAssetDescriptors.box);
        manager.load(SfxAssetDescriptors.pistolSound);
        manager.load(GfxAssetDescriptors.bulletTrace);

        manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        manager.load(GfxAssetDescriptors.tiledMap);
        manager.finishLoading();

        System.out.println("Loaded!");
    }

    public  void dispose() {
        manager.dispose();
    }
}
