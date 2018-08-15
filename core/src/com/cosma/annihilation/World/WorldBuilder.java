package com.cosma.annihilation.World;

import box2dLight.RayHandler;
import com.badlogic.ashley.core.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.cosma.annihilation.Components.TextureComponent;
import com.cosma.annihilation.Entities.EntityFactory;
import com.cosma.annihilation.Entities.PlayerEntity;
import com.cosma.annihilation.Systems.*;
import com.cosma.annihilation.Utils.AssetsLoader;
import com.cosma.annihilation.Utils.BodyID;
import com.cosma.annihilation.Utils.Constants;
import com.cosma.annihilation.Utils.StateManager;
import net.dermetfan.gdx.graphics.g2d.Box2DSprite;

public class WorldBuilder implements Disposable, EntityListener {


    private Engine engine;
    public World world;
    private OrthographicCamera camera;
    private Viewport viewport;
    private TiledMap tiledMap;
    private WorldLoader worldLoader;
    private RayHandler rayHandler;

        public WorldBuilder(){
        initializeEngine();

        // add all entity
        worldLoader = new WorldLoader(engine,world,tiledMap,rayHandler);
    }
    public void initializeEngine(){
        //Create camera
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(16/1.3f, 9/1.3f,camera);
        viewport.apply(true);
        camera.update();
        //Load map
        tiledMap = AssetsLoader.manager.get("Map/2/map1.tmx", TiledMap.class);
        //Create a pooled engine & world
        world = new World(new Vector2(Constants.WORLD_GRAVITY), true);
        engine = new PooledEngine();
        //Add all the relevant systems our engine should run
        LightRenderSystem lightRenderSystem = new LightRenderSystem(camera,world);
        this.rayHandler = lightRenderSystem.getRayHandler();
        engine.addSystem(new RenderSystem(camera,world));
        engine.addSystem(new CollisionSystem(world));
        engine.addSystem(new PhysicsSystem(world));
        engine.addSystem(new PlayerControlSystem());
        engine.addSystem(new CameraSystem(camera));
        engine.addSystem(new TileMapRender(camera,tiledMap));
        engine.addSystem(new AnimationSystem());
        engine.addSystem(new ActionSystem(engine,world));
        engine.addSystem(new HealthSystem());
        engine.addSystem(lightRenderSystem);
        engine.addSystem(new DebugRenderSystem(camera,world));
        engine.addSystem(new SaveSystem(world));


    }

    public void update(float delta) {
        debugInput();
        viewport.apply();
        engine.update(delta);
        camera.update();

        }

    public void resize(int w, int h) {
        viewport.update(w, h, true);
        }
    public OrthographicCamera getCamera() {
            return  camera;
            }

    private void debugInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) camera.translate(0, 1);
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) camera.translate(0, -1);
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) camera.translate(-1, 0);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) camera.translate(1, 0);
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) camera.zoom = camera.zoom + 0.2f;
        if (Gdx.input.isKeyPressed(Input.Keys.W)) camera.zoom = camera.zoom - 0.2f;
        camera.update();
    }

    public Engine getEngine() {
        return engine;
    }

    @Override
    public void entityAdded(Entity entity) {

    }

    @Override
    public void entityRemoved(Entity entity) {

    }

    @Override
    public void dispose() {
        for (EntitySystem entitySystem : engine.getSystems()) {
            engine.removeSystem(entitySystem);

            if (entitySystem instanceof Disposable) {
                ((Disposable) entitySystem).dispose();
            }
        }
    }
}
