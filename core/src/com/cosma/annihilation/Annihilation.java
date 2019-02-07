package com.cosma.annihilation;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.cosma.annihilation.Screens.GameScreen;
import com.cosma.annihilation.Screens.MapEditor;
import com.cosma.annihilation.Screens.MenuScreen;
import com.cosma.annihilation.Utils.AssetLoader;
import com.cosma.annihilation.Utils.LoaderOLD;

public class Annihilation extends Game {

    private LoaderOLD loaderOLD;
    private AssetLoader assetLoader;
    private MenuScreen menuScreen;
    private GameScreen gameScreen;
    private MapEditor mapEditor;
    private Boolean isGameLoaded;

    public Annihilation() {
        super();
        loaderOLD = new LoaderOLD();
        assetLoader = new AssetLoader();
    }

    @Override
    public void create() {
        isGameLoaded = false;
        loaderOLD.load();
        assetLoader.load();
        menuScreen = new MenuScreen(this,assetLoader);
        this.setScreen(menuScreen);
    }

    @Override
    public void dispose() {
        loaderOLD.dispose();
        assetLoader.dispose();
        menuScreen.dispose();
        if(gameScreen != null){
            gameScreen.dispose();
        }
    }

    public void setGameScreen() {
        gameScreen = new GameScreen(this, assetLoader);
        this.setScreen(gameScreen);
    }

    public void setEditorScreen() {
        mapEditor = new MapEditor(this);
        this.setScreen(mapEditor);
    }

    public void setGameState(Boolean gameLoaded) {
        isGameLoaded = gameLoaded;
    }

    public Boolean isGameLoaded() {
        return isGameLoaded;
    }

    public static AssetManager getAssets() {
        return ((Annihilation) Gdx.app.getApplicationListener()).assetLoader.manager;
    }



}
