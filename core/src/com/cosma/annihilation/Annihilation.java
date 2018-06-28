package com.cosma.annihilation;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.cosma.annihilation.Screens.TestScreen;
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
		this.setScreen(new TestScreen());
	}


	
	@Override
	public void dispose () {
		assetsLoader.dispose();
	}
}
