package com.cosma.annihilation.Ai;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;

public class PatrolTask extends Task {
    public PatrolTask(int priority, Array<Task> taskArray,int patrolRange, float timeToTurn) {
        super(priority,taskArray);

    }

    @Override
    public void run(Entity entity) {



    }
}
