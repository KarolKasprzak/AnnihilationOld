package com.cosma.annihilation.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.cosma.annihilation.Annihilation;

public class TextureComponent implements Component {
    public String texturePatch;
    public Texture texture;
    public TextureRegion textureRegion;
    public boolean flipTexture = false;
    public boolean renderWithShader = false;
}
