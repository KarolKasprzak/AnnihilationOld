package com.cosma.annihilation.Entities;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.cosma.annihilation.Components.*;
import com.cosma.annihilation.Gui.Inventory.InventoryItemLocation;
import com.cosma.annihilation.Utils.*;
import com.cosma.annihilation.Utils.Enums.EntityAction;
import com.cosma.annihilation.Utils.Enums.BodyID;
import com.cosma.annihilation.Utils.Enums.CollisionID;
import com.cosma.annihilation.Utils.Enums.EntityID;

import java.util.ArrayList;

public class EntityFactory {

    private static EntityFactory instance = null;
    private Engine engine;
    private World world;

    private EntityFactory() {
    }


    public void setEngine(Engine engine) {

        this.engine = engine;
    }

    public void setWorld(World world) {

        this.world = world;
    }

    public static EntityFactory getInstance() {
        if (instance == null) {
            instance = new EntityFactory();
        }

        return instance;
    }


    public Entity createTestEnemy(){
        Entity entity = new Entity();

        EnemyComponent enemyComponent = engine.createComponent(EnemyComponent.class);
        BodyComponent bodyComponent = engine.createComponent(BodyComponent.class);
        HealthComponent healthComponent = engine.createComponent(HealthComponent.class);
        TextureComponent textureComponent = engine.createComponent(TextureComponent.class);
        SerializationComponent serializationComponent = engine.createComponent(SerializationComponent.class);


        healthComponent.maxHP = 50;
        healthComponent.hp = 50;
        serializationComponent.type = EntityID.ENEMY_TEST;
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyComponent.body = world.createBody(bodyDef);
        bodyComponent.body.setUserData(entity);
        bodyComponent.body.setBullet(true);
        //Physic fixture
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.5f, 1f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;

        fixtureDef.filter.categoryBits = CollisionID.NO_SHADOW | CollisionID.JUMPABLE_OBJECT;
        bodyComponent.body.createFixture(fixtureDef).setUserData(BodyID.ENEMY_TEST);

        entity.add(textureComponent);
        entity.add(bodyComponent);
        entity.add(healthComponent);
        entity.add(enemyComponent);
        entity.add(serializationComponent);

        engine.addEntity(entity);
        return entity;
    }


    public Entity createBulletEntity(float x, float y,float speed,boolean flip, int dmg,boolean accuracy){
        Entity entity = engine.createEntity();
        BodyComponent bodyComponent = engine.createComponent(BodyComponent.class);
        BulletComponent bulletComponent = engine.createComponent(BulletComponent.class);

        bulletComponent.dmg = dmg;
        bulletComponent.isBulletHit = accuracy;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        bodyComponent.body = world.createBody(bodyDef);
        bodyComponent.body.setUserData(entity);
        bodyComponent.body.setBullet(true);
        //Physic fixture
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.02f, 0.01f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 8f;
        fixtureDef.friction = 1f;
        fixtureDef.filter.categoryBits = CollisionID.NO_SHADOW | CollisionID.JUMPABLE_OBJECT;
        bodyComponent.body.createFixture(fixtureDef).setUserData(BodyID.BULLET);

        bodyComponent.body.setLinearVelocity(speed,0.5f);
        entity.add(bodyComponent);
        entity.add(bulletComponent);

        return entity;
    }

    public Entity createBulletShellEntity(float x, float y){
        Entity entity = engine.createEntity();
        BodyComponent bodyComponent = engine.createComponent(BodyComponent.class);
        BulletComponent bulletComponent = engine.createComponent(BulletComponent.class);
        TextureComponent textureComponent = engine.createComponent(TextureComponent.class);



        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        bodyComponent.body = world.createBody(bodyDef);
        bodyComponent.body.setUserData(entity);
        bodyComponent.body.setBullet(false);
        //Physic fixture
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.02f, 0.01f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.2f;
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = 0.4f;

        fixtureDef.filter.categoryBits = CollisionID.BACKGROUND ;
        fixtureDef.filter.maskBits = CollisionID.SCENERY| CollisionID.JUMPABLE_OBJECT;

        bodyComponent.body.createFixture(fixtureDef).setUserData(BodyID.BULLET_SHELL);

        entity.add(textureComponent);
        entity.add(bodyComponent);
        entity.add(bulletComponent);

        return entity;
    }

