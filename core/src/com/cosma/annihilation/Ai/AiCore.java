package com.cosma.annihilation.Ai;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.cosma.annihilation.Components.AiComponent;
import com.cosma.annihilation.Components.AnimationComponent;
import com.cosma.annihilation.Components.BodyComponent;
import com.cosma.annihilation.Components.PlayerComponent;
import com.cosma.annihilation.Utils.Animation.AnimationStates;
import com.cosma.annihilation.Utils.Util;

/**
 * Core class to build npc ai
 */

public class AiCore {

    private RayCastCallback sightRayCallback;
    private Vector2 rayEndVector = new Vector2();
    private Vector2 destinationPosition = new Vector2();
    private boolean isEnemySpotted = false;
    private boolean onPosition = false;

    AiCore() {

        System.out.println("start Ai");
        System.out.println(destinationPosition.x);
        sightRayCallback = new RayCastCallback() {
            @Override
            public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                if (fixture.getBody().getUserData() instanceof Entity && ((Entity) fixture.getBody().getUserData()).getComponent(PlayerComponent.class) != null) {
                    isEnemySpotted = true;
                    return 0;
                }
                return 0;
            }
        };
    }

    boolean isPlayerInSight(Entity entity) {
        AnimationComponent animationComponent = entity.getComponent(AnimationComponent.class);
        Body aiBody = entity.getComponent(BodyComponent.class).body;
        World world = aiBody.getWorld();
        if(animationComponent.spriteDirection){
            world.rayCast(sightRayCallback, aiBody.getPosition(),rayEndVector.set(aiBody.getPosition().x + 7, aiBody.getPosition().y));

        }else{
            world.rayCast(sightRayCallback, aiBody.getPosition(),rayEndVector.set(aiBody.getPosition().x - 7, aiBody.getPosition().y));
        }
        if(isEnemySpotted){
            isEnemySpotted = false;
            System.out.println("found enemy");
            return true;
        }
        return false;
    }


    private void goToPosition(Vector2 targetPosition, Entity entity) {
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

    public void patrol(Entity entity){
        AnimationComponent animationComponent = entity.getComponent(AnimationComponent.class);
        Body aiBody = entity.getComponent(BodyComponent.class).body;
        AiComponent aiComponent = entity.getComponent(AiComponent.class);
        World world = aiBody.getWorld();

        goToPosition(destinationPosition,entity);

        System.out.println("body: " +(int) aiBody.getPosition().x +  "  destiny: " + (int) destinationPosition.x );

        if ((int) destinationPosition.x == (int) aiBody.getPosition().x){

            System.out.println(destinationPosition.x);
            if (animationComponent.spriteDirection) {
                destinationPosition.x = destinationPosition.x - aiComponent.patrolRange;
                System.out.println("fdsa");
            } else {destinationPosition.x = destinationPosition.x + aiComponent.patrolRange;}
            onPosition = true;
            System.out.println(destinationPosition.x);
            System.out.println(onPosition);
        } else onPosition = false;

    }
}