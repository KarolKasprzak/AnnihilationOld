package com.cosma.annihilation.Ai;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.utils.Timer;
import com.cosma.annihilation.Annihilation;
import com.cosma.annihilation.Components.*;
import com.cosma.annihilation.Utils.Animation.AnimationStates;
import com.cosma.annihilation.Utils.Util;

import java.util.ArrayList;


public class HumanAi implements ArtificialIntelligence {
    private Vector2 startPosition;
    private String aiStatus = "";
    private PatrolBehaviour patrolBehaviour;
    private ArrayList<Task> taskList;


    public HumanAi(Vector2 startPosition) {
        this.startPosition = startPosition;
        patrolBehaviour = new PatrolBehaviour(5, 1);
        patrolBehaviour.setNpcStartPosition(startPosition);
        taskList = new ArrayList<>();


    }

    @Override
    public void update(Entity entity) {
        patrolBehaviour.update(entity);
    }

    @Override
    public String getStatus() {
        return aiStatus;
    }


    private class PatrolBehaviour {
        float time;
        float patrolRange;
        float timeToTurn;
        boolean isBlocked = false;
        boolean isEnemySpotted = false;
        Vector2 npcStartPosition,targetPosition = new Vector2(),npcTargetPosition = new Vector2(),raycastEnd = new Vector2();
        Body aiBody;
        AnimationComponent animationComponent;
        boolean onPosition = false;
        RayCastCallback frontRayCallback_SHOOT, frontRayCallback;
        Body targetBody;
        Entity targetEntity;

        private PatrolBehaviour(float patrolRange, float timeToTurn) {
            this.patrolRange = patrolRange;
            this.timeToTurn = timeToTurn;

            frontRayCallback_SHOOT = new RayCastCallback() {
                @Override
                public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                    if (fixture.getBody().getUserData() instanceof Entity && ((Entity) fixture.getBody().getUserData()).getComponent(HealthComponent.class) != null) {
                        targetEntity = (Entity) fixture.getBody().getUserData();
                    } else targetEntity = null;
                    return 0;
                }
            };

