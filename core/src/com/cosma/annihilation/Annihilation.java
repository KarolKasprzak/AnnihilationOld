package com.cosma.annihilation;

import com.badlogic.gdx.Game;
import com.cosma.annihilation.Screens.GameScreen;
import com.cosma.annihilation.Screens.MenuScreen;
import com.cosma.annihilation.Utils.AssetLoader;
import com.cosma.annihilation.Utils.AssetsLoader;

public class Annihilation extends Game {

    private AssetsLoader assetsLoader;
    private AssetLoader assetLoader;
    private MenuScreen menuScreen;
    private GameScreen gameScreen;
    private Boolean isGameLoaded;

    public Annihilation() {
        super();
        assetsLoader = new AssetsLoader();
        assetLoader = new AssetLoader();
    }

    @Override
    public void create() {
        isGameLoaded = false;
        assetsLoader.load();
        menuScreen = new MenuScreen(this);
        this.setScreen(menuScreen);
    }

    @Override
    public void dispose() {
        assetsLoader.dispose();
        menuScreen.dispose();
        if(gameScreen != null){
            gameScreen.dispose();
        }
    }

    public void setGameScreen() {
        gameScreen = new GameScreen(this);
        this.setScreen(gameScreen);
    }

    public void setGameState(Boolean gameLoaded) {
        isGameLoaded = gameLoaded;
    }

    public Boolean isGameLoaded() {
        return isGameLoaded;
    }


}
