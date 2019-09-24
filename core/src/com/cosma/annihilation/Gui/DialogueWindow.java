package com.cosma.annihilation.Gui;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class DialogueWindow extends Window {




    public DialogueWindow(Skin skin) {
        super("", skin);

        setFillParent(true);



        TextButton exitButton = new TextButton("exit",skin);

        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                remove();
            }
        });

        add(exitButton).center().expand();





    }
}