            frontRayCallback = new RayCastCallback() {
                @Override
                public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                    if (fixture.getBody().getUserData() instanceof Entity && ((Entity) fixture.getBody().getUserData()).getComponent(PlayerComponent.class) != null) {
                        isEnemySpotted = true;
                        targetBody = fixture.getBody();
                        System.out.println(isEnemySpotted);
                        return 0;
                    }
                    return 0;
                }
            };
        }

        void setNpcStartPosition(Vector2 vector2) {
            if (this.npcStartPosition == null) {
                this.npcStartPosition = vector2;
                targetPosition.x = npcStartPosition.x - patrolRange;
            }
        }

        void update(Entity aiEntity) {
            animationComponent = aiEntity.getComponent(AnimationComponent.class);
            HealthComponent healthComponent = aiEntity.getComponent(HealthComponent.class);
            aiBody = aiEntity.getComponent(BodyComponent.class).body;
            time += Gdx.graphics.getDeltaTime();
            World world = aiBody.getWorld();

            if (!isBlocked) {
                //Search enemy
                if (animationComponent.spriteDirection) {
                    world.rayCast(frontRayCallback, aiBody.getPosition(), new Vector2(aiBody.getPosition().x + 7, aiBody.getPosition().y));
                } else
                    world.rayCast(frontRayCallback, aiBody.getPosition(), new Vector2(aiBody.getPosition().x - 7, aiBody.getPosition().y));
                //If found
                if(isEnemySpotted){
                    if(isEnemyInRange(world,animationComponent)){
                        aiStatus = "shoot mode";
                        aiBody.setLinearVelocity(0, aiBody.getLinearVelocity().y);
                        shoot(animationComponent,aiBody);
                    }else{
                        aiStatus = "follow mode";
                        followEnemy(aiBody,animationComponent);
                    }
                }else{
                    goToPosition(targetPosition, animationComponent, aiBody);
                    aiStatus = "on patrol";
                    animationComponent.animationState = AnimationStates.WALK;
                    if ((int) targetPosition.x == (int) aiBody.getPosition().x && !onPosition) {

                        if (animationComponent.spriteDirection) {
                            targetPosition.x = targetPosition.x - patrolRange;
                        } else targetPosition.x = targetPosition.x + patrolRange;
                        onPosition = true;
                    } else onPosition = false;
                }
                isEnemySpotted = false;


//                if (!isEnemySpotted) {
//                    goToPosition(targetPosition, animationComponent, aiBody);
//                    aiStatus = "on patrol";
//                    animationComponent.animationState = AnimationStates.WALK;
//                    if ((int) targetPosition.x == (int) aiBody.getPosition().x && !onPosition) {
//
//                        if (animationComponent.spriteDirection) {
//                            targetPosition.x = targetPosition.x - patrolRange;
//                        } else targetPosition.x = targetPosition.x + patrolRange;
//                        onPosition = true;
//                    } else onPosition = false;
//                } else {
//                    aiStatus = "find enemy";
//                    animationComponent.animationState = AnimationStates.IDLE;
//                    aiBody.setLinearVelocity(0, aiBody.getLinearVelocity().y);
//                    shoot(animationComponent, aiBody);
//                }
//                isEnemySpotted = false;
            }

    }



    private boolean isEnemyInRange(World world,AnimationComponent animationComponent){
        if (animationComponent.spriteDirection) {
            world.rayCast(frontRayCallback_SHOOT, aiBody.getPosition(), raycastEnd.set(aiBody.getPosition().x + 4, aiBody.getPosition().y));
            return targetEntity != null;
        } else{
            world.rayCast(frontRayCallback_SHOOT, aiBody.getPosition(), raycastEnd.set(aiBody.getPosition().x - 4, aiBody.getPosition().y));
            return targetEntity != null;
        }
    }

    private void goToPosition(Vector2 targetPosition, AnimationComponent animationComponent, Body aiBody) {
        Vector2 aiPosition = aiBody.getPosition();
        animationComponent.animationState = AnimationStates.WALK;
        if (Util.roundFloat(targetPosition.x, 1) != Util.roundFloat(aiPosition.x, 1)) {
            if (targetPosition.x < aiPosition.x) {
                aiBody.setLinearVelocity(new Vector2(-1, 0));
                animationComponent.spriteDirection = false;
            } else {
                aiBody.setLinearVelocity(new Vector2(1, 0));
                animationComponent.spriteDirection = true;
            }
        } else aiBody.setLinearVelocity(new Vector2(0, 0));
    }

    private void followEnemy(Body aiBody,AnimationComponent animationComponent){
            if(startPosition.x +10 >= aiBody.getPosition().x ){
                goToPosition(targetBody.getPosition(),animationComponent,aiBody);
            }
    }

    private void shoot(AnimationComponent animationComponent, Body aiBody) {
        World world = aiBody.getWorld();
        world.rayCast(frontRayCallback_SHOOT, aiBody.getPosition(), raycastEnd.set(aiBody.getPosition().x + 15, aiBody.getPosition().y));
        if (targetEntity != null) {
            System.out.println("hit");
            targetEntity.getComponent(HealthComponent.class).hp -= 20;
            targetEntity.getComponent(HealthComponent.class).isHit = true;
            targetEntity.getComponent(HealthComponent.class).attackerPosition = aiBody.getPosition();
        }
        isBlocked = true;
        animationComponent.time = 0;
        animationComponent.animationState = AnimationStates.NPC_SHOOT;
        float animationTimer = animationComponent.animationMap.get(AnimationStates.NPC_SHOOT.toString()).getAnimationDuration();

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                isBlocked = false;
            }
        }, animationTimer + 0.3f);
        targetEntity = null;
        Sound sound = Annihilation.getAssets().get("sfx/cg1.wav");
        sound.play();
    }
}
}

