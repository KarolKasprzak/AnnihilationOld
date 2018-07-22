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

        // prevent slip
        if(!Gdx.input.isKeyPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            playerBody.body.setLinearVelocity(new Vector2(0,playerBody.body.getLinearVelocity().y));
        }
        // jump
        if(StateManager.onGround && StateManager.canJump) {
            if (Gdx.input.isKeyPressed(Input.Keys.UP)|| touchpad.getKnobPercentY() >= 0.7) {
                     playerBody.body.applyLinearImpulse(new Vector2(0, 2f),
                     playerBody.body.getWorldCenter(), true);
            }
        }
        //Climbing
        if(StateManager.climbing){
            StateManager.canJump = false;
            playerBody.body.setGravityScale(0);
            playerBody.body.setLinearVelocity(new Vector2(0, 0));
        }else playerBody.body.setGravityScale(1);

        //Ladder up
        if(StateManager.canClimb) {
            if (Gdx.input.isKeyPressed(Input.Keys.UP)|| touchpad.getKnobPercentY() >= 0.5) {
                StateManager.climbing = true;
                playerBody.body.setLinearVelocity(new Vector2(0, 1));
            }
        }
        if(StateManager.canClimb || StateManager.canClimbDown) {
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)|| touchpad.getKnobPercentY() <= -0.5) {
                StateManager.climbing = true;
                playerBody.body.setLinearVelocity(new Vector2(0, -1));
            }
        }





        //Moving on side
        if(StateManager.canMoveOnSide) {
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT ) || touchpad.getKnobPercentX() >= 0.5) {

                Vector2 vec = playerBody.body.getLinearVelocity();
                float desiredSpeed = player.velocity;
                StateManager.climbing = false;
                StateManager.playerDirection=false;
                float speedX = desiredSpeed - vec.x;
                float impulse = playerBody.body.getMass() * speedX;
                playerBody.body.applyLinearImpulse(new Vector2(impulse, 0),
                        playerBody.body.getWorldCenter(), true);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || touchpad.getKnobPercentX() <= -0.5) {
                Vector2 vec = playerBody.body.getLinearVelocity();
                float desiredSpeed = -player.velocity;
                float speedX = desiredSpeed - vec.x;
                StateManager.climbing = false;
                StateManager.playerDirection=true;
                float impulse = playerBody.body.getMass() * speedX;
                playerBody.body.applyLinearImpulse(new Vector2(impulse, 0),
                        playerBody.body.getWorldCenter(), true);
            }
        }
    }
}