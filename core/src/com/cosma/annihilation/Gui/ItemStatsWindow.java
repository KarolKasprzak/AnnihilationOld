package com.cosma.annihilation.Gui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.cosma.annihilation.Annihilation;
import com.cosma.annihilation.Items.InventoryItem;
import com.cosma.annihilation.Items.WeaponItem;
import com.cosma.annihilation.Utils.Utilities;

class ItemStatsWindow extends Window {
    private Skin skin;
    private TextButton closeButton;
    private Label weaponNameValue;
    Label label1;

    ItemStatsWindow(String title, Skin skin) {
        super(title, skin);
        this.skin = skin;

        this.setColor(0, 82, 0, 255);

        Label weaponName = new Label("Weapon:", skin);
        weaponName.setColor(0, 82, 0, 255);
        this.add(weaponName).size(80,50);
        weaponNameValue = new Label("", skin);
        weaponNameValue.setColor(0, 82, 0, 255);
        this.add(weaponNameValue).size(80,50);

        this.row();
        label1 = new Label("Dmg = 25", skin);
        label1.setColor(0, 82, 0, 255);
        this.add(label1);
        this.row();
        Label label2 = new Label("Ammo in magazine = 45", skin);
        label2.setColor(0, 82, 0, 255);
        this.add(label2);
        this.row();
    }

    public void setDmglabel(WeaponItem item) {
        weaponNameValue.setText(item.getItemName());
        label1.setText(Integer.toString(item.getDamage()));

    }


}
