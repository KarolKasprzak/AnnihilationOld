package com.cosma.annihilation.Entities;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.cosma.annihilation.Annihilation;
import com.cosma.annihilation.Components.*;
import com.cosma.annihilation.Gui.Inventory.InventoryItemLocation;
import com.cosma.annihilation.Utils.*;
import com.cosma.annihilation.Utils.Enums.EntityAction;
import com.cosma.annihilation.Utils.Enums.BodyID;
import com.cosma.annihilation.Utils.Enums.CollisionID;
import com.cosma.annihilation.Utils.Enums.EntityID;

import java.util.ArrayList;
import java.util.Random;

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





    public Entity createBulletEntity(float x, float y,float speed,boolean flip){
        Entity entity = engine.createEntity();
        BodyComponent bodyComponent = engine.createComponent(BodyComponent.class);
        BulletComponent bulletComponent = engine.createComponent(BulletComponent.class);
        TextureComponent textureComponent = engine.createComponent(TextureComponent.class);

        textureComponent.texture = Annihilation.getAssets().get("gfx/textures/bullet_trace.png");
        textureComponent.renderAfterLight = true;
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
        fixtureDef.isSensor = true;
        fixtureDef.shape = shape;
        fixtureDef.density = 8f;
        fixtureDef.friction = 1f;
        fixtureDef.filter.categoryBits = CollisionID.NO_SHADOW | CollisionID.JUMPABLE_OBJECT;
        bodyComponent.body.createFixture(fixtureDef).setUserData(BodyID.BULLET);

        float yVelocity = MathUtils.random(-1f,1.5f);
        if(flip){
            bodyComponent.body.setLinearVelocity(speed,yVelocity);
            textureComponent.flipTexture = true;
        }else{
            bodyComponent.body.setLinearVelocity(-speed,yVelocity);
        }

        entity.add(textureComponent);
        entity.add(bodyComponent);
        entity.add(bulletComponent);

        return entity;
    }

    public Entity createBulletShellEntity(float x, float y){
        Entity entity = engine.createEntity();
        BodyComponent bodyComponent = engine.createComponent(BodyComponent.class);
        BulletComponent bulletComponent = engine.createComponent(BulletComponent.class);
        TextureComponent textureComponent = engine.createComponent(TextureComponent.class);

        textureComponent.texture = Annihilation.getAssets().get("gfx/textures/bullet_shell.png");


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

    public Entity createShootSplashEntity(float x, float y,boolean flip){
        Entity entity =  engine.createEntity();
        SpriteComponent spriteComponent = engine.createComponent(SpriteComponent.class);
        spriteComponent.texture = Annihilation.getAssets().get("gfx/textures/splash.png");
        spriteComponent.x = x;
        spriteComponent.y = y;
        spriteComponent.isLifeTimeLimited = true;
        spriteComponent.lifeTime = 0.18f;


        if(flip){
            spriteComponent.flipTexture = true;
        }
        entity.add(spriteComponent);

        return entity;

    }




    public Entity createDoorEntity(){
        Entity entity = new Entity();
        BodyComponent bodyComponent = new BodyComponent();
        TextureComponent textureComponent = new TextureComponent();
        ActionComponent actionComponent = new ActionComponent();
        HealthComponent healthComponent = new HealthComponent();
        DoorComponent doorComponent = new DoorComponent();

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

        //-----------Body Component End----------------------
        entity.add(doorComponent);
        entity.add(textureComponent);
        entity.add(bodyComponent);
        entity.add(actionComponent);
        entity.add(healthComponent);
        engine.addEntity(entity);

        return entity;
    }


    public Entity createPlayerEntity() {
        Entity entity = new Entity();

        BodyComponent bodyComponent = new BodyComponent();
        PlayerComponent playerComponent = new PlayerComponent();
        HealthComponent healthComponent = new HealthComponent();
        PlayerInventoryComponent playerInventoryComponent = new PlayerInventoryComponent();
        TextureComponent textureComponent = new TextureComponent();
        PlayerComponent stateComponent = new PlayerComponent();
        PlayerStatsComponent playerStatsComponent = new PlayerStatsComponent();
        AnimationComponent animationComponent = new AnimationComponent();
//        textureComponent.texturePatch = GfxPlayerAssetDescriptors.player_stand_melee.fileName;
//        textureComponent.setTexture();

//        animationComponent.animatedSprite = new AnimatedSprite();


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

        entity.add(healthComponent);
        entity.add(playerComponent);
        entity.add(bodyComponent);
        engine.addEntity(entity);
        return entity;
    }

}

