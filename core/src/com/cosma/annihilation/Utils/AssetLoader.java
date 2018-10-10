package com.cosma.annihilation.Utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class AssetLoader {
    private static  AssetManager manager;
    Texture mainTexture;
    public AssetLoader(){
        manager = new AssetManager();


    }


    public void load(){
        manager.load(AssetDescriptors.box);
        manager.finishLoading();
    }
}
