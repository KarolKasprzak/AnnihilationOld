package com.cosma.annihilation.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogic.gdx.utils.Timer;
import com.cosma.annihilation.Components.*;
import com.cosma.annihilation.Gui.PlayerGUI;
import com.cosma.annihilation.Utils.BodyID;
import com.cosma.annihilation.Utils.CollisionID;
import com.cosma.annihilation.Utils.StateManager;


public class CollisionSystem extends IteratingSystem implements ContactListener {

    private World world;
    private Family playerFamily;
    private Entity player;
    private float ladderX;
    private float ladderY;
    private float ladderHeight;
    private ComponentMapper<PlayerComponent> playerMapper;
    private ComponentMapper<BodyComponent> bodyMapper;
    private Body playerBody;
    private Filter goTroughFilter;
    private Filter normalFilter;


    public CollisionSystem(World world) {
        super(Family.all(PlayerComponent.class).get());
        bodyMapper = ComponentMapper.getFor(BodyComponent.class);
        playerMapper = ComponentMapper.getFor(PlayerComponent.class);
        this.world = world;
        world.setContactListener(this);
        //Filter for one way wall
        goTroughFilter = new Filter();
        goTroughFilter.maskBits = CollisionID.CATEGORY_SCENERY;
        goTroughFilter.groupIndex = -1;

        normalFilter = new Filter();
        normalFilter.categoryBits = CollisionID.NO_SHADOW;

    }

