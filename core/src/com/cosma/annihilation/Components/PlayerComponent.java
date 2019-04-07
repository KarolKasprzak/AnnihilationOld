package com.cosma.annihilation.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.cosma.annihilation.Items.WeaponItem;

import java.util.ArrayList;


public class PlayerComponent implements Component,Json.Serializable {
    public float velocity = 2;
    public int numFootContacts = 0;
    public boolean hidde = false;
    public boolean isWeaponHidden = true;
    public ArrayList<Entity> collisionEntityList = new ArrayList<Entity>();
    public WeaponItem activeWeapon;
    public int activeWeaponAmmo;
    public Entity processedEntity;

    @Override
    public void write(Json json) {

    }

    @Override
    public void read(Json json, JsonValue jsonData) {

    }
}
