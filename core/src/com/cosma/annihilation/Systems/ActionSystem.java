package com.cosma.annihilation.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.*;
import com.cosma.annihilation.Components.*;
import com.cosma.annihilation.Utils.Constants;
import com.cosma.annihilation.Utils.Enums.EntityAction;
import com.cosma.annihilation.Utils.Enums.CollisionID;
import com.cosma.annihilation.Utils.Enums.GameEvent;

public class ActionSystem extends IteratingSystem implements Listener<GameEvent> {
    private ComponentMapper<BodyComponent> bodyMapper;
    private ComponentMapper<PlayerComponent> playerMapper;
    private ComponentMapper<PlayerStateComponent> stateMapper;

    private Body playerBody;
    private World world;
    private PlayerComponent playerComponent;
    private PlayerStateComponent playerState;

    Filter filter;
    Filter filter1;

    public ActionSystem(World world) {
        super(Family.all(PlayerComponent.class).get(), Constants.ACTION_SYSTEM);
        this.world = world;
        bodyMapper = ComponentMapper.getFor(BodyComponent.class);
        playerMapper = ComponentMapper.getFor(PlayerComponent.class);
        stateMapper = ComponentMapper.getFor(PlayerStateComponent.class);
        filter = new Filter();
        filter.categoryBits = CollisionID.NO_SHADOW;

        filter1 = new Filter();
        filter1.categoryBits = CollisionID.CAST_SHADOW;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        playerComponent = playerMapper.get(entity);
        playerBody = bodyMapper.get(entity).body;
        playerState = stateMapper.get(entity);

        if (!playerComponent.collisionEntityList.isEmpty()) {
            playerComponent.processedEntity = playerComponent.collisionEntityList.listIterator().next();
            playerComponent.processedEntity.getComponent(TextureComponent.class).renderWithShader = true;
        } else
            playerComponent.processedEntity = null;
    }

    @Override
    public void receive(Signal<GameEvent> signal, GameEvent event) {
        switch(event){
            case PERFORM_ACTION:
                if (playerComponent.processedEntity != null && playerComponent.isWeaponHidden) {
                    EntityAction action = playerComponent.processedEntity.getComponent(ActionComponent.class).action;
                    switch (action) {
                        case OPEN_DOOR:
                            doorAction();
                            break;
                        case OPEN:
                            openBoxAction();
                            break;
                    }
                }
                break;
            case CROUCH:
                break;

        }

    }
//
//    private void playerCrouch(){
//        if(playerState.crouch){
//            Fixture fixture = playerBody.getFixtureList().get(0);
//            Shape shape = fixture.getShape();
//
//
//
//        }
//    }



    private void openBoxAction() {
        if (playerComponent.processedEntity.getComponent(ContainerComponent.class).itemLocations.size > -1) {
                  getEngine().getSystem(UserInterfaceSystem.class).showLootWindow(playerComponent.processedEntity);
        }
    }

    private void doorAction() {
        if (playerComponent.processedEntity.getComponent(BodyComponent.class).body.getFixtureList().get(0).isSensor()) {
            playerComponent.processedEntity.getComponent(BodyComponent.class).body.getFixtureList().get(0).setSensor(false);
            playerComponent.processedEntity.getComponent(BodyComponent.class).body.getFixtureList().get(0).setFilterData(filter1);
            playerComponent.processedEntity.getComponent(BodyComponent.class).body.getFixtureList().get(0).refilter();
            playerComponent.processedEntity.getComponent(DoorComponent.class).isOpen = false;

        } else {
            playerComponent.processedEntity.getComponent(BodyComponent.class).body.getFixtureList().get(0).setSensor(true);
            playerComponent.processedEntity.getComponent(BodyComponent.class).body.getFixtureList().get(0).setFilterData(filter);
            playerComponent.processedEntity.getComponent(DoorComponent.class).isOpen = true;

        }
    }

    public void loadDoor(Entity entity){
        entity.getComponent(BodyComponent.class).body.getFixtureList().get(0).setSensor(true);
        entity.getComponent(BodyComponent.class).body.getFixtureList().get(0).setFilterData(filter);
    }
}
