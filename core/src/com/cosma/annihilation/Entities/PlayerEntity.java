package com.cosma.annihilation.Entities;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.cosma.annihilation.Components.*;
import com.cosma.annihilation.Utils.AssetsLoader;
import com.cosma.annihilation.Utils.BodyID;
import com.cosma.annihilation.Utils.CollisionID;
import com.cosma.annihilation.Utils.Constants;
import net.dermetfan.gdx.graphics.g2d.AnimatedBox2DSprite;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;
import net.dermetfan.gdx.graphics.g2d.Box2DSprite;

public class PlayerEntity {

    public  PlayerEntity(Engine engine, World world) {


        Entity entity = new Entity();
        BodyComponent bodyComponent = new BodyComponent();
        PlayerComponent playerComponent = new PlayerComponent();
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        TransformComponent transformComponent = new TransformComponent();
        HealthComponent healthComponent= new HealthComponent();
        healthComponent.hp = 67;
        //Player body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(1, 1);
        bodyComponent.body = world.createBody(bodyDef);
        bodyComponent.body.setFixedRotation(true);
        //Body physic fixture
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(1f/2, 2f/2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0f;
        fixtureDef.filter.categoryBits = CollisionID.NO_SHADOW;
        bodyComponent.body.createFixture(fixtureDef).setUserData(BodyID.PLAYER_BODY);
        //Body sensor fixture
        PolygonShape bodySensorShape = new PolygonShape();
        bodySensorShape.setAsBox(0.5f/2,1.9f/2);
        FixtureDef centerFixtureDef = new FixtureDef();
        centerFixtureDef.shape = bodySensorShape;
        centerFixtureDef.isSensor = true;
        centerFixtureDef.filter.categoryBits = CollisionID.NO_SHADOW;
        bodyComponent.body.createFixture(centerFixtureDef).setUserData(BodyID.PLAYER_CENTER);
        //Foot sensor fixture
        PolygonShape footSensorShape = new PolygonShape();
        footSensorShape.setAsBox(0.5f/2,0.5f/2, new Vector2(0,-1),0);
        FixtureDef footFixtureDef = new FixtureDef();
        footFixtureDef.shape = footSensorShape;
        footFixtureDef.isSensor = true;
        footFixtureDef.filter.categoryBits = CollisionID.NO_SHADOW ;
        bodyComponent.body.createFixture(footFixtureDef).setUserData(BodyID.PLAYER_FOOT);
        //Sprite render fixture
        PolygonShape playerSensorShape = new PolygonShape();
        playerSensorShape.setAsBox(2f/2,2f/2, new Vector2(0,0),0);
        FixtureDef playerRenderFixture = new FixtureDef();
        playerRenderFixture.shape = playerSensorShape;
        playerRenderFixture.isSensor = true;
        playerRenderFixture.filter.categoryBits = CollisionID.NO_SHADOW;
        bodyComponent.body.createFixture(playerRenderFixture);
        //Add entity
        entity.add(healthComponent);
        entity.add(transformComponent);
        entity.add(playerComponent);
        entity.add(bodyComponent);
        entity.add(texture);
        engine.addEntity(entity);
    }
}
