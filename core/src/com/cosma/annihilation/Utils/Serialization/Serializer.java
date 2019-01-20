package com.cosma.annihilation.Utils.Serialization;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Json;
import com.cosma.annihilation.Components.*;
import com.cosma.annihilation.Entities.EntityFactory;
import com.cosma.annihilation.Systems.ActionSystem;
import com.cosma.annihilation.Utils.Enums.EntityID;
import com.cosma.annihilation.Utils.GfxAssetDescriptors;
import com.cosma.annihilation.Utils.StateManager;
import java.util.ArrayList;

public class Serializer{

    private Engine engine;
    private World world;
    private Json json;
    private FileHandle file;
    private EntityFactory entityFactory;

    public Serializer(Engine engine,World world){
        this.engine = engine;
        this.world = world;
        json = new Json();
        json.setUsePrototypes(false);
        entityFactory = EntityFactory.getInstance();
        file = Gdx.files.local("save.json");

    }

    public void save(){
        EngineWrapper engineWrapper = new EngineWrapper();
        engineWrapper.fillArray(engine);
        file.writeString(json.prettyPrint(engineWrapper),false);
    }

    public void load(){
//        System.out.println(engine.getEntities().size());
//        System.out.println(world.getBodyCount());
        StateManager.pause = true;
        for(Entity entity: engine.getEntitiesFor(Family.all(BodyComponent.class).get())){
            world.destroyBody(entity.getComponent(BodyComponent.class).body);
        }
        engine.removeAllEntities();
            ArrayList<EntityWrapper> entityList = json.fromJson(EngineWrapper.class, Gdx.files.local("save.json")).getEntityList();
            for (EntityWrapper entityWrapper : entityList) {
                createEntity(entityWrapper);
            }
        StateManager.pause = false;
//        System.out.println(engine.getEntities().size());
//        System.out.println(world.getBodyCount());
    }

    private void setPosition (Entity entity){
        Vector2 position = entity.getComponent(BodyComponent.class).body.getPosition();
        float angle = entity.getComponent(BodyComponent.class).body.getAngle();
//        float angle = entity.getComponent(BodyComponent.class).body.getAngle() * MathUtils.degreesToRadians;
        entity.getComponent(BodyComponent.class).body.setTransform(position,angle);
    }

    private void createEntity(EntityWrapper entityWrapper) {
        SerializationComponent serialization = (SerializationComponent) entityWrapper.getEntitysMap().get("SerializationComponent");
        EntityID id = serialization.type;
        Entity entity = null;
        switch (id) {
            case PLAYER:
                entity = EntityFactory.getInstance().createPlayerEntity();
                break;
            case DOOR:
                entity = EntityFactory.getInstance().createDoorEntity();
                break;
            case BOX:
                entity = EntityFactory.getInstance().createBoxEntityTest();
                break;
            case ENEMY_TEST:
                entity = EntityFactory.getInstance().createTestEnemy();
                break;
        }
        if (entity != null) {
            for (Component component : entityWrapper.getEntitysMap().values()) {
                entity.add(component);
            }
            if(entity.getComponent(SerializationComponent.class).type.equals(EntityID.DOOR)) {
                if (entity.getComponent(DoorComponent.class).isOpen) {
                    engine.getSystem(ActionSystem.class).loadDoor(entity);
                }
            }
            setPosition(entity);
        }
    }
}
