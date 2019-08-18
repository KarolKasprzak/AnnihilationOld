package com.cosma.annihilation.Ai;

import com.badlogic.ashley.core.Entity;
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

/**
 * Core class to build npc ai
 */

public class AiCore {

    private RayCastCallback sightRayCallback, shootRayCallback;
    private Vector2 rayEndVector = new Vector2();
    private Vector2 destinationPosition = new Vector2();
    private boolean isEnemySpotted = false;
    private boolean isBusy = false;
    private boolean isAttacked = false;
    private Body enemyBody;
    private Entity targetEntity;



    AiCore() {

        shootRayCallback = new RayCastCallback() {
            @Override
            public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                if (fixture.getBody().getUserData() instanceof Entity && ((Entity) fixture.getBody().getUserData()).getComponent(HealthComponent.class) != null) {
                    targetEntity = (Entity) fixture.getBody().getUserData();
                } else targetEntity = null;
                return 0;
            }
        };

        sightRayCallback = new RayCastCallback() {
            @Override
            public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                if (fixture.getBody().getUserData() instanceof Entity && ((Entity) fixture.getBody().getUserData()).getComponent(PlayerComponent.class) != null) {
                    isEnemySpotted = true;
                    enemyBody = fixture.getBody();
                    return 0;
                }
                return 0;
            }
        };
    }

    void isHearEnemy(Entity entity){
        AiComponent aiComponent = entity.getComponent(AiComponent.class);
        if(aiComponent.isHearEnemy){
            goToPosition(aiComponent.enemyPosition,entity);

            aiComponent.isHearEnemy = false;
        }
    }



    boolean isEnemyInSight(Entity entity) {
        AnimationComponent animationComponent = entity.getComponent(AnimationComponent.class);
        Body aiBody = entity.getComponent(BodyComponent.class).body;
        HealthComponent healthComponent = entity.getComponent(HealthComponent.class);
        if(healthComponent.isHit){

        }

        World world = aiBody.getWorld();
        if (!isBusy) {
            if (animationComponent.spriteDirection) {
                isEnemySpotted = false;
                enemyBody = null;
                world.rayCast(sightRayCallback, aiBody.getPosition(), rayEndVector.set(aiBody.getPosition().x + 7, aiBody.getPosition().y));

            } else {
                isEnemySpotted = false;
                enemyBody = null;
                world.rayCast(sightRayCallback, aiBody.getPosition(), rayEndVector.set(aiBody.getPosition().x - 7, aiBody.getPosition().y));
            }
            if (isEnemySpotted) {
                return true;
            }
        }
        return false;
    }

    boolean isEnemyInWeaponRange(Entity entity, float range) {
        if (!isBusy) {
            AnimationComponent animationComponent = entity.getComponent(AnimationComponent.class);
            Body aiBody = entity.getComponent(BodyComponent.class).body;
            World world = aiBody.getWorld();
            if (animationComponent.spriteDirection) {
                world.rayCast(shootRayCallback, aiBody.getPosition(), rayEndVector.set(aiBody.getPosition().x + range, aiBody.getPosition().y));
                return targetEntity != null;
            } else {
                world.rayCast(shootRayCallback, aiBody.getPosition(), rayEndVector.set(aiBody.getPosition().x - range, aiBody.getPosition().y));
                return targetEntity != null;
            }
        }
       return false;
    }

    void followEnemy(Entity entity) {
        if (!isBusy) {
            if (isEnemySpotted) {
                Body aiBody = entity.getComponent(BodyComponent.class).body;
                AiComponent aiComponent = entity.getComponent(AiComponent.class);
                if (aiComponent.startPosition.x + 7 >= aiBody.getPosition().x) {
                    goToPosition(enemyBody.getPosition(), entity);
                }
            }
        }
    }

    void goToPosition(Vector2 targetPosition, Entity entity) {
        AnimationComponent animationComponent = entity.getComponent(AnimationComponent.class);
        Body aiBody = entity.getComponent(BodyComponent.class).body;
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

    void shoot(Entity entity) {
        if (!isBusy) {
            AnimationComponent animationComponent = entity.getComponent(AnimationComponent.class);
            Body aiBody = entity.getComponent(BodyComponent.class).body;
            World world = aiBody.getWorld();
            System.out.println( aiBody.getPosition().add(15, 0));
            System.out.println( aiBody.getPosition());
            world.rayCast(shootRayCallback, aiBody.getPosition(),  rayEndVector.set(aiBody.getPosition().x + 10 , aiBody.getPosition().y));
            if (targetEntity != null) {
                System.out.println("hit");
                targetEntity.getComponent(HealthComponent.class).hp -= 0;
                targetEntity.getComponent(HealthComponent.class).isHit = true;
                targetEntity.getComponent(HealthComponent.class).attackerPosition = aiBody.getPosition();
            }
            isBusy = true;
            animationComponent.time = 0;
            animationComponent.animationState = AnimationStates.NPC_SHOOT;
            float animationTimer = animationComponent.animationMap.get(AnimationStates.NPC_SHOOT.toString()).getAnimationDuration();

            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    isBusy = false;
                }
            }, animationTimer + 0.3f);
            targetEntity = null;
            Sound sound = Annihilation.getAssets().get("sfx/cg1.wav");
            sound.play();
        }
    }

    void patrol(Entity entity) {
        if (!isBusy) {
            AnimationComponent animationComponent = entity.getComponent(AnimationComponent.class);
            Body aiBody = entity.getComponent(BodyComponent.class).body;
            AiComponent aiComponent = entity.getComponent(AiComponent.class);
            Vector2 startPosition = aiComponent.startPosition;
            World world = aiBody.getWorld();

            if (aiBody.getPosition().x > startPosition.x + aiComponent.patrolRange || aiBody.getPosition().x < startPosition.x - aiComponent.patrolRange) {
                destinationPosition.set(startPosition.x, 0);
                goToPosition(destinationPosition, entity);
            }

            if (animationComponent.spriteDirection && aiBody.getPosition().x < startPosition.x + aiComponent.patrolRange && aiBody.getPosition().x > startPosition.x - aiComponent.patrolRange) {
                destinationPosition.set(startPosition.x + aiComponent.patrolRange, 0);
                goToPosition(destinationPosition, entity);
            }
            if (!animationComponent.spriteDirection && aiBody.getPosition().x < startPosition.x + aiComponent.patrolRange && aiBody.getPosition().x > startPosition.x - aiComponent.patrolRange) {
                destinationPosition.set(startPosition.x - aiComponent.patrolRange, 0);
                goToPosition(destinationPosition, entity);
            }
            if (animationComponent.spriteDirection && Util.roundFloat(aiBody.getPosition().x, 1) == Util.roundFloat(startPosition.x + aiComponent.patrolRange - 1, 1)) {
                destinationPosition.set(startPosition.x - aiComponent.patrolRange, 0);
                goToPosition(destinationPosition, entity);
            }
            if (!animationComponent.spriteDirection && Util.roundFloat(aiBody.getPosition().x, 1) == Util.roundFloat(startPosition.x - aiComponent.patrolRange, 1)) {
                destinationPosition.set(startPosition.x + aiComponent.patrolRange, 0);
                goToPosition(destinationPosition, entity);
            }
        }
    }
}