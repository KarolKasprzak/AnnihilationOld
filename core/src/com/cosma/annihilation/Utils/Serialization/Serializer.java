package com.cosma.annihilation.Utils.Serialization;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Json;
import com.cosma.annihilation.Components.*;
import com.cosma.annihilation.Entities.EntityFactory;
import com.cosma.annihilation.Entities.PlayerEntity;
import com.cosma.annihilation.Utils.EntityID;
import com.cosma.annihilation.Utils.StateManager;
import java.util.ArrayList;

public class Serializer{

    private Engine engine;
    private World world;
    private Json json;
    private FileHandle file;

    public Serializer(Engine engine,World world){
        this.engine = engine;
        this.world = world;
        json = new Json();
        json.setUsePrototypes(false);
        file = Gdx.files.local("save/save.json");
    }

    public void save(){
        EngineWrapper engineWrapper = new EngineWrapper();
        engineWrapper.fillArray(engine);
        file.writeString(json.prettyPrint(engineWrapper),false);
    }

    public void load(){
        StateManager.pause = true;
        for(Entity entity: engine.getEntitiesFor(Family.all(BodyComponent.class).get())){
            world.destroyBody(entity.getComponent(BodyComponent.class).body);
        }
        engine.removeAllEntities();
        ArrayList<EntityWrapper> entityList = json.fromJson( EngineWrapper.class, Gdx.files.internal("save/save.json")).getEntityList();
        for(EntityWrapper entityWrapper: entityList){
            createEntity(entityWrapper);
        }
        StateManager.pause = false;
    }


    public void setPosition (Entity entity){
        Vector2 position = entity.getComponent(TransformComponent.class).position;
        float angle = entity.getComponent(TransformComponent.class).rotation * MathUtils.degreesToRadians;
        entity.getComponent(BodyComponent.class).body.setTransform(position,angle);
    }

    public void createEntity(EntityWrapper entityWrapper){
        SerializationComponent serialization = (SerializationComponent) entityWrapper.getEntitysMap().get("SerializationComponent");
        if(serialization.type.equals(EntityID.PLAYER)){
            PlayerEntity playerEntity = new PlayerEntity(engine,world);
            for(Component component: entityWrapper.getEntitysMap().values()){
                playerEntity.getEntity().add(component);
            }
            setPosition(playerEntity.getEntity());
        }
        if(serialization.type.equals(EntityID.BOX)){
            EntityFactory entityFactory = new EntityFactory(world,engine);
            Entity entity = entityFactory.createBoxEntityTest();
            for(Component component: entityWrapper.getEntitysMap().values()){
                entity.add(component);
            }
            setPosition(entity);
        }
    }
}
