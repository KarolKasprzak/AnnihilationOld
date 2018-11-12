package com.cosma.annihilation.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.cosma.annihilation.Components.BodyComponent;
import com.cosma.annihilation.Components.TransformComponent;
import com.cosma.annihilation.Utils.Enums.BodyID;
import com.cosma.annihilation.Utils.StateManager;


public class PhysicsSystem extends IteratingSystem {
    private static final float MAX_STEP_TIME = 1/45f;
    private static float accumulator = 0f;
    private World world;
    private Array<Entity> bodiesQueue;
    private ComponentMapper<BodyComponent> bodyMapper = ComponentMapper.getFor(BodyComponent.class);
    private ComponentMapper<TransformComponent> transformMapper = ComponentMapper.getFor(TransformComponent.class);

    @SuppressWarnings("unchecked")
    public PhysicsSystem(World world) {
        super(Family.all(BodyComponent.class, TransformComponent.class).get());
        this.world = world;
        this.bodiesQueue = new Array<Entity>();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;
        if(accumulator >= MAX_STEP_TIME &! StateManager.pause) {
            world.step(MAX_STEP_TIME, 6, 2);
            accumulator -= MAX_STEP_TIME;
            if(getEngine().getSystem(CollisionSystem.class).bodiesToRemove.size > 0 && !world.isLocked()){
                System.out.println(getEngine().getSystem(CollisionSystem.class).bodiesToRemove.size);
                for(Body body: getEngine().getSystem(CollisionSystem.class).bodiesToRemove){
                    world.destroyBody(body);
                    getEngine().getSystem(CollisionSystem.class).bodiesToRemove.removeValue(body,true);
                }
            }

        }
        for (Entity entity : bodiesQueue) {
            TransformComponent tfm = transformMapper.get(entity);
            BodyComponent bodyComp = bodyMapper.get(entity);
            Vector2 position = bodyComp.body.getPosition();
            tfm.position.x = position.x;
            tfm.position.y = position.y;
            tfm.rotation = bodyComp.body.getAngle() * MathUtils.radiansToDegrees;
        }
        bodiesQueue.clear();

    }
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        bodiesQueue.add(entity);


    }
}
