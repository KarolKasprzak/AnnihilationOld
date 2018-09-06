package com.cosma.annihilation.Gui;

import com.badlogic.gdx.scenes.scene2d.ui.*;

public class ActionSelectWindow extends Window {
    private Skin skin;
    private List list;
    public ActionSelectWindow(String title, Skin skin) {
        super(title, skin);
        this.skin = skin;
        list = new List(skin);
        list.setFillParent(true);
        String[] myList = {"Select", "player", "wall", "ladder"};
        list.setItems(myList);
        this.add(list);

    }
}

