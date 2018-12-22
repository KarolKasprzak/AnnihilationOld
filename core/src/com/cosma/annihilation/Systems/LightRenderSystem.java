package com.cosma.annihilation.Systems;


import box2dLight.RayHandler;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.cosma.annihilation.Utils.Constants;
import com.cosma.annihilation.Utils.StateManager;


public class LightRenderSystem extends IteratingSystem {

    private OrthographicCamera camera;
    private World world;
    private RayHandler rayHandler;
    private SpriteBatch batch;

    public LightRenderSystem(OrthographicCamera camera, World world,RayHandler rayHandler) {
        super(Family.all().get(),Constants.LIGHT_RENDER);
        this.camera = camera;
        this.world = world;
        this.rayHandler = rayHandler;
        rayHandler.useDiffuseLight(true);

    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        rayHandler.setCombinedMatrix(camera);
        rayHandler.updateAndRender();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

    }
    public RayHandler getRayHandler(){
        return rayHandler;
    }
}