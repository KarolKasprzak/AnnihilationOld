package com.cosma.annihilation.Ai;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.cosma.annihilation.Components.*;
import com.cosma.annihilation.Utils.Util;

public class HumanAi implements ArtificialIntelligence {
    private Vector2 startPosition;
    private RayCastCallback callback;
    private boolean findTarget = false;
    private Body targetBody;
    private PatrolBehaviour patrolBehaviour;
    private boolean isEnemy, isOnPatrol, canEscape;
    Body body;
//    public HumanAi() {
//        isOnPatrol = true;
//    }

    public HumanAi() {
        this.startPosition = startPosition;
        isOnPatrol = true;
        patrolBehaviour = new PatrolBehaviour(5,1);
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

//        world.rayCast(callback, body.getPosition(), new Vector2(body.getPosition().x -5, body.getPosition().y));
//
//        if(animationComponent.spriteDirection){
//            world.rayCast(callback, body.getPosition(), new Vector2(body.getPosition().x +5, body.getPosition().y));
//        }else world.rayCast(callback, body.getPosition(), new Vector2(body.getPosition().x -5, body.getPosition().y));
        patrolBehaviour.setStartPosition(body.getPosition());
        patrolBehaviour.update(entity);



    }


    private class PatrolBehaviour {
        float time;
        float patrolRange;
        float timeToTurn;
        Vector2 startPosition;
        Vector2 targetPosition = new Vector2();
        Body aiBody;
        AnimationComponent animationComponent;
        boolean onPosition = false;

        private PatrolBehaviour(float patrolRange, float timeToTurn){
                this.patrolRange = patrolRange;
                this.timeToTurn = timeToTurn;
        }

        void setStartPosition(Vector2 vector2){

            if(this.startPosition == null){
                this.startPosition = vector2;
                targetPosition.x = startPosition.x-patrolRange;
            }

        }

        void update(Entity aiEntity){
            animationComponent = aiEntity.getComponent(AnimationComponent.class);
            aiBody = aiEntity.getComponent(BodyComponent.class).body;
            time += Gdx.graphics.getDeltaTime();
            goToPosition(targetPosition,animationComponent,aiBody);
            if((int) targetPosition.x == (int) aiBody.getPosition().x && !onPosition){

                if(animationComponent.spriteDirection){
                    targetPosition.x = targetPosition.x - patrolRange;
                }else targetPosition.x = targetPosition.x + patrolRange;

                onPosition = true;
            }else onPosition = false;

        }

        private void goToPosition(Vector2 targetPosition,AnimationComponent animationComponent, Body aiBody ){
            Vector2 aiPosition = aiBody.getPosition();
            if(Util.roundFloat(targetPosition.x,1) != Util.roundFloat(aiPosition.x,1)){
                if(targetPosition.x < aiPosition.x){
                    aiBody.setLinearVelocity(new Vector2(-1, 0));
                    animationComponent.spriteDirection = false;
                }else{
                    aiBody.setLinearVelocity(new Vector2(1, 0));
                    animationComponent.spriteDirection = true;
                }
            }else  aiBody.setLinearVelocity(new Vector2(0, 0));

//            if(targetPosition.x > aiPosition.x){
//                aiBody.setLinearVelocity(new Vector2(1, 0));
//                animationComponent.spriteDirection = true;
//            }
        }


    }



//    private void goToPosition(Vector2 targetPosition,AnimationComponent animationComponent, Body aiBody ){
//        Vector2 aiPosition = aiBody.getPosition();
//        if(targetPosition.x+ 1.5f < aiPosition.x){
//            aiBody.setLinearVelocity(new Vector2(-1, 0));
//            animationComponent.spriteDirection = false;
//        }
//        if(targetPosition.x+ 1.5f > aiPosition.x){
//            aiBody.setLinearVelocity(new Vector2(1, 0));
//            animationComponent.spriteDirection = true;
//        }
//    }


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

