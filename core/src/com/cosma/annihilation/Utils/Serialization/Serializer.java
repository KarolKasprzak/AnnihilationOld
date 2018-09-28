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
import com.cosma.annihilation.Entities.PlayerEntity;
import com.cosma.annihilation.Entities.TestEntityFactory;
import com.cosma.annihilation.Utils.Enums.EntityID;
import com.cosma.annihilation.Utils.StateManager;
import java.util.ArrayList;

public class Serializer{

    private Engine engine;
    private World world;
    private Json json;
    private FileHandle file;
    private TestEntityFactory entityFactory;

    public Serializer(Engine engine,World world){
        this.engine = engine;
        this.world = world;
        json = new Json();
        json.setUsePrototypes(false);
        entityFactory = new TestEntityFactory(world,engine);
        file = Gdx.files.local("save/save.json");
    }

    public void save(){
        System.out.println("engine " + engine.getEntities().size());
        EngineWrapper engineWrapper = new EngineWrapper();
        engineWrapper.fillArray(engine);
        file.writeString(json.prettyPrint(engineWrapper),false);
        System.out.println(engine.getEntitiesFor(Family.all(PlayerComponent.class).get()).first().getComponent(PlayerComponent.class).numFootContacts);
    }

    public void load(){
        StateManager.pause = true;
        for(Entity entity: engine.getEntitiesFor(Family.all(BodyComponent.class).get())){
            world.destroyBody(entity.getComponent(BodyComponent.class).body);
        }
        engine.removeAllEntities();
        StateManager.pause = false;
            ArrayList<EntityWrapper> entityList = json.fromJson(EngineWrapper.class, Gdx.files.internal("save/save.json")).getEntityList();
            for (EntityWrapper entityWrapper : entityList) {
                createEntity(entityWrapper);
            }

    }

    public void setPosition (Entity entity){
        Vector2 position = entity.getComponent(TransformComponent.class).position;
        float angle = entity.getComponent(TransformComponent.class).rotation * MathUtils.degreesToRadians;
        entity.getComponent(BodyComponent.class).body.setTransform(position,angle);
    }


    public void createEntity(EntityWrapper entityWrapper) {
        SerializationComponent serialization = (SerializationComponent) entityWrapper.getEntitysMap().get("SerializationComponent");
        EntityID id = serialization.type;
        Entity entity = entityFactory.getEntity(id);
        for (Component component : entityWrapper.getEntitysMap().values()){
            entity.add(component);
        }
        engine.addEntity(entity);
        setPosition(entity);
//        if(serialization.type.equals(EntityID.PLAYER)){
//            Entity entity1 = entityFactory.getEntity(EntityID.PLAYER);
//            engine.addEntity(entity1);
//            for(Component component: entityWrapper.getEntitysMap().values()){
//                entity1.add(component);
//            }
//            setPosition(entity1);
//        }
//        if(serialization.type.equals(EntityID.BOX)){
//            Entity entity1 = entityFactory.createBox();
//            engine.addEntity(entity1);
//            for(Component component: entityWrapper.getEntitysMap().values()){
//                entity1.add(component);
//            }
//            setPosition(entity1);
//    }
//        if(serialization.type.equals(EntityID.PLAYER)){
//            PlayerEntity playerEntity = new PlayerEntity(engine,world);
//            for(Component component: entityWrapper.getEntitysMap().values()){
//                playerEntity.getEntity().add(component);
//            }
//            setPosition(playerEntity.getEntity());
//        }
//        if(serialization.type.equals(EntityID.BOX)){
//            EntityFactory entityFactory = new EntityFactory(world,engine);
//            Entity entity = entityFactory.createBoxEntityTest();
//            for(Component component: entityWrapper.getEntitysMap().values()){
//                entity.add(component);
//            }
//            setPosition(entity);
    }

}
