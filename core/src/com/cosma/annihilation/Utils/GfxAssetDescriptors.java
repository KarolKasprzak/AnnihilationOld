package com.cosma.annihilation.Utils;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class GfxAssetDescriptors {
    public static final AssetDescriptor<Texture> box = new AssetDescriptor<Texture>("gfx/box.png", Texture.class);
    public static final AssetDescriptor<Texture> ammo = new AssetDescriptor<Texture>("gfx/icon/ammo.png", Texture.class);
    public static final AssetDescriptor<Texture> bulletShell = new AssetDescriptor<Texture>("gfx/effect/bullet.png", Texture.class);
    public static final AssetDescriptor<Texture> bulletTrace = new AssetDescriptor<Texture>("gfx/effect/bullet_trace.png", Texture.class);
    public static final AssetDescriptor<Skin> skin = new AssetDescriptor<Skin>("interface/retro_pc/uiskin.json", Skin.class);
    public static final AssetDescriptor<Texture> mp44 = new AssetDescriptor<Texture>("gfx/icon/stg.png", Texture.class);
    public static final AssetDescriptor<Texture> p38 = new AssetDescriptor<Texture>("gfx/icon/p38.png", Texture.class);
    public static final AssetDescriptor<Texture> defaultStack = new AssetDescriptor<Texture>("interface/icon/stack_default.png", Texture.class);
    public static final AssetDescriptor<Texture> enemy1 = new AssetDescriptor<Texture>("gfx/characters/enemy.png", Texture.class);
    public static final AssetDescriptor<BitmapFont> font = new AssetDescriptor<BitmapFont>("gfx/fonts/font1.fnt", BitmapFont.class);

    public static final AssetDescriptor<TiledMap> tiledMap = new AssetDescriptor<TiledMap>("Map/2/map1.tmx", TiledMap.class);
    private GfxAssetDescriptors(){

    }


}
