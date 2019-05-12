package com.cosma.annihilation.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.World;
import com.cosma.annihilation.Components.*;
import com.cosma.annihilation.Utils.Constants;


public class AiSystem extends IteratingSystem {
    private World world;
    private ComponentMapper<AiComponent> aiMapper;
    private ComponentMapper<AnimationComponent> animationMapper;
    private ComponentMapper<BodyComponent> bodyMapper;


    public AiSystem(World world) {
        super(Family.all(AiComponent.class).get(), Constants.ACTION_SYSTEM);
        this.world = world;
        aiMapper = ComponentMapper.getFor(AiComponent.class);
        animationMapper = ComponentMapper.getFor(AnimationComponent.class);
        bodyMapper = ComponentMapper.getFor(BodyComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        AiComponent aiComponent = aiMapper.get(entity);
        AnimationComponent animationComponent = animationMapper.get(entity);
        BodyComponent bodyComponent = bodyMapper.get(entity);

        aiComponent.ai.update(entity);


    }
}
