package com.cosma.annihilation.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;

public class SpriteComponent implements Component {
    public float time = 0;
    public float lifeTime = 0;
    public boolean isLifeTimeLimited = false;
    public boolean flipTexture = false;
    public float x;
    public float y;
    public float angle = 0;
    public Texture texture;

}
