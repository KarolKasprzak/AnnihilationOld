package com.cosma.annihilation.Utils;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class GfxAssetDescriptors {
    public static final AssetDescriptor<Texture> box = new AssetDescriptor<Texture>("box.png", Texture.class);
    public static final AssetDescriptor<Texture> bulletTrace = new AssetDescriptor<Texture>("gfx/effect/bullet.png", Texture.class);
    public static final AssetDescriptor<Skin> skin = new AssetDescriptor<Skin>("interface/comadore/uiskin.json", Skin.class);
    public static final AssetDescriptor<TiledMap> tiledMap = new AssetDescriptor<TiledMap>("Map/2/map1.tmx", TiledMap.class);
    private GfxAssetDescriptors(){}
}
