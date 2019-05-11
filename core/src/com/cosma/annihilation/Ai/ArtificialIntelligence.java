package com.cosma.annihilation.Ai;

import com.badlogic.gdx.physics.box2d.Body;
import com.cosma.annihilation.Components.AnimationComponent;

public interface ArtificialIntelligence {
    public void update(Body body, AnimationComponent animationComponent);
}
