package com.cosma.annihilation.Utils.Serialization;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.utils.OrderedMap;
import com.cosma.annihilation.Components.*;

import java.util.ArrayList;

public class EntityWrapper {
    private OrderedMap<String,Component> map;
    public EntityWrapper(){
        map = new OrderedMap();
    }

    public void fillMap(ImmutableArray<Component> immutableArray){
        for(Component component: immutableArray){
            if(component instanceof PlayerStateComponent || component instanceof PlayerComponent){
                continue;
            }
            map.put(component.getClass().getSimpleName(),component);
        }
    }
    public OrderedMap<String,Component> getEntityMap() {
        return map;
    }
}
