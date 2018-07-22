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
import com.badlogic.gdx.utils.Timer;
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
    private float ladderY;
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
        playerBody = player.getComponent(BodyComponent.class).body;
        if (player.getComponent(PlayerComponent.class).numFootContacts >= 1) {
            StateManager.onGround = true;
        } else StateManager.onGround = false;

        //Player position on ladder
        if (StateManager.climbing) {
            float b1 = playerBody.getPosition().x;
            float b2 = ladderX;

            if (b1 == b2) {
                System.out.println((b1 + b2));
            } else {
                if (b1 < (b2 - 0.03f)) {
                    playerBody.setLinearVelocity(1, 0);
                }
                if (b1 > (b2 + 0.03f)) {
                    playerBody.setLinearVelocity(-1, 0);
                }
            }
        }


        //CORRECT THIS!
        //Player go through wall
        if (StateManager.climbing) {
              if (playerBody.getPosition().y > ladderY) {
                Filter filter = new Filter();
                filter.maskBits = CollisionID.CATEGORY_SCENERY;
                filter.groupIndex = -1;
                playerBody.getFixtureList().get(0).setFilterData(filter);
                if (playerBody.getPosition().y < ladderY) {
                    Filter filter1 = new Filter();
                    playerBody.getFixtureList().get(0).setFilterData(filter1);
                }
            } else {
                Filter filter2 = new Filter();
                playerBody.getFixtureList().get(0).setFilterData(filter2);
            }
        } else {
            Filter filter2 = new Filter();
            playerBody.getFixtureList().get(0).setFilterData(filter2);
        }
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
    }
    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if(fb.getUserData() == BodyID.PLAYER_FOOT || fa.getUserData() == BodyID.PLAYER_FOOT)  {
            player.getComponent(PlayerComponent.class).numFootContacts++;
        }
        if(fb.getUserData() == BodyID.PLAYER_CENTER && fa.getUserData() == BodyID.GROUND ||
                fa.getUserData() == BodyID.PLAYER_CENTER && fb.getUserData() == BodyID.GROUND)  {
            StateManager.canMoveOnSide = false;
        }

        if(fb.getUserData() == BodyID.PLAYER_FOOT && fa.getUserData() == BodyID.DESCENT_LADDER || fa.getUserData() == BodyID.PLAYER_FOOT && fb.getUserData() == BodyID.DESCENT_LADDER)  {
            StateManager.canClimbDown = true;
            if (fa.getUserData() == BodyID.DESCENT_LADDER) {
                ladderX = fa.getBody().getPosition().x;
            } else {
                ladderX = fb.getBody().getPosition().x;
            }
        }
        //Ladder climb
        if(fb.getUserData() == BodyID.PLAYER_CENTER && fa.getUserData() == BodyID.LADDER ||
                fa.getUserData() == BodyID.PLAYER_CENTER && fb.getUserData() == BodyID.LADDER)  {

            if (fa.getUserData() == BodyID.LADDER) {
                ladderX = fa.getBody().getPosition().x;
                ladderY = fa.getBody().getPosition().y;

            } else {
                ladderX = fb.getBody().getPosition().x;
                ladderY = fb.getBody().getPosition().y;


            }
             if(StateManager.onGround) {
                 StateManager.canClimb = true;
             }
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if(fb.getUserData() == BodyID.PLAYER_CENTER && fa.getUserData() == BodyID.GROUND ||
                fa.getUserData() == BodyID.PLAYER_CENTER && fb.getUserData() == BodyID.GROUND)  {
            StateManager.canMoveOnSide = true;
            StateManager.canJump = false;
            delayJump(0.4f);
        }

        if(fb.getUserData() == BodyID.PLAYER_FOOT && fa.getUserData() == BodyID.DESCENT_LADDER || fa.getUserData() == BodyID.PLAYER_FOOT && fb.getUserData() == BodyID.DESCENT_LADDER)  {
            StateManager.canClimbDown = false;

        }

        if(fb.getUserData() == BodyID.PLAYER_FOOT || fa.getUserData() == BodyID.PLAYER_FOOT){
            StateManager.onGround = false;
            player.getComponent(PlayerComponent.class).numFootContacts--;
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

    private void delayJump(float delay){
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                StateManager.canJump = true;
            }
        }, delay);
    }


}
