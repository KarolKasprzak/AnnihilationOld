package com.cosma.annihilation.Utils;

import com.badlogic.ashley.core.Entity;

public class GameEntity extends Entity {
    private String name;

    public GameEntity(){
    }

    public GameEntity(String name){
        this.name = name;

    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