    public Entity createDoorEntity(){
        Entity entity = new Entity();
        BodyComponent bodyComponent = new BodyComponent();
        TextureComponent textureComponent = new TextureComponent();
        ActionComponent actionComponent = new ActionComponent();
        HealthComponent healthComponent = new HealthComponent();
        SerializationComponent serializationComponent = engine.createComponent(SerializationComponent.class);
        DoorComponent doorComponent = new DoorComponent();

        serializationComponent.type = EntityID.DOOR;
        healthComponent.hp = 100;
        healthComponent.maxHP = 100;




        actionComponent.action = EntityAction.OPEN_DOOR;

        //----------Body Component----------------------
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyComponent.body = world.createBody(bodyDef);
        bodyComponent.body.setUserData(entity);
        //Physic fixture
        PolygonShape shape = new PolygonShape();
        shape.setAsBox( 0.1f,1);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 8f;
        fixtureDef.friction = 1f;
        fixtureDef.filter.categoryBits = CollisionID.CAST_SHADOW;
        bodyComponent.body.createFixture(fixtureDef);
        //Sensor fixture
        CircleShape sensorShape = new CircleShape();
        sensorShape.setRadius(1);
        FixtureDef touchSensorFixture = new FixtureDef();
        touchSensorFixture.shape = sensorShape;
        touchSensorFixture.isSensor = true;
        touchSensorFixture.filter.categoryBits = CollisionID.NO_SHADOW;
        bodyComponent.body.createFixture(touchSensorFixture).setUserData(BodyID.CONTAINER);
        //-----------Body Component End----------------------
        entity.add(doorComponent);
        entity.add(serializationComponent);
        entity.add(textureComponent);
        entity.add(bodyComponent);
        entity.add(actionComponent);
        entity.add(healthComponent);
        engine.addEntity(entity);

        return entity;
    }

