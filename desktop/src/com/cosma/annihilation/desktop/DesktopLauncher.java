package com.cosma.annihilation.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.cosma.annihilation.Annihilation;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Annihilation";
		config.width = 1280;
		config.height = 720;


//			config.width = 1920;
//	        config.height = 1200;
//
//		config.fullscreen = true;

		new LwjglApplication(new Annihilation(), config);
	}
}
