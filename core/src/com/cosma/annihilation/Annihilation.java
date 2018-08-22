package com.cosma.annihilation;

import com.badlogic.gdx.Game;
import com.cosma.annihilation.Screens.GameScreen;
import com.cosma.annihilation.Utils.AssetsLoader;

public class Annihilation extends Game {

    private AssetsLoader assetsLoader;

	public Annihilation(){
		super();
		assetsLoader = new AssetsLoader();
	}
	@Override
	public void create () {
		assetsLoader.load();
		this.setScreen(new GameScreen());
	}


	
	@Override
	public void dispose () {
		assetsLoader.dispose();
	}
}
