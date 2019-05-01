package com.cosma.annihilation.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.cosma.annihilation.Components.BodyComponent;
import com.cosma.annihilation.Components.PlayerComponent;
import com.cosma.annihilation.Components.PlayerStateComponent;
import com.cosma.annihilation.Utils.Constants;
import com.cosma.annihilation.Utils.StateManager;

public class PlayerControlSystem extends IteratingSystem {

    private ComponentMapper<PlayerComponent> playerMapper;
    private ComponentMapper<BodyComponent> bodyMapper;
    private ComponentMapper<PlayerStateComponent> stateMapper;

    public PlayerControlSystem() {
        super(Family.all(PlayerComponent.class).get(), Constants.PLAYER_CONTROL_SYSTEM);
        playerMapper = ComponentMapper.getFor(PlayerComponent.class);
        bodyMapper = ComponentMapper.getFor(BodyComponent.class);
        stateMapper = ComponentMapper.getFor(PlayerStateComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        BodyComponent playerBody = bodyMapper.get(entity);
        PlayerComponent player = playerMapper.get(entity);
        PlayerStateComponent stateComponent = stateMapper.get(entity);

        // prevent slip
        if (!Gdx.input.isKeyPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                 playerBody.body.setLinearVelocity(new Vector2(0, playerBody.body.getLinearVelocity().y));
        }
        // Jumping
        if (stateComponent.onGround && stateComponent.canJump) {

                if (Gdx.input.isKeyPressed(Input.Keys.W) || stateComponent.goUp) {
                    playerBody.body.applyLinearImpulse(new Vector2(0, 8f),
                            playerBody.body.getWorldCenter(), true);
                }
        }
        //Climbing
        if(stateComponent.climbing){
            stateComponent.canJump = false;
            playerBody.body.setGravityScale(0);
            playerBody.body.setLinearVelocity(new Vector2(0, 0));
        }else playerBody.body.setGravityScale(1);


        if(stateComponent.canClimb  && playerBody.body.getLinearVelocity().x == 0) {
            if (Gdx.input.isKeyPressed(Input.Keys.W)|| stateComponent.goUp) {
                stateComponent.climbing = true;
                if(playerBody.body.getLinearVelocity().x == 0f) {
                    playerBody.body.setLinearVelocity(new Vector2(0, 1));
                }
            }
        }
        if(stateComponent.canClimb || stateComponent.canClimbDown) {
            if (Gdx.input.isKeyPressed(Input.Keys.S)|| stateComponent.goDown) {
                stateComponent.climbing = true;
                playerBody.body.setLinearVelocity(new Vector2(0, -1));
            }
        }


        //Stealth mode
        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) ) {
            //TODO

        }

        //Weapon pick up
        if (Gdx.input.isKeyPressed(Input.Buttons.RIGHT) ) {
            //TODO

        }

        //Action/Shoot
        if (Gdx.input.isKeyPressed(Input.Buttons.LEFT) ) {
            //TODO

        }

        //Moving on side
        if(stateComponent.canMoveOnSide) {
            if (Gdx.input.isKeyPressed(Input.Keys.D ) || stateComponent.goRight) {

                Vector2 vec = playerBody.body.getLinearVelocity();
                float desiredSpeed = player.velocity;
                stateComponent.climbing = false;
                stateComponent.playerDirection = true;
                float speedX = desiredSpeed - vec.x;
                float impulse = playerBody.body.getMass() * speedX;
                playerBody.body.applyLinearImpulse(new Vector2(impulse, 0),
                        playerBody.body.getWorldCenter(), true);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A) || stateComponent.goLeft) {
                Vector2 vec = playerBody.body.getLinearVelocity();
                float desiredSpeed = -player.velocity;
                float speedX = desiredSpeed - vec.x;
                stateComponent.climbing = false;
                stateComponent.playerDirection = false;
                float impulse = playerBody.body.getMass() * speedX;
                playerBody.body.applyLinearImpulse(new Vector2(impulse, 0),
                        playerBody.body.getWorldCenter(), true);
            }
        }
    }
}