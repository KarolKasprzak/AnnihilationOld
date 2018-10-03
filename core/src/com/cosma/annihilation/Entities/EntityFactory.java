package com.cosma.annihilation.Entities;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.cosma.annihilation.Components.*;
import com.cosma.annihilation.Gui.InventoryItemLocation;
import com.cosma.annihilation.Utils.*;
import com.cosma.annihilation.Utils.Enums.BodyID;
import com.cosma.annihilation.Utils.Enums.CollisionID;
import com.cosma.annihilation.Utils.Enums.EntityID;
import net.dermetfan.gdx.graphics.g2d.Box2DSprite;

public class EntityFactory {
    private Engine engine;
    private World world;
     public EntityFactory(World world, Engine engine){
        this.world = world;
        this.engine = engine;
        }

    public Entity createBoxEntity(float x, float y, Array<InventoryItemLocation> itemList) {
        Entity entity = new Entity();
        Texture mainTexture = (Texture) AssetsLoader.getResource("box");
        Box2DSprite box2DSprite = new Box2DSprite(mainTexture);
        //Component
        BodyComponent bodyComponent = new BodyComponent();
        ContainerComponent containerComponent = new ContainerComponent();
        TransformComponent transformComponent = new TransformComponent();
        ActionComponent actionComponent = new ActionComponent();
        SerializationComponent serializationComponent = new SerializationComponent();
        serializationComponent.type = EntityID.BOX;

        actionComponent.openBoxAction = true;
        containerComponent.name = "box";
        containerComponent.itemLocations = itemList;
        //----------Body Component----------------------
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        bodyComponent.body = world.createBody(bodyDef);
        bodyComponent.body.setUserData(entity);
        //Physic fixture
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(1f / 2, 1f / 2);
        bodyComponent.SizeX = 1f / 2;
        bodyComponent.SizeY = 1f / 2;
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 8f;
        fixtureDef.friction = 1f;
        fixtureDef.filter.categoryBits = CollisionID.NO_SHADOW | CollisionID.CAN_JUMP_OBJECT;
        bodyComponent.body.createFixture(fixtureDef);
        //Render fixture
        FixtureDef renderFixture = new FixtureDef();
        renderFixture.shape = shape;
        renderFixture.isSensor = true;
        renderFixture.filter.categoryBits = CollisionID.NO_SHADOW;
        bodyComponent.body.createFixture(renderFixture).setUserData(box2DSprite);
        //Sensor fixture
        CircleShape sensorShape = new CircleShape();
        sensorShape.setRadius(1);
        FixtureDef touchSensorFixture = new FixtureDef();
        touchSensorFixture.shape = sensorShape;
        touchSensorFixture.isSensor = true;
        touchSensorFixture.filter.categoryBits = CollisionID.NO_SHADOW;
        bodyComponent.body.createFixture(touchSensorFixture).setUserData(BodyID.CONTAINER);
        //-----------Body Component End----------------------
        entity.add(serializationComponent);
        entity.add(bodyComponent);
        entity.add(containerComponent);
        entity.add(transformComponent);
        entity.add(actionComponent);
        engine.addEntity(entity);

        return entity;
    }

    public Entity createBoxEntityTest() {
        Entity entity = new Entity();
        Texture mainTexture = (Texture) AssetsLoader.getResource("box");
        Box2DSprite box2DSprite = new Box2DSprite(mainTexture);
        //Component
        BodyComponent bodyComponent = new BodyComponent();
        ContainerComponent containerComponent = new ContainerComponent();
        TransformComponent transformComponent = new TransformComponent();
        ActionComponent actionComponent = new ActionComponent();
        SerializationComponent serializationComponent = new SerializationComponent();
        serializationComponent.type = EntityID.BOX;

        actionComponent.openBoxAction = true;
        containerComponent.name = "box";
        //----------Body Component----------------------
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyComponent.body = world.createBody(bodyDef);
        bodyComponent.body.setUserData(entity);
        //Physic fixture
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(1f / 2, 1f / 2);
        bodyComponent.SizeX = 1f / 2;
        bodyComponent.SizeY = 1f / 2;
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 8f;
        fixtureDef.friction = 1f;
        fixtureDef.filter.categoryBits = CollisionID.NO_SHADOW | CollisionID.CAN_JUMP_OBJECT;
        bodyComponent.body.createFixture(fixtureDef);
        //Render fixture
        FixtureDef renderFixture = new FixtureDef();
        renderFixture.shape = shape;
        renderFixture.isSensor = true;
        renderFixture.filter.categoryBits = CollisionID.NO_SHADOW;
        bodyComponent.body.createFixture(renderFixture).setUserData(box2DSprite);
        //Sensor fixture
        CircleShape sensorShape = new CircleShape();
        sensorShape.setRadius(1);
        FixtureDef touchSensorFixture = new FixtureDef();
        touchSensorFixture.shape = sensorShape;
        touchSensorFixture.isSensor = true;
        touchSensorFixture.filter.categoryBits = CollisionID.NO_SHADOW;
        bodyComponent.body.createFixture(touchSensorFixture).setUserData(BodyID.CONTAINER);
        //-----------Body Component End----------------------
        entity.add(serializationComponent);
        entity.add(bodyComponent);
        entity.add(containerComponent);
        entity.add(transformComponent);
        entity.add(actionComponent);
        engine.addEntity(entity);
        return entity;
    }

    public Entity createPlayerEntity(){
        Entity entity = new Entity();
        BodyComponent bodyComponent = new BodyComponent();
        PlayerComponent playerComponent = new PlayerComponent();
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        TransformComponent transformComponent = new TransformComponent();
        HealthComponent healthComponent= new HealthComponent();
        SerializationComponent typeComponent = new SerializationComponent();
        typeComponent.className = this.getClass().getName();
        typeComponent.type = EntityID.PLAYER;
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
        entity.add(typeComponent);
        entity.add(healthComponent);
        entity.add(transformComponent);
        entity.add(playerComponent);
        entity.add(bodyComponent);
        entity.add(texture);
        engine.addEntity(entity);
        return entity;
    }

    }

