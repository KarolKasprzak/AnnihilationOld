package com.cosma.annihilation.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.cosma.annihilation.Components.*;
import com.cosma.annihilation.Utils.EntityEventSignal;
import com.cosma.annihilation.Utils.Enums.BodyID;
import com.cosma.annihilation.Utils.Enums.CollisionID;
import com.cosma.annihilation.Utils.Enums.GameEvent;
import com.cosma.annihilation.Utils.StateManager;


public class CollisionSystem extends IteratingSystem implements ContactListener {

    private Signal<EntityEventSignal> signal;
    private World world;
    private Entity player;
    private float ladderX;
    private float ladderY;
    private float ladderHeight;
    private ComponentMapper<PlayerComponent> playerMapper;
    private ComponentMapper<BodyComponent> bodyMapper;
    private ComponentMapper<PlayerStateComponent> stateMapper;
    private Body playerBody;
    private PlayerStateComponent playerState;
    private Filter goTroughFilter;
    private Filter normalFilter;
    public Array<Body> bodiesToRemove;
    private EntityEventSignal entityEventSignal;


    public CollisionSystem(World world) {
        super(Family.all(PlayerComponent.class).get());
        bodyMapper = ComponentMapper.getFor(BodyComponent.class);
        playerMapper = ComponentMapper.getFor(PlayerComponent.class);
        stateMapper = ComponentMapper.getFor(PlayerStateComponent.class);
        this.world = world;

        world.setContactListener(this);
        //Filter for one way wall
        goTroughFilter = new Filter();
        goTroughFilter.maskBits = CollisionID.NO_SHADOW;
        goTroughFilter.groupIndex = -1;
        normalFilter = new Filter();
        normalFilter.categoryBits = CollisionID.NO_SHADOW;
        bodiesToRemove = new Array<Body>();

        signal = new Signal<EntityEventSignal>();
        entityEventSignal = new EntityEventSignal();


    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        player = getEngine().getEntitiesFor(Family.all(PlayerComponent.class).get()).first();
        playerBody = player.getComponent(BodyComponent.class).body;
        playerState = player.getComponent(PlayerStateComponent.class);
    }

    public void SetSignal(){
        signal.add(this.getEngine().getSystem(HealthSystem.class));
    }

