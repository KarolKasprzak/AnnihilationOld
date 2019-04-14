package com.cosma.annihilation.World;

import box2dLight.RayHandler;
import com.badlogic.ashley.core.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import com.cosma.annihilation.Components.BodyComponent;
import com.cosma.annihilation.Components.HealthComponent;
import com.cosma.annihilation.Components.PlayerComponent;
import com.cosma.annihilation.Editor.CosmaMap.CosmaMapLoader;
import com.cosma.annihilation.Editor.CosmaMap.GameMap;
import com.cosma.annihilation.Entities.EntityFactory;
import com.cosma.annihilation.Gui.Gui;
import com.cosma.annihilation.Items.ItemFactory;
import com.cosma.annihilation.Systems.*;
import com.cosma.annihilation.Utils.AssetLoader;
import com.cosma.annihilation.Utils.Constants;


public class WorldBuilder implements Disposable, EntityListener {


    private Engine engine;
    public World world;
    private OrthographicCamera camera;
    private Viewport viewport;
    private GameMap gameMap;
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

        world = new World(new Vector2(Constants.WORLD_GRAVITY), true);
        rayHandler = new RayHandler(world);
        engine = new PooledEngine();

        EntityFactory.getInstance().setAssetLoader(assetLoader);


        gui = new Gui(engine, world,assetLoader);

        CosmaMapLoader loader = new CosmaMapLoader("map/map.json",world,rayHandler,engine);
        if (isGameLoaded) {
            gui.loadGame();

        }

        engine.addSystem(new RenderSystem(camera,world,rayHandler,batch));
        engine.addSystem(new SecondRenderSystem(camera,batch));
        engine.addSystem(new HealthSystem(gui,camera));
        engine.addSystem(new CollisionSystem(world));
        engine.addSystem(new PhysicsSystem(world));
        engine.addSystem(new PlayerControlSystem());
        engine.addSystem(new CameraSystem(camera));
        engine.addSystem(new TileMapRender(camera,loader.getMap()));
        engine.addSystem(new AnimationSystem(assetLoader));
        engine.addSystem(new DebugRenderSystem(camera,world));
        engine.addSystem(new ActionSystem(world,gui));
        engine.addSystem(new ShootingSystem(world,rayHandler));
        engine.getSystem(CollisionSystem.class).SetSignal();

        gui.addSystemsReferences(engine);
        engine.addEntityListener(this);

        rayHandler.render();

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
            System.out.println(entity.flags);
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
