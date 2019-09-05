package com.cosma.annihilation.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Sound;
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
        FileHandle mapTextures = Gdx.files.local("map/map_tiles/");
        for(FileHandle texture: mapTextures.list(".atlas")){
            manager.load(texture.path(),TextureAtlas.class);
        }
        //Load map textures
        FileHandle mapSprites = Gdx.files.local("map/map_sprites/");
        for(FileHandle texture: mapSprites.list(".atlas")){
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
        //Load texture atlas
        FileHandle gfxAtlas = Gdx.files.local("gfx/atlas/");

        for(FileHandle file:  gfxAtlas.list()){
            if(file.isDirectory()){
                for(FileHandle texture: file.list(".atlas")){
                    manager.load(texture.path(),TextureAtlas.class);
                    System.out.println(texture.path());
                }
            }
        }
        for(FileHandle texture: gfxAtlas.list(".atlas")){
            manager.load(texture.path(),TextureAtlas.class);
        }

        //Load player textures
        FileHandle playerAtlas = Gdx.files.local("gfx/player/");
        for(FileHandle texture: playerAtlas.list(".atlas")){
            manager.load(texture.path(),TextureAtlas.class);
        }

        //Load sfx
        FileHandle sounds = Gdx.files.local("sfx/");
        for(FileHandle sound: sounds.list(".wav")){
            manager.load(sound.path(),Sound.class);
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
