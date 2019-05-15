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
import com.cosma.annihilation.Utils.Constants;
import com.cosma.annihilation.Utils.EntityEventSignal;
import com.cosma.annihilation.Utils.Enums.AnimationStates;
import com.cosma.annihilation.Utils.Enums.BodyID;
import com.cosma.annihilation.Utils.Enums.CollisionID;
import com.cosma.annihilation.Utils.Enums.GameEvent;


public class CollisionSystem extends IteratingSystem implements ContactListener {

    private Signal<EntityEventSignal> signal;
    private World world;
    private Entity player;
    private float ladderX;
    private float ladderY;
    private float ladderHeight;
    private ComponentMapper<PlayerComponent> playerMapper;
    private ComponentMapper<BodyComponent> bodyMapper;
    private ComponentMapper<AnimationComponent> animationMapper;
    private Body playerBody;
    private PlayerComponent playerComponent;
    private AnimationComponent animationComponent;
    private Filter goTroughFilter;
    private Filter normalFilter;
    public Array<Body> bodiesToRemove;
    private EntityEventSignal entityEventSignal;


    public CollisionSystem(World world) {
        super(Family.all(PlayerComponent.class).get(),Constants.PHYSIC_SYSTEM);
        bodyMapper = ComponentMapper.getFor(BodyComponent.class);
        playerMapper = ComponentMapper.getFor(PlayerComponent.class);
        animationMapper = ComponentMapper.getFor(AnimationComponent.class);
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
        playerComponent = player.getComponent(PlayerComponent.class);
        animationComponent = player.getComponent(AnimationComponent.class);
    }

    public void addListenerSystems(){
        signal.add(this.getEngine().getSystem(HealthSystem.class));
    }

    @Override
    public void update(float deltaTime) {

       playerBody = player.getComponent(BodyComponent.class).body;


           if(player.getComponent(PlayerComponent.class).numFootContacts >= 1) {
               playerComponent.onGround = true;

//               playerComponent.canMoveOnSide = true;
           } else {
               playerComponent.onGround = false;
               animationComponent.animationState = AnimationStates.JUMP;

//

           }

           //Setting player on ladder center
           if ( playerComponent.climbing) {
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
           if (playerComponent.climbing) {
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

            removeShellAfterTime(fa,3);
        }

        if(fb.getUserData() == BodyID.BULLET_SHELL){

            removeShellAfterTime(fb,3);
        }

        //Player ground contact
        if (fb.getUserData() == BodyID.PLAYER_FOOT && !fa.isSensor() || fa.getUserData() == BodyID.PLAYER_FOOT && !fb.isSensor()) {
            player.getComponent(PlayerComponent.class).numFootContacts++;
        }
        //Ladder climb down
        if (fb.getUserData() == BodyID.PLAYER_CENTER && fa.getUserData() == BodyID.GROUND ||
                fa.getUserData() == BodyID.PLAYER_CENTER && fb.getUserData() == BodyID.GROUND) {
            if (playerComponent.climbing) {
                playerComponent.canMoveOnSide = false;
            }
        }

        if (fb.getUserData() == BodyID.PLAYER_FOOT && fa.getUserData() == BodyID.DESCENT_LADDER || fa.getUserData() == BodyID.PLAYER_FOOT && fb.getUserData() == BodyID.DESCENT_LADDER) {
            playerComponent.canClimbDown = true;
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

            if (playerComponent.onGround) {
                playerComponent.canClimb = true;
                playerComponent.canJump = false;
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

            //Player ground contact
            if (fb.getUserData() == BodyID.PLAYER_FOOT && !fa.isSensor() || fa.getUserData() == BodyID.PLAYER_FOOT && !fb.isSensor()) {
                playerComponent.onGround = false;
                player.getComponent(PlayerComponent.class).numFootContacts--;
            }

            //------------------------Ladder contacts--------------
            if (fb.getUserData() == BodyID.PLAYER_CENTER && fa.getUserData() == BodyID.GROUND ||
                    fa.getUserData() == BodyID.PLAYER_CENTER && fb.getUserData() == BodyID.GROUND) {

                playerComponent.canMoveOnSide = true;
                playerComponent.canJump = false;
                delayJump(0.4f);
            }

            if (fb.getUserData() == BodyID.PLAYER_FOOT && fa.getUserData() == BodyID.DESCENT_LADDER || fa.getUserData() == BodyID.PLAYER_FOOT && fb.getUserData() == BodyID.DESCENT_LADDER) {
                playerComponent.canClimbDown = false;

            }

            //Ladder climb
            if (fb.getUserData() == BodyID.PLAYER_CENTER && fa.getUserData() == BodyID.LADDER ||
                    fa.getUserData() == BodyID.PLAYER_CENTER && fb.getUserData() == BodyID.LADDER) {
                delayJump(0.4f);
                playerComponent.canClimb = false;
                playerComponent.climbing = false;
            }

        }
    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    private void removeShellAfterTime(final Fixture fixture, float delay){
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                        if(fixture.getBody().getUserData() != null){
                            getEngine().removeEntity((Entity) fixture.getBody().getUserData());
                        }



                }
            }, delay);

    }

    private void delayJump(float delay){
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                playerComponent.canJump = true;
            }
        }, delay);
    }

    private void bulletCollision(Fixture fa, Fixture fb){
        if(fa.getUserData() == BodyID.BULLET ){
            if(fb.getBody().getUserData()instanceof Entity){
                Entity entity = (Entity) fa.getBody().getUserData();
                entityEventSignal.setEvent(GameEvent.OBJECT_HIT,(Entity) fb.getBody().getUserData(),entity.getComponent(BulletComponent.class).dmg,entity.getComponent(BulletComponent.class).isBulletHit);
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
