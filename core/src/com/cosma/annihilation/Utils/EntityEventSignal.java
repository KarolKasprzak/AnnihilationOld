package com.cosma.annihilation.Utils;

import com.badlogic.ashley.core.Entity;
import com.cosma.annihilation.Utils.Enums.GameEvent;



public class EntityEventSignal {
    private GameEvent gameEvent;

    public float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }

    private Entity entity;
    private int dmg;
    private float accuracy;

    public EntityEventSignal(){}


    public GameEvent getGameEvent() {
        return gameEvent;
    }

    public Entity getEntity() {
        return entity;
    }

    public int getDamage() {
        return dmg;
    }

    public void setEvent(GameEvent gameEvent,Entity entity,int dmg,float accuracy) {
        this.gameEvent = gameEvent;
        this.entity = entity;
        this.dmg = dmg;
        this.accuracy = accuracy;
    }


}
