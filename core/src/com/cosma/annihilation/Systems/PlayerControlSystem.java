package com.cosma.annihilation.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.cosma.annihilation.Components.*;
import com.cosma.annihilation.Items.WeaponItem;
import com.cosma.annihilation.Utils.Constants;
import com.cosma.annihilation.Utils.Animation.AnimationStates;
import com.cosma.annihilation.Utils.Enums.GameEvent;

import java.util.ArrayList;

public class PlayerControlSystem extends IteratingSystem implements InputProcessor {

    private ComponentMapper<PlayerComponent> playerMapper;
    private ComponentMapper<BodyComponent> bodyMapper;
    private ComponentMapper<StateComponent> stateMapper;
    private ComponentMapper<AnimationComponent> animationMapper;
    private Signal<GameEvent> signal;
    private ArrayList<GameEvent> gameEventList;
    private RayCastCallback noiseRayCallback;
    // false = left, true = right
    private boolean mouseCursorPosition = false;
    private World world;
    private Entity noiseTestEntity;

    public PlayerControlSystem(ArrayList<GameEvent> gameEventList, World world) {
        super(Family.all(PlayerComponent.class).get(), Constants.PLAYER_CONTROL_SYSTEM);
        this.gameEventList = gameEventList;
        this.world = world;
        playerMapper = ComponentMapper.getFor(PlayerComponent.class);
        bodyMapper = ComponentMapper.getFor(BodyComponent.class);
        stateMapper = ComponentMapper.getFor(StateComponent.class);
        animationMapper = ComponentMapper.getFor(AnimationComponent.class);
        signal = new Signal<GameEvent>();

        noiseRayCallback = new RayCastCallback() {
            @Override
            public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                if (fixture.getBody().getUserData() instanceof Entity && ((Entity) fixture.getBody().getUserData()).getComponent(AiComponent.class) != null) {
                    noiseTestEntity = (Entity) fixture.getBody().getUserData();
//                    ((Entity) fixture.getBody().getUserData()).getComponent(AiComponent.class).isHearEnemy = true;
                }
                return 0;
            }
        };

    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        BodyComponent playerBody = bodyMapper.get(entity);
        PlayerComponent playerComponent = playerMapper.get(entity);
        AnimationComponent animationComponent = animationMapper.get(entity);
        animationComponent.spriteDirection = mouseCursorPosition;


        for(GameEvent gameEvent: gameEventList ){
            signal.dispatch(gameEvent);
        }
        gameEventList.clear();




