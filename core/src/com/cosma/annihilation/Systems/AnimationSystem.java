package com.cosma.annihilation.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.cosma.annihilation.Components.*;
import com.cosma.annihilation.Utils.*;
import com.cosma.annihilation.Utils.Enums.AnimationStates;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;

public class AnimationSystem extends IteratingSystem {

    private ComponentMapper<TextureComponent> textureMapper;
    private ComponentMapper<StateComponent> stateMapper;
    private ComponentMapper<AnimationComponent> animationMapper;


    public AnimationSystem() {
        super(Family.all(StateComponent.class, TextureComponent.class, AnimationComponent.class).get(), Constants.ANIMATION);
        stateMapper = ComponentMapper.getFor(StateComponent.class);
        textureMapper = ComponentMapper.getFor(TextureComponent.class);
        animationMapper = ComponentMapper.getFor(AnimationComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TextureComponent textureComponent = textureMapper.get(entity);
        AnimationComponent animationComponent = animationMapper.get(entity);
        StateComponent stateComponent = stateMapper.get(entity);

        textureComponent.flipTexture = !stateComponent.spriteDirection;

        animationComponent.time += deltaTime;

        animationComponent.currentAnimation = animationComponent.animationMap.get(animationComponent.animationState.toString());

        if (animationMapper.get(entity).currentAnimation != null) {
            textureComponent.texture_ = animationComponent.currentAnimation.getKeyFrame(animationComponent.time);
        }
    }
}
