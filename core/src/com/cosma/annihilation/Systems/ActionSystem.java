package com.cosma.annihilation.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.cosma.annihilation.Components.BodyComponent;
import com.cosma.annihilation.Components.PlayerComponent;
import com.cosma.annihilation.Gui.OnScreenGui;
import com.cosma.annihilation.Utils.BodyID;

public class ActionSystem extends IteratingSystem {
    private ComponentMapper<BodyComponent> bodyMapper;
    private Body playerBody;
    private World world;
    private Vector2 point1;
    private Vector2 point2;
    private Vector2 collisionVector;


    public ActionSystem(Engine engine, World world) {
        super(Family.all(PlayerComponent.class).get(),11);
        this.world = world;
        bodyMapper = ComponentMapper.getFor(BodyComponent.class);



    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
      playerBody = bodyMapper.get(entity).body;
     point1 = playerBody.getPosition();
     point2 = new Vector2(point1.x+0.5f,point1.y);


        RayCastCallback callback = new RayCastCallback() {
            @Override
            public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                if(fixture.getUserData() == BodyID.CONTAINER){
//                    OnScreenGui.setLabelposition(fixture.getBody().getPosition().x,fixture.getBody().getPosition().y);
//                    OnScreenGui.setLabelText("Open");
                }


//                if(fixture.){
//                    OnScreenGui.setLabelText("");
//                }


                return 0;
            }
        };



      world.rayCast(callback,point1,point2);





    }
}
