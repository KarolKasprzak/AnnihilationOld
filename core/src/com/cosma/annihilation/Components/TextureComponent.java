package com.cosma.annihilation.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.cosma.annihilation.Annihilation;

public class TextureComponent implements Component, Json.Serializable {
    public String texturePatch;
    public Texture texture;
    public boolean renderWithShader = false;


    @Override
    public void write(Json json) {
        json.writeValue("texture", texturePatch);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        texturePatch = jsonData.getString("texture");
        setTexture();
    }

    public void setTexture(){
        texture = Annihilation.getAssets().get(texturePatch);
    }

}
