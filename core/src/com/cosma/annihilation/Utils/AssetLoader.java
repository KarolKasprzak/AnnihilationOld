package com.cosma.annihilation.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;

public class AssetLoader {
    public AssetManager manager;

    public AssetLoader() {
        manager = new AssetManager();
    }


    public void load() {

        loadFonts();

        //Load map textures
        FileHandle mapTextures = Gdx.files.local("map/map_textures/");
        for(FileHandle texture: mapTextures.list(".atlas")){
            manager.load(texture.path(),TextureAtlas.class);
        }
        //Load icon
        FileHandle iconTextures = Gdx.files.local("gfx/textures/icon/");
        for(FileHandle texture: iconTextures.list(".png")){
            manager.load(texture.path(),Texture.class);
        }
        //Load interface textures
        FileHandle interfaceTextures = Gdx.files.local("gfx/interface/");
        for(FileHandle texture: interfaceTextures.list(".png")){
            manager.load(texture.path(),Texture.class);
        }
        //Load textures
        FileHandle textures = Gdx.files.local("gfx/textures/");
        for(FileHandle texture: textures.list(".png")){
            manager.load(texture.path(),Texture.class);
        }
        //Load atlas
        FileHandle gfxAtlas = Gdx.files.local("gfx/atlas/");
        for(FileHandle texture: gfxAtlas.list(".atlas")){
            manager.load(texture.path(),TextureAtlas.class);
        }
        //Load player textures
        FileHandle playerAtlas = Gdx.files.local("gfx/player/");
        for(FileHandle texture: playerAtlas.list(".atlas")){
            manager.load(texture.path(),TextureAtlas.class);
        }
        for(FileHandle texture: playerAtlas.list(".png")){
            manager.load(texture.path(),Texture.class);
        }
        //Load locale files
        FileHandle locale = Gdx.files.internal("locale");
        for(FileHandle local: locale.list()){
            manager.load(local.pathWithoutExtension(),I18NBundle.class);
        }
        //Load skin files
        manager.load("gfx/interface/uiskin.json", Skin.class);


//        manager.load("gfx/player_move.atlas",TextureAtlas.class);
//        manager.load("gfx/player/player_noweapon_stand.png",Texture.class);
//        manager.load("gfx/player/player_jump.png",Texture.class);
//        manager.load(GfxAssetDescriptors.box);
//        manager.load(SfxAssetDescriptors.pistolSound);
//        manager.load(GfxAssetDescriptors.bulletShell);
//        manager.load(GfxAssetDescriptors.bulletTrace);
//        manager.load(GfxPlayerAssetDescriptors.player_stand_pistol);
//        manager.load(GfxAssetDescriptors.skin);
//        manager.load(GfxAssetDescriptors.ammo);
//        manager.load(GfxAssetDescriptors.mp44);
//        manager.load(GfxAssetDescriptors.p38);
//        manager.load(GfxAssetDescriptors.defaultStack);
//        manager.load(GfxPlayerAssetDescriptors.player_stand);
//        manager.load(GfxPlayerAssetDescriptors.player_stand_rifle);
//        manager.load(GfxAssetDescriptors.enemy1);
//        manager.load(GfxAssetDescriptors.guiframe);
//        manager.load(GfxAssetDescriptors.guiframe64x64);
//        manager.load(GfxAssetDescriptors.tabletGui);
//        manager.load(GfxAssetDescriptors.gui_button);
//        manager.load(GfxAssetDescriptors.gui_button_down);
//        manager.load(GfxAssetDescriptors.gui_button1);
//        manager.load(GfxAssetDescriptors.clearColor);
//        manager.load(GfxAssetDescriptors.gui_weapon_slot);
//        manager.load(GfxAssetDescriptors.gui_armour_slot);
//        manager.load(GfxAssetDescriptors.gui_human_animation);
//        manager.load(GfxAssetDescriptors.door);
//        manager.load(GfxAssetDescriptors.door_open);
//        manager.load(GfxAssetDescriptors.gui_buttons);
//        manager.load(GfxAssetDescriptors.fireaxe);
//        manager.load(GfxPlayerAssetDescriptors.player_stand_melee);
//        manager.load(GfxAssetDescriptors.player_attack_melee);
//        manager.load(GfxAssetDescriptors.map_conc);
//        manager.load(GfxAssetDescriptors.editor_icons);

        manager.finishLoading();

        System.out.println("Loaded!");
    }

    private void loadFonts(){
        manager.setLoader(BitmapFont.class, new BitmapFontLoader(new InternalFileHandleResolver()));
    }

    public void dispose() {
        manager.dispose();
    }
}
