package com.cosma.annihilation.Gui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.cosma.annihilation.Items.ItemFactory;
import com.cosma.annihilation.Items.WeaponItem;


class ItemStatsWindow extends Window {
    private Label weaponName;
    private Label weaponDescription;
    private Label weaponDamage;
    private Label weaponAccuracy;
    private Label weaponAmmo;
    private Label weaponAmmoInMagazine;

    ItemStatsWindow(String title, Skin skin) {
        super(title, skin);

        this.setColor(0, 82, 0, 1);

        weaponName = new Label("", skin);
        weaponName.setColor(0, 82, 0, 255);
        weaponName.setFontScale(1.1f);
        this.add(weaponName).left().expandX().top().pad(4);
        this.row();

        weaponDescription = new Label("", skin);
        weaponDescription.setColor(0, 82, 0, 255);
        weaponDescription.setWrap(true);
        this.add(weaponDescription).left().pad(4);
        this.row();

        weaponDamage = new Label("", skin);
        weaponDamage.setColor(0, 82, 0, 255);
        weaponDamage.setFontScale(0.9f);
        this.add(weaponDamage).left().expandX().top().pad(4).padTop(8);
        this.row();

        weaponAccuracy = new Label("", skin);
        weaponAccuracy.setColor(0, 79, 0, 255);
        weaponAccuracy.setFontScale(0.9f);
        this.add(weaponAccuracy).left().expandX().top().pad(4);
        this.row();

        weaponAmmo = new Label("", skin);
        weaponAmmo.setColor(0, 82, 0, 255);
        weaponAmmo.setFontScale(0.9f);
        this.add(weaponAmmo).left().expandX().top().pad(4);
        this.row();

        weaponAmmoInMagazine = new Label("", skin);
        weaponAmmoInMagazine.setColor(0, 82, 0, 255);
        weaponAmmoInMagazine.setFontScale(0.9f);
        this.add(weaponAmmoInMagazine).left().expandX().top().pad(4);
        this.row();
    }

    void setDmglabel(WeaponItem item) {
        weaponName.setText(item.getItemName());
        weaponDescription.setText(item.getItemShortDescription());
        weaponDamage.setText("Weapon damage: " + item.getDamage());
        weaponAccuracy.setText("Weapon accuracy: " + item.getAccuracy());
        weaponAmmo.setText("Ammunition type: " + ItemFactory.getInstance().getItem(item.getAmmoID()).getItemName());
        weaponAmmoInMagazine.setText("Magazine capacity: " + item.getMaxAmmoInMagazine());

    }


}
