package com.cosma.annihilation;

import com.badlogic.gdx.Game;
import com.cosma.annihilation.Screens.GameScreen;
import com.cosma.annihilation.Screens.MenuScreen;
import com.cosma.annihilation.Utils.AssetLoader;
import com.cosma.annihilation.Utils.LoaderOLD;

public class Annihilation extends Game {

    private LoaderOLD loaderOLD;
    private AssetLoader assetLoader;
    private MenuScreen menuScreen;
    private GameScreen gameScreen;
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

    public void setGameState(Boolean gameLoaded) {
        isGameLoaded = gameLoaded;
    }

    public Boolean isGameLoaded() {
        return isGameLoaded;
    }


}
