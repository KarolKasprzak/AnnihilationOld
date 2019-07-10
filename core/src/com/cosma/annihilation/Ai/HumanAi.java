package com.cosma.annihilation.Ai;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;
import com.cosma.annihilation.Annihilation;
import com.cosma.annihilation.Components.*;
import com.cosma.annihilation.Utils.Animation.AnimationStates;
import com.cosma.annihilation.Utils.Util;


public class HumanAi implements ArtificialIntelligence {
    private Vector2 startPosition;
    private String aiStatus = "";
    private PatrolBehaviour patrolBehaviour;


    public HumanAi(Vector2 startPosition) {
        this.startPosition = startPosition;
        patrolBehaviour = new PatrolBehaviour(5, 1);
        patrolBehaviour.setStartPosition(startPosition);

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
        Vector2 startPosition;
        Vector2 targetPosition = new Vector2();
        Body aiBody;
        AnimationComponent animationComponent;
        boolean onPosition = false;
        RayCastCallback frontRayCallback_SHOOT, frontRayCallback;
        Body targetBody;
        Entity targetEntity;
        Vector2 raycastEnd = new Vector2();


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

        void setStartPosition(Vector2 vector2) {

            if (this.startPosition == null) {
                this.startPosition = vector2;
                targetPosition.x = startPosition.x - patrolRange;
            }

        }

        void update(Entity aiEntity) {
            animationComponent = aiEntity.getComponent(AnimationComponent.class);
            HealthComponent healthComponent = aiEntity.getComponent(HealthComponent.class);
            aiBody = aiEntity.getComponent(BodyComponent.class).body;
            time += Gdx.graphics.getDeltaTime();
            World world = aiBody.getWorld();


            if (!isBlocked) {
                if (animationComponent.spriteDirection) {
                    world.rayCast(frontRayCallback, aiBody.getPosition(), new Vector2(aiBody.getPosition().x + 4, aiBody.getPosition().y));
                } else
                    world.rayCast(frontRayCallback, aiBody.getPosition(), new Vector2(aiBody.getPosition().x - 4, aiBody.getPosition().y));

                if (!isEnemySpotted) {
                    goToPosition(targetPosition, animationComponent, aiBody);
                    aiStatus = "on patrol";
                    animationComponent.animationState = AnimationStates.WALK;
                    if ((int) targetPosition.x == (int) aiBody.getPosition().x && !onPosition) {

                        if (animationComponent.spriteDirection) {
                            targetPosition.x = targetPosition.x - patrolRange;
                        } else targetPosition.x = targetPosition.x + patrolRange;
                        onPosition = true;
                    } else onPosition = false;
                } else {
                    aiStatus = "find enemy";
                    animationComponent.animationState = AnimationStates.IDLE;
                    aiBody.setLinearVelocity(0, aiBody.getLinearVelocity().y);
                    shoot(animationComponent, aiBody);
                }
                isEnemySpotted = false;
            }

    }

    private void goToPosition(Vector2 targetPosition, AnimationComponent animationComponent, Body aiBody) {
        Vector2 aiPosition = aiBody.getPosition();
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

    private void shoot(AnimationComponent animationComponent, Body aiBody) {
        World world = aiBody.getWorld();
        world.rayCast(frontRayCallback, aiBody.getPosition(), raycastEnd.set(aiBody.getPosition().x + 15, aiBody.getPosition().y));
        if (targetEntity != null) {
            System.out.println("hit");
            targetEntity.getComponent(HealthComponent.class).hp -= 5;
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

