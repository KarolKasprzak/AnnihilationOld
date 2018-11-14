package com.cosma.annihilation.Gui;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.cosma.annihilation.Utils.Serialization.Serializer;

class OptionsMenuWindow extends Window {
    private MenuWindow menuWindow;
    private TextButton saveButton;
    private TextButton loadButton;
    private TextButton exitButton;
    private TextButton pauseButton;
    private Skin skin;

    OptionsMenuWindow(String title, Skin skin, MenuWindow menuWindow) {
        super(title, skin);
        this.skin = skin;
        this.menuWindow = menuWindow;

        addButtons();
        addAction();
    }

    private void addButtons(){

        saveButton = new TextButton("save",skin);
        this.add(saveButton).size(150,50);
        this.row();

        loadButton = new TextButton("load",skin);
        this.add(loadButton).size(150,50);
        this.row();

        exitButton = new TextButton("Exit game",skin);
        this.add(exitButton).size(150,50);
        this.row();
    }

    private void addAction(){

        saveButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                menuWindow.saveGame();
                return true;
            }
        });

        loadButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                menuWindow.loadGame();
                return true;
            }
        });

        exitButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();
                return true;
            }
        });
    }
}



