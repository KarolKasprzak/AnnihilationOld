package com.cosma.annihilation.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.cosma.annihilation.Utils.AnimationFactory;

import java.util.HashMap;

public class AnimationComponent implements Component {
    public float time = 0f;
    public AnimationFactory.AnimationId animationId;
    public Animation<TextureRegion> currentAnimation;
    public HashMap<String,Animation<TextureRegion>> animationMap = new HashMap<>();
}
