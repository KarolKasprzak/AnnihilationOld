package com.cosma.annihilation.Utils;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class SfxAssetDescriptors {
    public static final AssetDescriptor<Sound> pistolSound = new AssetDescriptor<Sound>("sfx/weapons/pistol.wav", Sound.class);
    private SfxAssetDescriptors(){}
}