    @Override
    public void update(float deltaTime) {

       playerBody = player.getComponent(BodyComponent.class).body;


           if (player.getComponent(PlayerComponent.class).numFootContacts >= 1) {
               playerState.onGround = true;
           } else  playerState.onGround = false;

           //Setting player on ladder center
           if ( playerState.climbing) {
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
           if (playerState.climbing) {
               System.out.println("1");
               if (playerBody.getPosition().y > ladderY - (ladderHeight / 2 - 2)) {
                   System.out.println("2");
                   playerBody.getFixtureList().get(0).setFilterData(goTroughFilter);
                   if (playerBody.getPosition().y < ladderY - (ladderHeight / 2 - 2)) {
                       System.out.println("3");
                       playerBody.getFixtureList().get(0).setFilterData(normalFilter);
                   }
               } else {
                   playerBody.getFixtureList().get(0).setFilterData(normalFilter);
               }
           } else {

               playerBody.getFixtureList().get(0).setFilterData(normalFilter);
           }
       }


    @Override
    protected void processEntity(Entity entity, float deltaTime) {
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        bulletCollision(fa,fb);
        bulletCollision(fb,fa);

        addEntityToActionList(fa, fb);

        //Bullet shell contact
        if(fa.getUserData() == BodyID.BULLET_SHELL ){
            getEngine().removeEntity((Entity) fa.getBody().getUserData());
            removeShellAfterTime(fa,2);
        }

        if(fb.getUserData() == BodyID.BULLET_SHELL){
            getEngine().removeEntity((Entity) fb.getBody().getUserData());
            removeShellAfterTime(fb,2);
        }

        //Player ground contact
        if (fb.getUserData() == BodyID.PLAYER_FOOT && !fa.isSensor() || fa.getUserData() == BodyID.PLAYER_FOOT && !fb.isSensor()) {
            player.getComponent(PlayerComponent.class).numFootContacts++;
        }
        //Ladder climb down
        if (fb.getUserData() == BodyID.PLAYER_CENTER && fa.getUserData() == BodyID.GROUND ||
                fa.getUserData() == BodyID.PLAYER_CENTER && fb.getUserData() == BodyID.GROUND) {
            if (playerState.climbing) {
                playerState.canMoveOnSide = false;
            }
        }

        if (fb.getUserData() == BodyID.PLAYER_FOOT && fa.getUserData() == BodyID.DESCENT_LADDER || fa.getUserData() == BodyID.PLAYER_FOOT && fb.getUserData() == BodyID.DESCENT_LADDER) {
            playerState.canClimbDown = true;
            System.out.println("truewa");
            if (fa.getUserData() == BodyID.DESCENT_LADDER) {
                ladderX = fa.getBody().getPosition().x;
                ladderY = fa.getBody().getPosition().y;
                ladderHeight = (Float) fa.getBody().getUserData();
            } else {

                ladderX = fb.getBody().getPosition().x;
                ladderY = fb.getBody().getPosition().y;
                ladderHeight = (Float) fb.getBody().getUserData();
            }

        }
        //Ladder climb up
        if (fb.getUserData() == BodyID.PLAYER_CENTER && fa.getUserData() == BodyID.LADDER ||
                fa.getUserData() == BodyID.PLAYER_CENTER && fb.getUserData() == BodyID.LADDER) {

            if (playerState.onGround) {
                playerState.canClimb = true;
                playerState.canJump = false;
            }

            if (fa.getUserData() == BodyID.LADDER) {
                ladderX = fa.getBody().getPosition().x;
                ladderY = fa.getBody().getPosition().y;
                ladderHeight = (Float) fa.getBody().getUserData();
            } else {
                ladderX = fb.getBody().getPosition().x;
                ladderY = fb.getBody().getPosition().y;
                ladderHeight = (Float) fb.getBody().getUserData();
            }
        }
    }
    @Override
    public void endContact(Contact contact) {
            Fixture fa = contact.getFixtureA();
            Fixture fb = contact.getFixtureB();

            removeEntityFromActionList(fa,fb);

            if (fb.getUserData() == BodyID.PLAYER_FOOT && !fa.isSensor() || fa.getUserData() == BodyID.PLAYER_FOOT && !fb.isSensor()) {
                playerState.onGround = false;
                player.getComponent(PlayerComponent.class).numFootContacts--;
            }

            //------------------------Ladder contacts--------------
            if (fb.getUserData() == BodyID.PLAYER_CENTER && fa.getUserData() == BodyID.GROUND ||
                    fa.getUserData() == BodyID.PLAYER_CENTER && fb.getUserData() == BodyID.GROUND) {

                playerState.canMoveOnSide = true;
                playerState.canJump = false;
                delayJump(0.4f);
            }

            if (fb.getUserData() == BodyID.PLAYER_FOOT && fa.getUserData() == BodyID.DESCENT_LADDER || fa.getUserData() == BodyID.PLAYER_FOOT && fb.getUserData() == BodyID.DESCENT_LADDER) {
                playerState.canClimbDown = false;

            }

            //Ladder climb
            if (fb.getUserData() == BodyID.PLAYER_CENTER && fa.getUserData() == BodyID.LADDER ||
                    fa.getUserData() == BodyID.PLAYER_CENTER && fb.getUserData() == BodyID.LADDER) {
                delayJump(0.4f);
                playerState.canClimb = false;
                playerState.climbing = false;
            }

        }
    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    private void removeShellAfterTime(final Fixture fixture, float delay){
        if(!fixture.getBody().isBullet()){
            fixture.getBody().setBullet(true);
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    world.destroyBody(fixture.getBody());
                }
            }, delay);
        }
    }

    private void delayJump(float delay){
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                playerState.canJump = true;
            }
        }, delay);
    }

    private void bulletCollision(Fixture fa, Fixture fb){
        if(fa.getUserData() == BodyID.BULLET ){
            if(fb.getBody().getUserData()instanceof Entity){
                Entity entity = (Entity) fa.getBody().getUserData();
                entityEventSignal.setEvent(GameEvent.OBJECT_HIT,(Entity) fb.getBody().getUserData(),entity.getComponent(BulletComponent.class).dmg,entity.getComponent(BulletComponent.class).accuracy);
                signal.dispatch(entityEventSignal);
            }
            if (!bodiesToRemove.contains(fa.getBody(),true)){
                bodiesToRemove.add(fa.getBody());
                getEngine().removeEntity((Entity) fa.getBody().getUserData());

            }
        }
    }

    private void addEntityToActionList(Fixture fa, Fixture fb){
        if(fa.getUserData() == BodyID.CONTAINER && fb.getUserData() == BodyID.PLAYER_BODY  || fb.getUserData() == BodyID.CONTAINER &&  fa.getUserData() == BodyID.PLAYER_BODY){
            if(fa.getUserData() != BodyID.PLAYER_BODY){
                if(!player.getComponent(PlayerComponent.class).collisionEntityList.contains(fa.getBody().getUserData())){
                    player.getComponent(PlayerComponent.class).collisionEntityList.add((Entity) fa.getBody().getUserData());
                }
            }else
            if(!player.getComponent(PlayerComponent.class).collisionEntityList.contains(fb.getBody().getUserData())){
                player.getComponent(PlayerComponent.class).collisionEntityList.add((Entity) fb.getBody().getUserData());
            }
        }
    }

    private void removeEntityFromActionList(Fixture fa, Fixture fb){
        if(fa.getUserData() == BodyID.CONTAINER && fb.getUserData() == BodyID.PLAYER_BODY  || fb.getUserData() == BodyID.CONTAINER &&  fa.getUserData() == BodyID.PLAYER_BODY){
            if(fa.getUserData() != BodyID.PLAYER_BODY){
                    player.getComponent(PlayerComponent.class).collisionEntityList.remove(fa.getBody().getUserData());
                Entity entity = (Entity) fa.getBody().getUserData();
                entity.getComponent(TextureComponent.class).renderWithShader = false;
            }else
                player.getComponent(PlayerComponent.class).collisionEntityList.remove(fb.getBody().getUserData());
            Entity entity = (Entity) fb.getBody().getUserData();
            entity.getComponent(TextureComponent.class).renderWithShader = false;
        }
    }
}
