package com.cosma.annihilation.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.cosma.annihilation.Components.HealthComponent;
import com.cosma.annihilation.Components.PlayerComponent;
import com.cosma.annihilation.Utils.Constants;
import com.cosma.annihilation.Utils.StateManager;

public class HealthSystem extends IteratingSystem {

    private ComponentMapper<PlayerComponent> playerMapper;
    private ComponentMapper<HealthComponent> healthMapper;

    public HealthSystem() {
           super(Family.all(HealthComponent.class, PlayerComponent.class).get(), Constants.HEALTHSYSTEM);
           playerMapper = ComponentMapper.getFor(PlayerComponent.class);
           healthMapper = ComponentMapper.getFor(HealthComponent.class);

    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        float hp = healthMapper.get(entity).hp;
        float maxHP = healthMapper.get(entity).maxHP;

    }
}
