package com.cosma.annihilation.Ai;

import com.badlogic.ashley.core.Entity;

public class Task implements Comparable<Task>{
    private boolean isEnded = false;
    private boolean isActive = true;
    private int priority = 0;

    public Task(int priority) {
        this.priority = priority;

    }

    public boolean isActive() {
        return isActive;
    }

    public void run(Entity entity){}

    public int getPriority() {
        return priority;
    }

    @Override
    public int compareTo(Task task) {
        return this.priority - task.priority;
    }
}
