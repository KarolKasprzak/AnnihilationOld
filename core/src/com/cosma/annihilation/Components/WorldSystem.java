package com.cosma.annihilation.Components;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IntervalIteratingSystem;
import com.badlogic.gdx.utils.Disposable;

public class WorldSystem extends IntervalIteratingSystem implements Disposable {

    private ComponentMapper<WorldComponent> worldMapper;
    private float step;

    public WorldSystem() {
        super(Family.all(WorldComponent.class).get(), 1.0f / 60, 10);

        worldMapper = ComponentMapper.getFor(WorldComponent.class);
        step = 1.0f / 60;
    }

    @Override
    protected void processEntity(Entity entity) {
        worldMapper.get(entity).world.step(step, 6, 2);
    }

    @Override
    public void dispose() {
        for (Entity entity: this.getEntities()) {
            worldMapper.get(entity).world.dispose();
        }
    }
}
