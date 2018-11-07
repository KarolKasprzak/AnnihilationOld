package com.cosma.annihilation.Utils.Serialization;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.cosma.annihilation.Components.BulletComponent;

import java.util.ArrayList;

public class EngineWrapper {
    private ArrayList<EntityWrapper> entityList;


    public EngineWrapper(){
        entityList = new ArrayList<EntityWrapper>();
    }

    public void fillArray(Engine engine){
        for(Entity entity: engine.getEntities()){
            if(entity.getComponent(BulletComponent.class) != null){
                continue;
            }
            EntityWrapper entityWrapper = new EntityWrapper();
            entityWrapper.fillMap(entity.getComponents());
            entityList.add(entityWrapper);
        }
    }

    public ArrayList<EntityWrapper> getEntityList() {
        return entityList;
    }
}
