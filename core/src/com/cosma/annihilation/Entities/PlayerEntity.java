package com.cosma.annihilation.Entities;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.cosma.annihilation.Components.BodyComponent;
import com.cosma.annihilation.Components.PlayerComponent;
import com.cosma.annihilation.Components.TextureComponent;
import com.cosma.annihilation.Utils.AssetsLoader;
import com.cosma.annihilation.Utils.BodyID;
import net.dermetfan.gdx.graphics.g2d.Box2DSprite;

public class PlayerEntity {
//    private  Engine engine;

    public  PlayerEntity(Engine engine, World world) {
        Entity entity = new Entity();
        BodyComponent bodyComponent = new BodyComponent();
        PlayerComponent playerComponent = new PlayerComponent();
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        texture.texture = (Texture) AssetsLoader.getResource("hero");

        Box2DSprite box2DSprite = new Box2DSprite(texture.texture);
        // Player physic components
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(9, 1);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(1f, 2);
        bodyComponent.body = world.createBody(bodyDef);
        bodyComponent.body.setFixedRotation(true);
        //Fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        bodyComponent.body.createFixture(fixtureDef).setUserData(box2DSprite);
        //Sensor fixture
        PolygonShape sensorShape = new PolygonShape();
        sensorShape.setAsBox(0.5f,0.5f, new Vector2(0,-2),0);
        fixtureDef.shape = sensorShape;
        fixtureDef.density = 0.2f;
        fixtureDef.isSensor = true;
        bodyComponent.body.createFixture(fixtureDef).setUserData(BodyID.PLAYER_FOOT);
        //Add entity
        entity.add(playerComponent);
        entity.add(bodyComponent);
        entity.add(texture);
        engine.addEntity(entity);
    }
}
