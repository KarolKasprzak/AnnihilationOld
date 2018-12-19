package com.cosma.annihilation.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.cosma.annihilation.Components.BodyComponent;
import com.cosma.annihilation.Components.HealthComponent;
import com.cosma.annihilation.Gui.PlayerGUI;
import com.cosma.annihilation.Utils.Constants;
import com.cosma.annihilation.Utils.EntityEventSignal;

public class HealthSystem extends IteratingSystem implements Listener<EntityEventSignal> {



    private ComponentMapper<HealthComponent> healthMapper;
    private PlayerGUI playerGUI;
    public OrthographicCamera camera;
    private Vector3 worldCoordinates;

    public HealthSystem() {
        super(Family.all(HealthComponent.class).get(), Constants.HEALTHSYSTEM);
        healthMapper = ComponentMapper.getFor(HealthComponent.class);
        worldCoordinates = new Vector3();


    }


    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        HealthComponent healthComponent = healthMapper.get(entity);




     if (healthComponent.hp <= 0) {
         getEngine().getSystem(CollisionSystem.class).bodiesToRemove.add(entity.getComponent(BodyComponent.class).body);
         this.getEngine().removeEntity(entity);
        }

    }


    public void setPlayerGUI(PlayerGUI playerGUI) {
        this.playerGUI = playerGUI;
    }


    @Override
    public void receive(Signal<EntityEventSignal> signal, EntityEventSignal object) {
        switch (object.getGameEvent()){
            case OBJECT_HIT:
                System.out.println("f" + object.getEntity().getComponent(HealthComponent.class).hp);
                object.getEntity().getComponent(HealthComponent.class).hp -= object.getDamage();
                System.out.println("f1" + object.getEntity().getComponent(HealthComponent.class).hp);
                System.out.println("f2" + object.getDamage());
                break;
            case WEAPON_SHOOT:

                break;
        }
    }
}
