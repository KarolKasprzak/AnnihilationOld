package com.cosma.annihilation.Utils;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class GfxPlayerAssetDescriptors {
    public static final AssetDescriptor<Texture> player_stand_pistol = new AssetDescriptor<Texture>("gfx/player/player_pistol_takeout.png", Texture.class);
    public static final AssetDescriptor<Texture> player_stand_melee = new AssetDescriptor<Texture>("gfx/player/player_melee.png", Texture.class);
    public static final AssetDescriptor<Texture> player_stand_rifle = new AssetDescriptor<Texture>("gfx/player/player_rifle_takeout.png", Texture.class);
    public static final AssetDescriptor<Texture> player_stand = new AssetDescriptor<Texture>("gfx/Player1.png", Texture.class);


    private GfxPlayerAssetDescriptors(){}
}
