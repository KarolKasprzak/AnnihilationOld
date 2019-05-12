package com.cosma.annihilation.Ai;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.cosma.annihilation.Components.*;

public class HumanAi implements ArtificialIntelligence {
    private Vector2 startPosition;
    private RayCastCallback callback;
    private boolean findTarget = false;
    private Body targetBody;

    private boolean isEnemy, isOnPatrol, canEscape;
    Body body;
//    public HumanAi() {
//        isOnPatrol = true;
//    }

    public HumanAi() {
        this.startPosition = startPosition;
        isOnPatrol = true;
        callback = new RayCastCallback() {
            @Override
            public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                if(fixture.getBody().getUserData() instanceof Entity && ((Entity) fixture.getBody().getUserData()).getComponent(PlayerComponent.class ) != null){
                   findTarget = true;
                   targetBody = fixture.getBody();
                }
                return 0;
            }
        };
    }

    @Override
    public void update(Entity entity) {
        body = entity.getComponent(BodyComponent.class).body;
        World world = body.getWorld();

        AnimationComponent animationComponent = entity.getComponent(AnimationComponent.class);

        world.rayCast(callback, body.getPosition(), new Vector2(body.getPosition().x -5, body.getPosition().y));

        if(animationComponent.spriteDirection){
            world.rayCast(callback, body.getPosition(), new Vector2(body.getPosition().x +5, body.getPosition().y));
        }else world.rayCast(callback, body.getPosition(), new Vector2(body.getPosition().x -5, body.getPosition().y));

        onPatrol(animationComponent, body);


    }

    private void goToPosition(Vector2 targetPosition,AnimationComponent animationComponent, Body aiBody ){
        Vector2 aiPosition = aiBody.getPosition();
        if(targetPosition.x+ 1.5f < aiPosition.x){
            aiBody.setLinearVelocity(new Vector2(-1, 0));
            animationComponent.spriteDirection = false;
        }
        if(targetPosition.x+ 1.5f > aiPosition.x){
            aiBody.setLinearVelocity(new Vector2(1, 0));
            animationComponent.spriteDirection = true;
        }
    }


    private void onPatrol(AnimationComponent animationComponent, Body aiBody){


            if(findTarget && targetBody != null){
                Vector2 targetPosition = targetBody.getPosition();
                Vector2 aiPosition = aiBody.getPosition();
                System.out.println(targetPosition);

                if(targetPosition.x+ 1.5f < aiPosition.x){
                    aiBody.setLinearVelocity(new Vector2(-1, 0));
                    animationComponent.spriteDirection = false;
                }
                if(targetPosition.x - 1.5f > aiPosition.x){
                    aiBody.setLinearVelocity(new Vector2(1, 0));
                    animationComponent.spriteDirection = true;
                }

            }
    }

}

