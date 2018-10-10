package com.cosma.annihilation;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.cosma.annihilation.Screens.GameScreen;
import com.cosma.annihilation.Utils.AssetDescriptors;
import com.cosma.annihilation.Utils.AssetLoader;
import com.cosma.annihilation.Utils.AssetsLoader;

public class Annihilation extends Game {

    private AssetsLoader assetsLoader;
    private AssetLoader assetLoader;

	public Annihilation(){
		super();
		assetsLoader = new AssetsLoader();
		assetLoader = new AssetLoader();
	}
	@Override
	public void create () {
		assetsLoader.load();
		assetLoader.load();




		this.setScreen(new GameScreen());
	}


	
	@Override
	public void dispose () {
		assetsLoader.dispose();
	}
}
