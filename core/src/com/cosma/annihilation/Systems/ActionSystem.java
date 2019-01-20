package com.cosma.annihilation.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.World;
import com.cosma.annihilation.Components.*;
import com.cosma.annihilation.Gui.Gui;
import com.cosma.annihilation.Utils.Enums.ActionID;
import com.cosma.annihilation.Utils.Enums.CollisionID;
import com.cosma.annihilation.Utils.Enums.GameEvent;
import com.cosma.annihilation.Utils.GfxAssetDescriptors;

public class ActionSystem extends IteratingSystem implements Listener<GameEvent> {
    private ComponentMapper<BodyComponent> bodyMapper;
    private ComponentMapper<PlayerComponent> playerMapper;
    private Body playerBody;
    private World world;
    private PlayerComponent playerComponent;
    private final Gui gui;
    Filter filter;
    Filter filter1;

    public ActionSystem(World world,Gui gui) {
        super(Family.all(PlayerComponent.class).get(), 11);
        this.world = world;
        this.gui = gui;
        bodyMapper = ComponentMapper.getFor(BodyComponent.class);
        playerMapper = ComponentMapper.getFor(PlayerComponent.class);
        filter = new Filter();
        filter.categoryBits = CollisionID.NO_SHADOW;

        filter1 = new Filter();
        filter1.categoryBits = CollisionID.CAST_SHADOW;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        playerComponent = playerMapper.get(entity);

        if (!playerComponent.collisionEntityList.isEmpty()) {
            playerComponent.processedEntity = playerComponent.collisionEntityList.listIterator().next();
            playerComponent.processedEntity.getComponent(TextureComponent.class).renderWithShader = true;
        } else
            playerComponent.processedEntity = null;
    }

    @Override
    public void receive(Signal<GameEvent> signal, GameEvent event) {
        if (event.equals(GameEvent.PERFORM_ACTION) && playerComponent.processedEntity != null && playerComponent.isWeaponHidden) {
            ActionID action = playerComponent.processedEntity.getComponent(ActionComponent.class).action;

            switch (action) {
                case OPEN_DOOR:
                    doorAction();
                    break;
                case OPEN:
                    openBoxAction();
                    break;
            }
        }
    }

    private void openBoxAction() {
        if (playerComponent.processedEntity.getComponent(ContainerComponent.class).itemLocations.size > -1) {
            if(gui != null){
                gui.showLootWindow(playerComponent.processedEntity);
            }

        }
    }

    private void doorAction() {
        if (playerComponent.processedEntity.getComponent(BodyComponent.class).body.getFixtureList().get(0).isSensor()) {
            playerComponent.processedEntity.getComponent(BodyComponent.class).body.getFixtureList().get(0).setSensor(false);
            playerComponent.processedEntity.getComponent(BodyComponent.class).body.getFixtureList().get(0).setFilterData(filter1);
            playerComponent.processedEntity.getComponent(BodyComponent.class).body.getFixtureList().get(0).refilter();
            playerComponent.processedEntity.getComponent(DoorComponent.class).isOpen = false;
            playerComponent.processedEntity.getComponent(TextureComponent.class).setTexture(GfxAssetDescriptors.door.fileName);
        } else {
            playerComponent.processedEntity.getComponent(BodyComponent.class).body.getFixtureList().get(0).setSensor(true);
            playerComponent.processedEntity.getComponent(BodyComponent.class).body.getFixtureList().get(0).setFilterData(filter);
            playerComponent.processedEntity.getComponent(DoorComponent.class).isOpen = true;
            playerComponent.processedEntity.getComponent(TextureComponent.class).setTexture(GfxAssetDescriptors.door_open.fileName);
        }
    }

    public void loadDoor(Entity entity){
        entity.getComponent(BodyComponent.class).body.getFixtureList().get(0).setSensor(true);
        entity.getComponent(BodyComponent.class).body.getFixtureList().get(0).setFilterData(filter);
        entity.getComponent(TextureComponent.class).setTexture(GfxAssetDescriptors.door_open.fileName);
    }
}
