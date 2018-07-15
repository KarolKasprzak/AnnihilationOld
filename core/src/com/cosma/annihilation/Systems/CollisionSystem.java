package com.cosma.annihilation.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.cosma.annihilation.Components.BodyComponent;
import com.cosma.annihilation.Components.PlayerComponent;
import com.cosma.annihilation.Components.TransformComponent;
import com.cosma.annihilation.Utils.BodyID;
import com.cosma.annihilation.Utils.CollisionID;
import com.cosma.annihilation.Utils.StateManager;


public class CollisionSystem extends IteratingSystem implements ContactListener {

    private World world;
    private Family playerFamily;
    private Entity player;
    private float ladderX;
    private ComponentMapper<PlayerComponent> playerMapper = ComponentMapper.getFor(PlayerComponent.class);
    private ComponentMapper<BodyComponent> bodyMapper = ComponentMapper.getFor(BodyComponent.class);
    private ComponentMapper<TransformComponent> transformMapper = ComponentMapper.getFor(TransformComponent.class);
    private Body playerBody;

    public CollisionSystem(World world) {
        // System for all Entities that have B2dBodyComponent and TransformComponent
        super(Family.all(PlayerComponent.class).get());
        bodyMapper = ComponentMapper.getFor(BodyComponent.class);
        playerMapper = ComponentMapper.getFor(PlayerComponent.class);
        this.world = world;
        world.setContactListener(this);

    }
    @Override
    public void update(float deltaTime) {
        player = getEngine().getEntitiesFor(Family.all(PlayerComponent.class).get()).first();
        playerBody =  player.getComponent(BodyComponent.class).body;

        if(StateManager.climbing && StateManager.canClimb) {
            float b1 = playerBody.getPosition().x;
            float b2 = ladderX;

            if (b1 == b2) {
                System.out.println((b1 + b2));
            } else {
                if (b1 < (b2 - 0.05f)) {
                    playerBody.setLinearVelocity(1, 0);
                }
                if (b1 > (b2 + 0.05f)) {
                    playerBody.setLinearVelocity(-1, 0);
                }
            }
        }
//        System.out.println(playerBody.getFixtureList().get(0));
    }
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
    }
    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();





        if(fb.getUserData() == BodyID.PLAYER_FOOT || fa.getUserData() == BodyID.PLAYER_FOOT)  {
            StateManager.onGround = true;
        }
        //Ladder climb
        if(fb.getUserData() == BodyID.PLAYER_CENTER && fa.getUserData() == BodyID.LADDER ||
                fa.getUserData() == BodyID.PLAYER_CENTER && fb.getUserData() == BodyID.LADDER)  {
                StateManager.canJump = false;
                if(StateManager.onGround)
                    StateManager.canClimb = true;
                    if (fa.getUserData() == BodyID.LADDER) {
                        ladderX = fa.getBody().getPosition().x;
                    } else {
                        ladderX = fb.getBody().getPosition().x;
                    }

        }
        if(StateManager.climbing) {
        if(fb.getUserData() == BodyID.PLAYER_CENTER && fa.getUserData() == BodyID.DESCENT || fa.getUserData() == BodyID.PLAYER_CENTER && fb.getUserData() == BodyID.DESCENT ){
            System.out.println("start");


                Filter filter = new Filter();
                filter.maskBits = CollisionID.CATEGORY_SCENERY;
                filter.groupIndex = -1;
                playerBody.getFixtureList().get(0).setFilterData(filter);
            }
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();
        if(fb.getUserData() == BodyID.PLAYER_FOOT || fa.getUserData() == BodyID.PLAYER_FOOT){
            StateManager.onGround = false;
        }
        //Ladder climb
        if(fb.getUserData() == BodyID.PLAYER_CENTER && fa.getUserData() == BodyID.LADDER ||
            fa.getUserData() == BodyID.PLAYER_CENTER && fb.getUserData() == BodyID.LADDER){
            StateManager.canJump = true;
            StateManager.canClimb = false;
        }



        if(fb.getUserData() == BodyID.PLAYER_CENTER && fa.getUserData() == BodyID.DESCENT || fa.getUserData() == BodyID.PLAYER_CENTER && fb.getUserData() == BodyID.DESCENT ){

                   Filter filter = new Filter();
                   System.out.println("end");
                   playerBody.getFixtureList().get(0).setFilterData(filter);

        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

        Body playerBody =  player.getComponent(BodyComponent.class).body;

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }



}