        if (!Gdx.input.isKeyPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.RIGHT)  && playerComponent.onGround && animationComponent.isAnimationFinish) {
                 playerBody.body.setLinearVelocity(0, playerBody.body.getLinearVelocity().y);
                 animationComponent.animationState = AnimationStates.IDLE;
                 if(!playerComponent.isWeaponHidden){
                     animationComponent.animationState = AnimationStates.IDLE_WEAPON_SMALL;
                 }
        }

        if (!Gdx.input.isKeyPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.RIGHT)  && playerComponent.onGround) {
            playerBody.body.setLinearVelocity(0, playerBody.body.getLinearVelocity().y);
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
        if(playerComponent.canMoveOnSide && playerComponent.onGround && playerComponent.isWeaponHidden) {
            if (Gdx.input.isKeyPressed(Input.Keys.D ) || playerComponent.goRight) {
                float desiredSpeed = playerComponent.velocity;
                playerComponent.climbing = false;
                if(animationComponent.spriteDirection){
                    animationComponent.animationState = AnimationStates.WALK;
                    animationComponent.currentAnimation.setPlayMode(Animation.PlayMode.LOOP);
                }else{
                    animationComponent.animationState = AnimationStates.WALK;
                    animationComponent.currentAnimation.setPlayMode(Animation.PlayMode.LOOP_REVERSED);
                    desiredSpeed = desiredSpeed *0.7f;
                }
                Vector2 vec = playerBody.body.getLinearVelocity();
                float speedX = desiredSpeed - vec.x;
                float impulse = playerBody.body.getMass() * speedX;
                playerBody.body.applyLinearImpulse(new Vector2(impulse, 0),
                        playerBody.body.getWorldCenter(), true);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A) || playerComponent.goLeft) {
                float desiredSpeed = -playerComponent.velocity;
                playerComponent.climbing = false;
                if(animationComponent.spriteDirection){
                    animationComponent.animationState = AnimationStates.WALK;
                    animationComponent.currentAnimation.setPlayMode(Animation.PlayMode.LOOP_REVERSED);
                    desiredSpeed = desiredSpeed *0.7f;
                }else{
                    animationComponent.animationState = AnimationStates.WALK;
                    animationComponent.currentAnimation.setPlayMode(Animation.PlayMode.LOOP);
                }
                Vector2 vec = playerBody.body.getLinearVelocity();
                float speedX = desiredSpeed - vec.x;
                float impulse = playerBody.body.getMass() * speedX;
                playerBody.body.applyLinearImpulse(new Vector2(impulse, 0),
                        playerBody.body.getWorldCenter(), true);
            }
        }
        //Moving on side with weapon
        if(playerComponent.canMoveOnSide && playerComponent.onGround && !playerComponent.isWeaponHidden && playerComponent.activeWeapon != null) {
            if (Gdx.input.isKeyPressed(Input.Keys.D ) || playerComponent.goRight) {
                float desiredSpeed = playerComponent.velocity*0.8f;
                if(animationComponent.spriteDirection){
                    setPlayerAnimation(playerComponent,animationComponent);
                    animationComponent.currentAnimation.setPlayMode(Animation.PlayMode.LOOP);
                }else{
                    setPlayerAnimation(playerComponent,animationComponent);
                    animationComponent.currentAnimation.setPlayMode(Animation.PlayMode.LOOP_REVERSED);
                    desiredSpeed = desiredSpeed *0.7f;
                }
                Vector2 vec = playerBody.body.getLinearVelocity();
                playerComponent.climbing = false;
                float speedX = desiredSpeed - vec.x;
                float impulse = playerBody.body.getMass() * speedX;
                playerBody.body.applyLinearImpulse(new Vector2(impulse, 0),
                        playerBody.body.getWorldCenter(), true);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A) || playerComponent.goLeft) {
                float desiredSpeed = -playerComponent.velocity*0.8f;
                if(animationComponent.spriteDirection){
                    setPlayerAnimation(playerComponent,animationComponent);
                    animationComponent.currentAnimation.setPlayMode(Animation.PlayMode.LOOP_REVERSED);
                    desiredSpeed = desiredSpeed *0.7f;
                }else{
                    setPlayerAnimation(playerComponent,animationComponent);
                    animationComponent.currentAnimation.setPlayMode(Animation.PlayMode.LOOP);
                }
                Vector2 vec = playerBody.body.getLinearVelocity();
                float speedX = desiredSpeed - vec.x;
                playerComponent.climbing = false;
                float impulse = playerBody.body.getMass() * speedX;
                playerBody.body.applyLinearImpulse(new Vector2(impulse, 0),
                        playerBody.body.getWorldCenter(), true);
            }
        }

        //simulatingPlayerNoise
        if(playerBody.body.getLinearVelocity().x != 0 && !playerComponent.isPlayerCrouch){

            world.rayCast(noiseRayCallback,playerBody.body.getPosition().x,playerBody.body.getPosition().y,
                    playerBody.body.getPosition().x+3,playerBody.body.getPosition().y);

            world.rayCast(noiseRayCallback,playerBody.body.getPosition().x,playerBody.body.getPosition().y,
                    playerBody.body.getPosition().x-3,playerBody.body.getPosition().y);
            if(noiseTestEntity != null){
                AnimationComponent animationComponentAi = noiseTestEntity.getComponent(AnimationComponent.class);
                AiComponent aiComponent = noiseTestEntity.getComponent(AiComponent.class);
                if(animationComponentAi.spriteDirection == animationComponent.spriteDirection){
                    aiComponent.isHearEnemy = true;
                    aiComponent.enemyPosition = playerBody.body.getPosition();
                    noiseTestEntity = null;
                }
            }
        }

    }

    public void addListenerSystems(){
        signal.add(getEngine().getSystem(ActionSystem.class));
        signal.add(getEngine().getSystem(ShootingSystem.class));
        signal.add(getEngine().getSystem(UserInterfaceSystem.class));
    }

    public void setPlayerAnimation(PlayerComponent playerComponent, AnimationComponent animationComponent){
        WeaponItem.ItemID weaponID = playerComponent.activeWeapon.getItemID();
        switch (weaponID) {
            case P38:
                animationComponent.animationState = AnimationStates.WALK_WEAPON_SMALL;
                break;
            case MP44:
                animationComponent.animationState = AnimationStates.WALK_WEAPON_MP;
                break;
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        int x = Gdx.graphics.getWidth();
        int y = Gdx.graphics.getHeight();
        mouseCursorPosition = screenX >= x / 2;
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}