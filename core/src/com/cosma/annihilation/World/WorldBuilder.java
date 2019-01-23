package com.cosma.annihilation.World;

import box2dLight.RayHandler;
import com.badlogic.ashley.core.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import com.cosma.annihilation.Entities.EntityFactory;
import com.cosma.annihilation.Gui.Gui;
import com.cosma.annihilation.Items.ItemFactory;
import com.cosma.annihilation.Systems.*;
import com.cosma.annihilation.Utils.GfxAssetDescriptors;
import com.cosma.annihilation.Utils.AssetLoader;
import com.cosma.annihilation.Utils.Constants;


public class WorldBuilder implements Disposable, EntityListener {


    private Engine engine;
    public World world;
    private OrthographicCamera camera;
    private Viewport viewport;
    private TiledMap tiledMap;
    private RayHandler rayHandler;
    private Gui gui;
    private AssetLoader assetLoader;

    public WorldBuilder(Boolean isGameLoaded, AssetLoader assetLoader) {
        this.assetLoader = assetLoader;
        ItemFactory.getInstance().setAssetLoader(assetLoader);

        camera = new OrthographicCamera();
        viewport = new ExtendViewport(16/1.3f, 9/1.3f,camera);
        viewport.apply(true);
        camera.update();
        SpriteBatch batch = new SpriteBatch();

        tiledMap = assetLoader.manager.get(GfxAssetDescriptors.tiledMap);

        world = new World(new Vector2(Constants.WORLD_GRAVITY), true);
        rayHandler = new RayHandler(world);
        engine = new PooledEngine();

        EntityFactory.getInstance().setAssetLoader(assetLoader);
        EntityFactory.getInstance().createPlayerEntity(engine,world);
        new WorldLoader(engine, world, tiledMap, rayHandler);

        gui = new Gui(engine, world,assetLoader);

        if (isGameLoaded) {
            gui.loadGame();

        }

        engine.addSystem(new RenderSystem(camera,world,rayHandler,batch));
        engine.addSystem(new HealthSystem(gui,camera));
        engine.addSystem(new CollisionSystem(world));
        engine.addSystem(new PhysicsSystem(world));
        engine.addSystem(new PlayerControlSystem());
        engine.addSystem(new CameraSystem(camera));
        engine.addSystem(new TileMapRender(camera,tiledMap));
        engine.addSystem(new AnimationSystem(assetLoader));
        engine.addSystem(new DebugRenderSystem(camera,world));
        engine.addSystem(new ActionSystem(world,gui));
        engine.addSystem(new ShootingSystem(world,assetLoader));
        engine.getSystem(CollisionSystem.class).SetSignal();

        gui.addSystemsReferences(engine);
        engine.addEntityListener(this);
    }

    public void update(float delta) {
        debugInput();
        viewport.apply();
        engine.update(delta);
        camera.update();
        gui.render(delta);
        }

    public void resize(int w, int h) {
        viewport.update(w, h, true);
        gui.resize(w,h);
        }
    public OrthographicCamera getCamera() {
            return  camera;
            }
    public Stage getPlayerHudStage()        {
            return gui.getStage();
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
        world.dispose();
        for (EntitySystem entitySystem : engine.getSystems()) {
            engine.removeSystem(entitySystem);

            if (entitySystem instanceof Disposable) {
                ((Disposable) entitySystem).dispose();
            }
        }
    }
}
