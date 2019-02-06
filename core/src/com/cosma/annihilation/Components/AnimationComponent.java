package com.cosma.annihilation.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;

public class AnimationComponent implements Component {
    public float time = 0f;
    public Animation<TextureRegion> currentAnimation;
}
