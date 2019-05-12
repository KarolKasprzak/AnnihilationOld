package com.cosma.annihilation.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.cosma.annihilation.Components.AnimationComponent;
import com.cosma.annihilation.Components.BodyComponent;
import com.cosma.annihilation.Components.PlayerComponent;
import com.cosma.annihilation.Components.StateComponent;
import com.cosma.annihilation.Utils.Constants;
import com.cosma.annihilation.Utils.Enums.AnimationStates;
import com.cosma.annihilation.Utils.Enums.GameEvent;

public class PlayerControlSystem extends IteratingSystem{

    private ComponentMapper<PlayerComponent> playerMapper;
    private ComponentMapper<BodyComponent> bodyMapper;
    private ComponentMapper<StateComponent> stateMapper;
    private ComponentMapper<AnimationComponent> animationMapper;
    private Signal<GameEvent> signal;
    private boolean isLeftButtonClicked = false;
    public PlayerControlSystem() {
        super(Family.all(PlayerComponent.class).get(), Constants.PLAYER_CONTROL_SYSTEM);
        playerMapper = ComponentMapper.getFor(PlayerComponent.class);
        bodyMapper = ComponentMapper.getFor(BodyComponent.class);
        stateMapper = ComponentMapper.getFor(StateComponent.class);
        animationMapper = ComponentMapper.getFor(AnimationComponent.class);
        signal = new Signal<GameEvent>();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        BodyComponent playerBody = bodyMapper.get(entity);
        PlayerComponent player = playerMapper.get(entity);
        PlayerComponent playerComponent = playerMapper.get(entity);
        AnimationComponent animationComponent = animationMapper.get(entity);
        StateComponent  stateComponent = stateMapper.get(entity);




//         prevent slip/idle mode
        if (!Gdx.input.isKeyPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.RIGHT)  && playerComponent.onGround && !animationComponent.isAnimationPlayed) {
                 playerBody.body.setLinearVelocity(new Vector2(0, playerBody.body.getLinearVelocity().y));
                 animationComponent.animationState = AnimationStates.IDLE;
        }


        if (!Gdx.input.isKeyPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.RIGHT)  && playerComponent.onGround) {
            playerBody.body.setLinearVelocity(new Vector2(0, playerBody.body.getLinearVelocity().y));
        }


        // Jumping
        if (playerComponent.onGround && playerComponent.canJump && playerComponent.isWeaponHidden) {

                if (Gdx.input.isKeyPressed(Input.Keys.SPACE) || playerComponent.goUp) {
                    float x;
                    if(animationComponent.spriteDirection){
                        x = 30;
                    } else x = -30;
                    playerBody.body.applyLinearImpulse(new Vector2(x, 10),
                            playerBody.body.getWorldCenter(), true);
                }
        }
        //Climbing
        if(playerComponent.climbing){
            playerComponent.canJump = false;
            playerBody.body.setGravityScale(0);
            playerBody.body.setLinearVelocity(new Vector2(0, 0));
        }else playerBody.body.setGravityScale(1);


        if(playerComponent.canClimb  && playerBody.body.getLinearVelocity().x == 0) {
            if (Gdx.input.isKeyPressed(Input.Keys.W)|| playerComponent.goUp) {
                playerComponent.climbing = true;
                if(playerBody.body.getLinearVelocity().x == 0f) {
                    playerBody.body.setLinearVelocity(new Vector2(0, 1));
                }
            }
        }
        if(playerComponent.canClimb || playerComponent.canClimbDown) {
            if (Gdx.input.isKeyPressed(Input.Keys.S)|| playerComponent.goDown) {
                playerComponent.climbing = true;
                playerBody.body.setLinearVelocity(new Vector2(0, -1));
            }
        }


        //Stealth mode
        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) ) {
            //TODO

        }

        //Moving on side
        if(playerComponent.canMoveOnSide && playerComponent.onGround) {
            if (Gdx.input.isKeyPressed(Input.Keys.D ) || playerComponent.goRight) {
                animationComponent.animationState = AnimationStates.WALK;
                Vector2 vec = playerBody.body.getLinearVelocity();
                float desiredSpeed = player.velocity;
                playerComponent.climbing = false;
                animationComponent.spriteDirection = true;
                float speedX = desiredSpeed - vec.x;
                float impulse = playerBody.body.getMass() * speedX;
                playerBody.body.applyLinearImpulse(new Vector2(impulse, 0),
                        playerBody.body.getWorldCenter(), true);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A) || playerComponent.goLeft) {
                animationComponent.animationState = AnimationStates.WALK;
                Vector2 vec = playerBody.body.getLinearVelocity();
                float desiredSpeed = -player.velocity;
                float speedX = desiredSpeed - vec.x;
                playerComponent.climbing = false;
                animationComponent.spriteDirection = false;
                float impulse = playerBody.body.getMass() * speedX;
                playerBody.body.applyLinearImpulse(new Vector2(impulse, 0),
                        playerBody.body.getWorldCenter(), true);
            }
        }
    }

    public void addListenerSystems(){
        signal.add(getEngine().getSystem(ActionSystem.class));
        signal.add(getEngine().getSystem(ShootingSystem.class));
        signal.add(getEngine().getSystem(UserInterfaceSystem.class));
    }

}