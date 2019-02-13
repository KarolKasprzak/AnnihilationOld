package com.cosma.annihilation.Utils;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
    public static final AssetDescriptor<Texture> fireaxe = new AssetDescriptor<Texture>("gfx/icon/fireaxe.png", Texture.class);
    public static final AssetDescriptor<Texture> defaultStack = new AssetDescriptor<Texture>("interface/icon/stack_default.png", Texture.class);
    public static final AssetDescriptor<Texture> enemy1 = new AssetDescriptor<Texture>("gfx/characters/enemy.png", Texture.class);
    public static final AssetDescriptor<Texture> door = new AssetDescriptor<Texture>("gfx/object/door.png", Texture.class);
    public static final AssetDescriptor<Texture> door_open = new AssetDescriptor<Texture>("gfx/object/door_open1.png", Texture.class);

    public static final AssetDescriptor<Texture> guiframe = new AssetDescriptor<Texture>("gfx/gui/gui_frame.png", Texture.class);
    public static final AssetDescriptor<Texture> tabletGui = new AssetDescriptor<Texture>("gfx/gui/tablet.png", Texture.class);
    public static final AssetDescriptor<Texture> clearColor = new AssetDescriptor<Texture>("gfx/gui/clear.png", Texture.class);
    public static final AssetDescriptor<Texture> gui_button = new AssetDescriptor<Texture>("gfx/gui/table_button.png", Texture.class);
    public static final AssetDescriptor<Texture> gui_button1 = new AssetDescriptor<Texture>("gfx/gui/table_button1.png", Texture.class);
    public static final AssetDescriptor<Texture> gui_button_down = new AssetDescriptor<Texture>("gfx/gui/table_button_pressed.png", Texture.class);
    public static final AssetDescriptor<Texture> gui_weapon_slot = new AssetDescriptor<Texture>("gfx/gui/gui_weapon_slot.png", Texture.class);
    public static final AssetDescriptor<Texture> gui_armour_slot = new AssetDescriptor<Texture>("gfx/gui/gui_armour_slot.png", Texture.class);
    public static final AssetDescriptor<Texture> map_conc = new AssetDescriptor<Texture>("gfx/map/conc.png", Texture.class);


    public static final AssetDescriptor<TextureAtlas> gui_human_animation = new AssetDescriptor<TextureAtlas>("gfx/gui/human/gui_human.txt", TextureAtlas.class);
    public static final AssetDescriptor<TextureAtlas> gui_buttons = new AssetDescriptor<TextureAtlas>("gfx/gui/buttons_gui.txt", TextureAtlas.class);
    public static final AssetDescriptor<TextureAtlas> player_attack_melee = new AssetDescriptor<TextureAtlas>("gfx/player/player_melee.atlas", TextureAtlas.class);

    public static final AssetDescriptor<Texture> guiframe64x64 = new AssetDescriptor<Texture>("gfx/gui/gui_frame_64x64.png", Texture.class);

    public static final AssetDescriptor<BitmapFont> font = new AssetDescriptor<BitmapFont>("gfx/fonts/font1.fnt", BitmapFont.class);

    public static final AssetDescriptor<TiledMap> tiledMap = new AssetDescriptor<TiledMap>("Map/map1.tmx", TiledMap.class);

    private GfxAssetDescriptors(){

    }


}
