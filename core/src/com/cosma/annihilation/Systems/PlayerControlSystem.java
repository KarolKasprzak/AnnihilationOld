package com.cosma.annihilation.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.cosma.annihilation.Components.BodyComponent;
import com.cosma.annihilation.Components.PlayerComponent;
import com.cosma.annihilation.Components.StateComponent;
import com.cosma.annihilation.Utils.StateManager;

public class PlayerControlSystem extends IteratingSystem{

    private ComponentMapper<PlayerComponent> playerMapper;
    private ComponentMapper<BodyComponent> bodyMapper;
    private ComponentMapper<StateComponent> stateMapper;




    public PlayerControlSystem() {
        super(Family.all(PlayerComponent.class).get());

        playerMapper = ComponentMapper.getFor(PlayerComponent.class);
        bodyMapper = ComponentMapper.getFor(BodyComponent.class);
        stateMapper = ComponentMapper.getFor(StateComponent.class);
    }
    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        BodyComponent b2body = bodyMapper.get(entity);
        PlayerComponent player = playerMapper.get(entity);
//        StateComponent state = sm.get(entity);
//        // if body is going down set state falling
//        if(b2body.body.getLinearVelocity().y > 0){
//            state.set(StateComponent.STATE_FALLING);
//        }
//        // if body stationary on y axis
//        if(b2body.body.getLinearVelocity().y == 0){
//            // only change to normal if previous state was falling(no mid air jump)
//            if(state.get() == StateComponent.STATE_FALLING){
//                state.set(StateComponent.STATE_NORMAL);
//            }
//            // set state moving if not falling and moving on x axis
//            if(b2body.body.getLinearVelocity().x != 0){
//                state.set(StateComponent.STATE_MOVING);
//            }
//        }
        // apply forces depending on controller input
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {

            Vector2 vec = b2body.body.getLinearVelocity();
            float desiredSpeed = -player.velocity;
            float speedX = desiredSpeed - vec.x;
            float impulse =b2body.body.getMass() * speedX;
            b2body.body.applyLinearImpulse(new Vector2(impulse,0),
            b2body.body.getWorldCenter(), true);


        }
        if(StateManager.onGround)
             if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                     b2body.body.applyLinearImpulse(new Vector2(0,2),
                     b2body.body.getWorldCenter(), true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {

            Vector2 vec = b2body.body.getLinearVelocity();
            float desiredSpeed = player.velocity;
            float speedX = desiredSpeed - vec.x;
            float impulse =b2body.body.getMass() * speedX;
            b2body.body.applyLinearImpulse(new Vector2(impulse,0),
                    b2body.body.getWorldCenter(), true);


        }

    }
}