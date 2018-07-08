package com.cosma.annihilation.Systems;




import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.physics.box2d.*;
import com.cosma.annihilation.Utils.BodyID;
import com.cosma.annihilation.Utils.StateManager;


public class ContactListenerSystem extends EntitySystem implements ContactListener {

    public ContactListenerSystem(){

    }



    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();



        if(fb.getUserData() == BodyID.PLAYER_FOOT || fa.getUserData() == BodyID.PLAYER_FOOT)  {
            StateManager.onGround = true;
        }
        if(fb.getUserData() == BodyID.PLAYER_BODY && fa.getUserData() == BodyID.LADDER ||
                fa.getUserData() == BodyID.PLAYER_BODY && fb.getUserData() == BodyID.LADDER)  {
            System.out.println("its ladder");
            StateManager.onGround =false;
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();
        if(fb.getUserData() == BodyID.PLAYER_FOOT || fa.getUserData() == BodyID.PLAYER_FOOT)   {
            StateManager.onGround = false;
        }
        if(fb.getUserData() == BodyID.PLAYER_BODY && fa.getUserData() == BodyID.LADDER ||
                fa.getUserData() == BodyID.PLAYER_BODY && fb.getUserData() == BodyID.LADDER)  {
            System.out.println("its out ladder");
            StateManager.onGround =false;
        }

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }


}
