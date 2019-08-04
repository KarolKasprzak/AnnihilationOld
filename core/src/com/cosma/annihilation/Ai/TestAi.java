package com.cosma.annihilation.Ai;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

public class TestAi implements ArtificialIntelligence {
    private Array<Task> taskList;

    public TestAi() {
        taskList = new Array<>();
        Task task = new Task(5);
        Task task1 = new Task(3);
        Task task2 = new Task(6);
        taskList.add(task);
        taskList.add(task1);
        taskList.add(task2);

    }

    @Override
    public void update(Entity entity) {
            taskList.sort();
            for(Task task: taskList){
                if(task.isActive()){
                    task.run();
                }
            }
    }

    @Override
    public String getStatus() {
        return null;
    }
}
