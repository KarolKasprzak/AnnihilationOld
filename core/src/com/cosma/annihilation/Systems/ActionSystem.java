package com.cosma.annihilation.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.cosma.annihilation.Components.*;
import com.cosma.annihilation.Gui.PlayerGUI;
import com.cosma.annihilation.Utils.Enums.ActionID;
import com.cosma.annihilation.Utils.Enums.GameEvent;

public class ActionSystem extends IteratingSystem implements Listener<GameEvent> {
    private ComponentMapper<BodyComponent> bodyMapper;
    private ComponentMapper<PlayerComponent> playerMapper;
    private Body playerBody;
    private World world;
    private PlayerComponent playerComponent;
    private PlayerGUI playerGUI;

    public ActionSystem(World world) {
        super(Family.all(PlayerComponent.class).get(),11);
        this.world = world;
        bodyMapper = ComponentMapper.getFor(BodyComponent.class);
        playerMapper = ComponentMapper.getFor(PlayerComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        playerComponent = playerMapper.get(entity);

        if(!playerComponent.collisionEntityList.isEmpty()) {
            playerComponent.processedEntity = playerComponent.collisionEntityList.listIterator().next();

            playerGUI.setDisplayedActionName(playerComponent.processedEntity.getComponent(ActionComponent.class).action);
        }
            else
                playerComponent.processedEntity = null;
                playerGUI.setDisplayedActionName(ActionID.NOTHING);
    }

    public void setPlayerGUI(PlayerGUI playerGUI){
        this.playerGUI = playerGUI;
    }

    public PlayerGUI getPlayerGUI(){
        return  playerGUI;
    }

    @Override
    public void receive(Signal<GameEvent> signal, GameEvent event) {
        if (event.equals(GameEvent.PERFORM_ACTION) && playerComponent.processedEntity != null) {
            ActionID action = playerComponent.processedEntity.getComponent(ActionComponent.class).action;

            switch (action) {
                case OPEN_DOOR:
                    break;
                case OPEN:
                    openBoxAction();
                    break;

            }
        }
    }

    private void openBoxAction(){
        System.out.println(playerComponent.processedEntity.getComponent(ActionComponent.class).action);
        if(playerComponent.processedEntity.getComponent(ContainerComponent.class).itemLocations.size>0){
          playerGUI.showLootWindow(playerComponent.processedEntity);
        }
    }
}
