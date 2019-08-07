package com.cosma.annihilation.Ai;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

public class TestAi implements ArtificialIntelligence {
    private Array<Task> taskList;

    public TestAi() {
        taskList = new Array<>();
        Task task = new Task(5,taskList);

        taskList.add(task);


    }

    @Override
    public void update(Entity entity) {
            taskList.sort();
            for(Task task: taskList){
                if(task.isActive()){
//                    task.run();
                }
            }
    }

    @Override
    public String getStatus() {
        return null;
    }
}