    @Override
    public void update(float deltaTime) {
       if(!StateManager.pause) {
           player = getEngine().getEntitiesFor(Family.all(PlayerComponent.class).get()).first();
           playerBody = player.getComponent(BodyComponent.class).body;
           if (player.getComponent(PlayerComponent.class).numFootContacts >= 1) {
               StateManager.onGround = true;
           } else StateManager.onGround = false;

           //Setting player on ladder center
           if (StateManager.climbing) {
               float b1 = playerBody.getPosition().x;
               float b2 = ladderX;

               if (b1 == b2) {
               } else {
                   if (b1 < (b2 - 0.03f)) {
                       playerBody.setLinearVelocity(1, 0);
                   }
                   if (b1 > (b2 + 0.03f)) {
                       playerBody.setLinearVelocity(-1, 0);
                   }
               }
           }
           //Player go through wall
           if (StateManager.climbing) {
               if (playerBody.getPosition().y > ladderY - (ladderHeight / 2 - 2)) {
                   playerBody.getFixtureList().get(0).setFilterData(goTroughFilter);
                   if (playerBody.getPosition().y < ladderY - (ladderHeight / 2 - 2)) {

                       playerBody.getFixtureList().get(0).setFilterData(normalFilter);
                   }
               } else {
                   playerBody.getFixtureList().get(0).setFilterData(normalFilter);
               }
           } else {

               playerBody.getFixtureList().get(0).setFilterData(normalFilter);
           }
       }
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
    }
    @Override
    public void beginContact(Contact contact) {





        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        //TODO
        //-----------------------Interaction-----------------------------//
        if(fa.getUserData() == BodyID.PLAYER_BODY && fb.getUserData() == BodyID.CONTAINER
                || fb.getUserData() == BodyID.PLAYER_BODY && fa.getUserData() == BodyID.CONTAINER)  {
           if(fa.getUserData() == BodyID.CONTAINER){
               player.getComponent(PlayerComponent.class).collisionEntity = (Entity) fa.getBody().getUserData();
            }else{
               player.getComponent(PlayerComponent.class).collisionEntity = (Entity) fb.getBody().getUserData();
           }
        }

        //-----------------------Jumping & Climb contacts-----------------------------
        if(fb.getUserData() == BodyID.PLAYER_FOOT && !fa.isSensor() || fa.getUserData() == BodyID.PLAYER_FOOT && !fb.isSensor())  {
            player.getComponent(PlayerComponent.class).numFootContacts++;
        }
        //Ladder climb down
        if(fb.getUserData() == BodyID.PLAYER_CENTER && fa.getUserData() == BodyID.GROUND ||
                fa.getUserData() == BodyID.PLAYER_CENTER && fb.getUserData() == BodyID.GROUND)  {
            if(StateManager.climbing) {
                StateManager.canMoveOnSide = false;
            }
        }

        if(fb.getUserData() == BodyID.PLAYER_FOOT && fa.getUserData() == BodyID.DESCENT_LADDER || fa.getUserData() == BodyID.PLAYER_FOOT && fb.getUserData() == BodyID.DESCENT_LADDER)  {
            StateManager.canClimbDown = true;
            if (fa.getUserData() == BodyID.DESCENT_LADDER) {
                ladderX = fa.getBody().getPosition().x;
                ladderY = fa.getBody().getPosition().y;
                ladderHeight = (Float) fa.getBody().getUserData();
            }
                else {

                    ladderX = fb.getBody().getPosition().x;
                    ladderY = fb.getBody().getPosition().y;
                    ladderHeight = (Float) fb.getBody().getUserData();
                    }

        }
        //Ladder climb up
        if(fb.getUserData() == BodyID.PLAYER_CENTER && fa.getUserData() == BodyID.LADDER ||
                fa.getUserData() == BodyID.PLAYER_CENTER && fb.getUserData() == BodyID.LADDER)  {

            if(StateManager.onGround) {
                StateManager.canClimb = true;
                StateManager.canJump = false;}

            if (fa.getUserData() == BodyID.LADDER) {
                ladderX = fa.getBody().getPosition().x;
                ladderY = fa.getBody().getPosition().y;
                ladderHeight = (Float) fa.getBody().getUserData();}
                else {
                    ladderX = fb.getBody().getPosition().x;
                    ladderY = fb.getBody().getPosition().y;
                    ladderHeight = (Float) fb.getBody().getUserData();}
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        //TODO
        //-----------------------Interaction-----------------------------//
        if(fa.getUserData() == BodyID.PLAYER_BODY && fb.getUserData() == BodyID.CONTAINER
                || fb.getUserData() == BodyID.PLAYER_BODY && fa.getUserData() == BodyID.CONTAINER)  {
                player.getComponent(PlayerComponent.class).collisionEntity = null;
        }




        //-----------------------Jumping-----------------------------
        if(fb.getUserData() == BodyID.PLAYER_FOOT && !fa.isSensor() || fa.getUserData() == BodyID.PLAYER_FOOT && !fb.isSensor()) {
            StateManager.onGround = false;
            player.getComponent(PlayerComponent.class).numFootContacts--;
        }

        //------------------------Ladder contacts--------------
        if(fb.getUserData() == BodyID.PLAYER_CENTER && fa.getUserData() == BodyID.GROUND ||
                fa.getUserData() == BodyID.PLAYER_CENTER && fb.getUserData() == BodyID.GROUND)  {

            StateManager.canMoveOnSide = true;
            StateManager.canJump = false;
            delayJump(0.4f);
        }

        if(fb.getUserData() == BodyID.PLAYER_FOOT && fa.getUserData() == BodyID.DESCENT_LADDER || fa.getUserData() == BodyID.PLAYER_FOOT && fb.getUserData() == BodyID.DESCENT_LADDER)  {
            StateManager.canClimbDown = false;

        }


        //Ladder climb
        if(fb.getUserData() == BodyID.PLAYER_CENTER && fa.getUserData() == BodyID.LADDER ||
            fa.getUserData() == BodyID.PLAYER_CENTER && fb.getUserData() == BodyID.LADDER){
            delayJump(0.4f);
            StateManager.canClimb = false;
            StateManager.climbing = false;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
    //Action delay method
    private void delayJump(float delay){
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                StateManager.canJump = true;
            }
        }, delay);
    }


}
