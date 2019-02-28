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
//			config.width = 1024;
//	        config.height = 768;
		LwjglFrame frame = new LwjglFrame(new Annihilation(),config);
		frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		Cursor cursor = new Cursor(Cursor.HAND_CURSOR);
		frame.setCursor(cursor);
//        LwjglApplication game = new LwjglApplication(new Annihilation(), config);
	}
}
