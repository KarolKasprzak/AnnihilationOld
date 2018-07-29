package com.cosma.annihilation.Entities;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.cosma.annihilation.Components.BodyComponent;
import com.cosma.annihilation.Components.PlayerComponent;
import com.cosma.annihilation.Components.TextureComponent;
import com.cosma.annihilation.Components.TransformComponent;
import com.cosma.annihilation.Utils.AssetsLoader;
import com.cosma.annihilation.Utils.BodyID;
import com.cosma.annihilation.Utils.CollisionID;
import com.cosma.annihilation.Utils.Constants;
import net.dermetfan.gdx.graphics.g2d.AnimatedBox2DSprite;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;
import net.dermetfan.gdx.graphics.g2d.Box2DSprite;

public class PlayerEntity {
//    private  Engine engine;

    public  PlayerEntity(Engine engine, World world) {




        Entity entity = new Entity();
        BodyComponent bodyComponent = new BodyComponent();
        PlayerComponent playerComponent = new PlayerComponent();
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        TransformComponent transformComponent = new TransformComponent();
        //Player physic fixture
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(1, 1);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.5f/2, 2/2);
        bodyComponent.body = world.createBody(bodyDef);
        bodyComponent.body.setFixedRotation(true);
        //Body physic fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 2f;
        fixtureDef.friction = 0f;
        bodyComponent.body.createFixture(fixtureDef).setUserData(BodyID.PLAYER_BODY);
        //Body sensor fixture
        PolygonShape bodySensorShape = new PolygonShape();
        bodySensorShape.setAsBox(0.5f/2,1.9f/2, new Vector2(0,0f),0);
        FixtureDef centerFixtureDef = new FixtureDef();
        centerFixtureDef.shape = bodySensorShape;
        centerFixtureDef.density = 0.2f;
        centerFixtureDef.isSensor = true;
        centerFixtureDef.filter.categoryBits = CollisionID.CATEGORY_PLAYER_SENSOR;
        bodyComponent.body.createFixture(centerFixtureDef).setUserData(BodyID.PLAYER_CENTER);
        //Foot sensor fixture
        PolygonShape footSensorShape = new PolygonShape();
        footSensorShape.setAsBox(0.5f/2,0.5f/2, new Vector2(0,-1),0);
        FixtureDef footFixtureDef = new FixtureDef();
        footFixtureDef.shape = footSensorShape;
        footFixtureDef.density = 0.2f;
        footFixtureDef.isSensor = true;
        bodyComponent.body.createFixture(footFixtureDef).setUserData(BodyID.PLAYER_FOOT);
        //Sprite render fixture
        PolygonShape playerSensorShape = new PolygonShape();
        playerSensorShape.setAsBox(1f/2,2f/2, new Vector2(0,0),0);
        FixtureDef playerRenderFixture = new FixtureDef();
        playerRenderFixture.shape = playerSensorShape;
        playerRenderFixture.isSensor = true;
        bodyComponent.body.createFixture(playerRenderFixture);
        //Add entity
        entity.add(transformComponent);
        entity.add(playerComponent);
        entity.add(bodyComponent);
        entity.add(texture);
        engine.addEntity(entity);
    }
}