    public Entity createBoxEntity(float x, float y, Array<InventoryItemLocation> itemList) {
        Entity entity = new Entity();
        //Component
        BodyComponent bodyComponent = new BodyComponent();
        ContainerComponent containerComponent = new ContainerComponent();
        TextureComponent textureComponent = new TextureComponent();
        ActionComponent actionComponent = new ActionComponent();
        SerializationComponent serializationComponent = new SerializationComponent();
        HealthComponent healthComponent = new HealthComponent();
        healthComponent.hp = 50;
        healthComponent.maxHP = 50;



        serializationComponent.type = EntityID.BOX;
        actionComponent.action = EntityAction.OPEN;
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
        shape.setAsBox(0.93f / 2, 0.93f / 2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 8f;
        fixtureDef.friction = 1f;
        fixtureDef.filter.categoryBits = CollisionID.NO_SHADOW | CollisionID.JUMPABLE_OBJECT;
        bodyComponent.body.createFixture(fixtureDef);
        //Sensor fixture
        CircleShape sensorShape = new CircleShape();
        sensorShape.setRadius(1);
        FixtureDef touchSensorFixture = new FixtureDef();
        touchSensorFixture.shape = sensorShape;
        touchSensorFixture.isSensor = true;
        touchSensorFixture.filter.categoryBits = CollisionID.NO_SHADOW;
        bodyComponent.body.createFixture(touchSensorFixture).setUserData(BodyID.CONTAINER);
        //-----------Body Component End----------------------
        entity.add(textureComponent);
        entity.add(serializationComponent);
        entity.add(bodyComponent);
        entity.add(containerComponent);
        entity.add(actionComponent);
        entity.add(healthComponent);
        engine.addEntity(entity);
        return entity;
    }

    public Entity createBoxEntityTest() {
        Entity entity = new Entity();


        BodyComponent bodyComponent = new BodyComponent();
        ContainerComponent containerComponent = new ContainerComponent();
        ActionComponent actionComponent = new ActionComponent();
        SerializationComponent serializationComponent = new SerializationComponent();
        serializationComponent.type = EntityID.BOX;


        actionComponent.action = EntityAction.OPEN;
        containerComponent.name = "box";
        //----------Body Component----------------------
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyComponent.body = world.createBody(bodyDef);
        bodyComponent.body.setUserData(entity);
        //Physic fixture
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(1f / 2, 1f / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 8f;
        fixtureDef.friction = 1f;
        fixtureDef.filter.categoryBits = CollisionID.NO_SHADOW | CollisionID.JUMPABLE_OBJECT;
        bodyComponent.body.createFixture(fixtureDef);
        //Render fixture
        FixtureDef renderFixture = new FixtureDef();
        renderFixture.shape = shape;
        renderFixture.isSensor = true;
        renderFixture.filter.categoryBits = CollisionID.NO_SHADOW;

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
        entity.add(actionComponent);
        engine.addEntity(entity);
        return entity;
    }

    public void createPlayerEntity(Engine engine,World world) {
        this.engine = engine;
        this.world = world;
        createPlayerEntity();
    }

    public Entity createPlayerEntity() {
        Entity entity = new Entity();

        BodyComponent bodyComponent = new BodyComponent();
        PlayerComponent playerComponent = new PlayerComponent();
        HealthComponent healthComponent = new HealthComponent();
        SerializationComponent typeComponent = new SerializationComponent();
        PlayerInventoryComponent playerInventoryComponent = new PlayerInventoryComponent();
        TextureComponent textureComponent = new TextureComponent();
        PlayerComponent stateComponent = new PlayerComponent();
        PlayerStatsComponent playerStatsComponent = new PlayerStatsComponent();
        AnimationComponent animationComponent = new AnimationComponent();
//        textureComponent.texturePatch = GfxPlayerAssetDescriptors.player_stand_melee.fileName;
//        textureComponent.setTexture();

//        animationComponent.animatedSprite = new AnimatedSprite();

        typeComponent.type = EntityID.PLAYER;
        healthComponent.hp = 67;
        playerComponent.collisionEntityList = new ArrayList<Entity>();
        playerInventoryComponent.inventoryItem = new Array<InventoryItemLocation>();
        playerInventoryComponent.equippedItem = new Array<InventoryItemLocation>();

        //Player body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyComponent.body = world.createBody(bodyDef);
        bodyComponent.body.setFixedRotation(true);
        //Body physic fixture
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.5f, 1f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0f;
        fixtureDef.filter.categoryBits = CollisionID.NO_SHADOW;
        fixtureDef.filter.maskBits = CollisionID.MASK_PLAYER;
        bodyComponent.body.createFixture(fixtureDef).setUserData(BodyID.PLAYER_BODY);
        shape.dispose();
        //Body sensor fixture
        PolygonShape bodySensorShape = new PolygonShape();
        bodySensorShape.setAsBox(0.5f / 2, 1.9f / 2);
        FixtureDef centerFixtureDef = new FixtureDef();
        centerFixtureDef.shape = bodySensorShape;
        centerFixtureDef.isSensor = true;
        centerFixtureDef.filter.categoryBits = CollisionID.NO_SHADOW;
        bodyComponent.body.createFixture(centerFixtureDef).setUserData(BodyID.PLAYER_CENTER);
        bodySensorShape.dispose();
        //Foot sensor fixture
        PolygonShape footSensorShape = new PolygonShape();
        footSensorShape.setAsBox(0.5f / 2, 0.5f / 2, new Vector2(0, -1), 0);
        FixtureDef footFixtureDef = new FixtureDef();
        footFixtureDef.shape = footSensorShape;
        footFixtureDef.isSensor = true;
        footFixtureDef.filter.categoryBits = CollisionID.JUMPABLE_OBJECT;
        footFixtureDef.filter.maskBits = CollisionID.JUMPABLE_OBJECT;
        bodyComponent.body.createFixture(footFixtureDef).setUserData(BodyID.PLAYER_FOOT);
        //Add entity
        entity.add(animationComponent);
        entity.add(playerStatsComponent);
        entity.add(stateComponent);
        entity.add(textureComponent);
        entity.add(playerInventoryComponent);
        entity.add(typeComponent);
        entity.add(healthComponent);
        entity.add(playerComponent);
        entity.add(bodyComponent);
        engine.addEntity(entity);
        return entity;
    }

}

