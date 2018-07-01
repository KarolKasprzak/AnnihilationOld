package com.cosma.annihilation.Entities;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.*;
import com.cosma.annihilation.Components.*;
import com.cosma.annihilation.Utils.AssetsLoader;
import net.dermetfan.gdx.graphics.g2d.Box2DSprite;

public class EntityFactory {
    private Engine engine;
    private World world;
    private OrthographicCamera camera;
    public EntityFactory(World world, Engine engine, OrthographicCamera camera){
        this.world = world;
        this.engine = engine;
        this.camera = camera;

    }
    public  Entity createWorld() {
        Entity entity = new Entity();
        WorldComponent worldComponent = new WorldComponent();
        worldComponent.world = this.world;
        entity.add(worldComponent);
        this.engine.addEntity(entity);
        return entity;
    }
//    public Entity playerEntity() {
//        Entity entity = new Entity();
//        BodyComponent bodyComponent = new BodyComponent();
//        PlayerComponent playerComponent = new PlayerComponent();
//
//        BodyDef bodyDef = new BodyDef();
//        bodyDef.type = BodyDef.BodyType.DynamicBody;
//        bodyDef.position.set(9, 1);
//        PolygonShape shape = new PolygonShape();
//        shape.setAsBox(0.5f, 1);
//        FixtureDef fixtureDef = new FixtureDef();
//        fixtureDef.shape = shape;
//        fixtureDef.density = 1f;
//        bodyComponent.body = world.createBody(bodyDef);
//        bodyComponent.body.setFixedRotation(true);
//        bodyComponent.body.createFixture(fixtureDef);
//
//        TextureComponent texture = engine.createComponent(TextureComponent.class);
//        texture.texture = (Texture) AssetsLoader.getResorce("hero");
//
//        Box2DSprite box2DSprite = new Box2DSprite(texture.texture);
//        bodyComponent.body.setUserData(box2DSprite);
//
//        entity.add(playerComponent);
//        entity.add(bodyComponent);
//        entity.add(texture);
//
//        this.engine.addEntity(entity);
//
//        return entity;
//    }
        public Entity cameraEntity(){
            Entity entity = engine.createEntity();
            CameraComponent cameraComponent = new CameraComponent();
            cameraComponent.camera = camera;
            entity.add(cameraComponent);
            this.engine.addEntity(entity);

            return entity;
        }
    }

