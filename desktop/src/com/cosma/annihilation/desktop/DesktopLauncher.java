package com.cosma.annihilation.desktop;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglFrame;

import com.cosma.annihilation.Annihilation;

import javax.swing.*;
import java.awt.*;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = "Annihilation";
        Dimension dimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		config.height = (int) dimension.getHeight();
		config.width = (int) dimension.getWidth();
        config.resizable = false;
			config.width = 1024;
	        config.height = 768;
		LwjglFrame frame = new LwjglFrame(new Annihilation(),config);
//		frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);

	}
}

//public class DesktopLauncher {
//	public static void main (String[] arg) {
//		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
//
////		config.setMaximized(true);
//		config.setWindowIcon("icon/editor.png");
//		config.setTitle("Annihilation");
//		config.setWindowedMode(1024,768);
//		Lwjgl3Application frame =  new Lwjgl3Application(new Annihilation(),config);
//	}
//}
