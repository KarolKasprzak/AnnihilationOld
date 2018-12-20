package com.cosma.annihilation.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.ashley.systems.IteratingSystem;
import com.cosma.annihilation.Components.BodyComponent;
import com.cosma.annihilation.Components.HealthComponent;
import com.cosma.annihilation.Components.TransformComponent;
import com.cosma.annihilation.Utils.Constants;
import com.cosma.annihilation.Utils.EntityEventSignal;

public class HealthSystem extends IteratingSystem implements Listener<EntityEventSignal> {


    private ComponentMapper<HealthComponent> healthMapper;



    public HealthSystem() {
        super(Family.all(HealthComponent.class).get(), Constants.HEALTHSYSTEM);
        healthMapper = ComponentMapper.getFor(HealthComponent.class);

    }


    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        HealthComponent healthComponent = healthMapper.get(entity);


        if (healthComponent.hp <= 0) {
            getEngine().getSystem(CollisionSystem.class).bodiesToRemove.add(entity.getComponent(BodyComponent.class).body);
            this.getEngine().removeEntity(entity);
        }

    }

    @Override
    public void receive(Signal<EntityEventSignal> signal, EntityEventSignal object) {
        switch (object.getGameEvent()) {
            case OBJECT_HIT:
                object.getEntity().getComponent(HealthComponent.class).hp -= object.getDamage();
                this.getEngine().getSystem(RenderSystem.class).renderText("Chuj",object.getEntity().getComponent(TransformComponent.class).position);

                break;
            case WEAPON_SHOOT:

                break;
        }
    }
}
