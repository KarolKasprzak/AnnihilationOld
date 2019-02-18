package com.cosma.annihilation.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.cosma.annihilation.Annihilation;

import java.awt.*;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Annihilation";
        Dimension dimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();

//		config.height = (int) dimension.getHeight();
//		config.width = (int) dimension.getWidth();


			config.width = 1024;
	        config.height = 768;
//
//		config.fullscreen = true;


        LwjglApplication game = new LwjglApplication(new Annihilation(), config);
	}
}
