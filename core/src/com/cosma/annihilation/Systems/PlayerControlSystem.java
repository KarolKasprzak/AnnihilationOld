package com.cosma.annihilation.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.cosma.annihilation.Components.BodyComponent;
import com.cosma.annihilation.Components.PlayerComponent;
import com.cosma.annihilation.Components.StateComponent;
import com.cosma.annihilation.Gui.OnScreenGui;
import com.cosma.annihilation.Utils.StateManager;

public class PlayerControlSystem extends IteratingSystem{

    private ComponentMapper<PlayerComponent> playerMapper;
    private ComponentMapper<BodyComponent> bodyMapper;
    private Touchpad touchpad;




    public PlayerControlSystem() {
        super(Family.all(PlayerComponent.class).get());
        playerMapper = ComponentMapper.getFor(PlayerComponent.class);
        bodyMapper = ComponentMapper.getFor(BodyComponent.class);
        this.touchpad = OnScreenGui.getTouchpad();

    }
    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        BodyComponent playerBody = bodyMapper.get(entity);
        PlayerComponent player = playerMapper.get(entity);
//        StateComponent state = sm.get(entity);
//        // if body is going down set state falling
//        if(playerBody.body.getLinearVelocity().y > 0){
//            state.set(StateComponent.STATE_FALLING);
//        }
//        // if body stationary on y axis
//        if(playerBody.body.getLinearVelocity().y == 0){
//            // only change to normal if previous state was falling(no mid air jump)
//            if(state.get() == StateComponent.STATE_FALLING){
//                state.set(StateComponent.STATE_NORMAL);
//            }
//            // set state moving if not falling and moving on x axis
//            if(playerBody.body.getLinearVelocity().x != 0){
//                state.set(StateComponent.STATE_MOVING);
//            }
//        }
        // apply forces depending on controller input

        //Jump
        if(!Gdx.input.isKeyPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            playerBody.body.setLinearVelocity(new Vector2(0,playerBody.body.getLinearVelocity().y));
        }

        if(StateManager.onGround) {
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                playerBody.body.applyLinearImpulse(new Vector2(0, 2f),
                        playerBody.body.getWorldCenter(), true);
            }
        }

        //Ladder up
        if(StateManager.canClimb) {
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                StateManager.climbing = true;
                StateManager.canMoveOnSide = false;
                playerBody.body.setGravityScale(0);
                playerBody.body.setLinearVelocity(new Vector2(0, 1));
            }else {
                if(StateManager.climbing){playerBody.body.setLinearVelocity(new Vector2(0, 0));}
                StateManager.canMoveOnSide = true;}
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                StateManager.climbing = true;
                playerBody.body.setGravityScale(0);
                StateManager.canMoveOnSide = false;
                playerBody.body.setLinearVelocity(new Vector2(0, -1));
            }

        }else {
            playerBody.body.setGravityScale(1);
//            StateManager.climbing = false;
        }

        //Moving on side
        if(StateManager.canMoveOnSide) {
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {

                Vector2 vec = playerBody.body.getLinearVelocity();
                float desiredSpeed = player.velocity;
                StateManager.climbing = false;
                playerBody.body.setGravityScale(1);
                float speedX = desiredSpeed - vec.x;
                float impulse = playerBody.body.getMass() * speedX;
                playerBody.body.applyLinearImpulse(new Vector2(impulse, 0),
                        playerBody.body.getWorldCenter(), true);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                Vector2 vec = playerBody.body.getLinearVelocity();
                float desiredSpeed = -player.velocity;
                float speedX = desiredSpeed - vec.x;
                StateManager.climbing = false;
                playerBody.body.setGravityScale(1);
                float impulse = playerBody.body.getMass() * speedX;
                playerBody.body.applyLinearImpulse(new Vector2(impulse, 0),
                        playerBody.body.getWorldCenter(), true);
            }
        }
    }
}