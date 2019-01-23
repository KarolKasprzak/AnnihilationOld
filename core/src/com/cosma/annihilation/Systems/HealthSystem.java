package com.cosma.annihilation.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.cosma.annihilation.Components.*;
import com.cosma.annihilation.Gui.Gui;
import com.cosma.annihilation.Utils.Constants;
import com.cosma.annihilation.Utils.EntityEventSignal;
import com.cosma.annihilation.Utils.TextActor;

import java.util.concurrent.TimeUnit;

public class HealthSystem extends IteratingSystem implements Listener<EntityEventSignal> {


    private ComponentMapper<HealthComponent> healthMapper;
    private Gui gui;
    private  OrthographicCamera camera;

    public HealthSystem(Gui gui, OrthographicCamera camera) {
        super(Family.all(HealthComponent.class).get(), Constants.HEALTHSYSTEM);
        this.gui = gui;
        this.camera = camera;

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
    public void receive(Signal<EntityEventSignal> signal, EntityEventSignal entityEvent) {
        switch (entityEvent.getGameEvent()) {
            case OBJECT_HIT:
                 calculateAccuracy(entityEvent);

                break;
            case WEAPON_SHOOT:

                break;
        }
    }

    private void calculateAccuracy(EntityEventSignal entityEvent){
        if(entityEvent.getAccuracy()){
            displayMessage(entityEvent,"miss");

        }else{
            entityEvent.getEntity().getComponent(HealthComponent.class).hp -= entityEvent.getDamage();
            displayMessage(entityEvent,Integer.toString(entityEvent.getDamage()) + " dmg");

        }
    }

    private void displayMessage(EntityEventSignal entityEvent, String message){
//        Vector3 worldCoordinates = new Vector3(entityEvent.getEntity().getComponent(TransformComponent.class).position.x, entityEvent.getEntity().getComponent(TransformComponent.class).position.y, 0);
//        Vector3 cameraCoordinates = camera.project(worldCoordinates);
//        TextActor floatingText = new TextActor(message, TimeUnit.SECONDS.toMillis(1));
//        floatingText.setPosition(cameraCoordinates.x, cameraCoordinates.y+100);
//        floatingText.setDeltaY(200);
//        gui.getStage().addActor(floatingText);
//        floatingText.animate();
    }


}
