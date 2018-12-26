package com.cosma.annihilation.Utils;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class AssetLoader {
    public AssetManager manager;

    public AssetLoader() {
        manager = new AssetManager();
    }


    public void load() {

        loadMap();
        loadFonts();

        manager.load(GfxAssetDescriptors.box);
        manager.load(SfxAssetDescriptors.pistolSound);
        manager.load(GfxAssetDescriptors.bulletShell);
        manager.load(GfxAssetDescriptors.bulletTrace);
        manager.load(GfxPlayerAssetDescriptors.player_stand_pistol);
        manager.load(GfxAssetDescriptors.skin);
        manager.load(GfxAssetDescriptors.ammo);
        manager.load(GfxAssetDescriptors.mp44);
        manager.load(GfxAssetDescriptors.p38);
        manager.load(GfxAssetDescriptors.defaultStack);
        manager.load(GfxPlayerAssetDescriptors.player_stand);
        manager.load(GfxPlayerAssetDescriptors.player_stand_rifle);
        manager.load(GfxAssetDescriptors.enemy1);
        manager.load(GfxAssetDescriptors.guiframe);
        manager.load(GfxAssetDescriptors.guiframe64x64);
        manager.load(GfxAssetDescriptors.tabletGui);
        manager.load(GfxAssetDescriptors.gui_button);
        manager.load(GfxAssetDescriptors.gui_button_down);
        manager.load(GfxAssetDescriptors.gui_button1);
        manager.load(GfxAssetDescriptors.clearColor);
        manager.load(GfxAssetDescriptors.gui_weapon_slot);
        manager.load(GfxAssetDescriptors.gui_armour_slot);
        manager.load(GfxAssetDescriptors.gui_human_animation);
        manager.load(GfxAssetDescriptors.door);
        manager.load(GfxAssetDescriptors.door_open);

        manager.finishLoading();





        System.out.println("Loaded!");
    }

    private void loadFonts(){
        manager.setLoader(BitmapFont.class, new BitmapFontLoader(new InternalFileHandleResolver()));
        manager.load(GfxAssetDescriptors.font);
    }

    private void loadMap(){
        manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        manager.load(GfxAssetDescriptors.tiledMap);
    }

    public void dispose() {
        manager.dispose();
    }
}
