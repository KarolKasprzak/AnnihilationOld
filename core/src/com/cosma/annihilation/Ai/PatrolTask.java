package com.cosma.annihilation.Ai;

import com.badlogic.ashley.core.Entity;

public class PatrolTask extends Task {
    public PatrolTask(int priority,int patrolRange, float timeToTurn) {
        super(priority);

    }

    @Override
    public void run(Entity entity) {



    }
}
