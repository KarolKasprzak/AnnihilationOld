package com.cosma.annihilation.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.cosma.annihilation.Annihilation;
import com.cosma.annihilation.Utils.GfxAssetDescriptors;
import com.cosma.annihilation.Utils.Utilities;

public class HealthComponent implements Component, Json.Serializable {
    public int hp = 100;
    public int maxHP = 100;
    public Label hpIndicator;

    @Override
    public void write(Json json) {
        json.writeValue("hp", hp);
        json.writeValue("maxHp", maxHP);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        hp = jsonData.getInt("hp");
        maxHP = jsonData.getInt("maxHp");
        hpIndicator = new Label("",Annihilation.getAssets().get(GfxAssetDescriptors.skin));
        hpIndicator.setFontScale(Utilities.setFontScale(1));
        hpIndicator.setColor(0,82,0,255);
    }
}
