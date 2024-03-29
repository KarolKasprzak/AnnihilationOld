package com.cosma.annihilation.World;

import box2dLight.RayHandler;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.cosma.annihilation.Annihilation;
import com.cosma.annihilation.Components.BodyComponent;
import com.cosma.annihilation.Components.PlayerComponent;
import com.cosma.annihilation.Editor.CosmaMap.CosmaMapLoader;
import com.cosma.annihilation.Entities.EntityFactory;
import com.cosma.annihilation.Items.ItemFactory;
import com.cosma.annihilation.Systems.*;
import com.cosma.annihilation.Utils.Constants;
import com.cosma.annihilation.Utils.Dialogs.DialogueManager;
import com.cosma.annihilation.Utils.Enums.GameEvent;
import com.cosma.annihilation.Utils.LuaScript.LuaScript;
import com.cosma.annihilation.Utils.LuaScript.ScriptManager;
import com.cosma.annihilation.Utils.LuaScript.TestLua;
import com.cosma.annihilation.Utils.Serialization.GameEntitySerializer;
import com.cosma.annihilation.Utils.StateManager;

import java.util.ArrayList;


public class WorldBuilder implements Disposable, EntityListener, Listener<GameEvent> {


    private Engine engine;
    public World world;
    private OrthographicCamera camera;
    private Viewport viewport;
    private CosmaMapLoader mapLoader;
    private Signal<GameEvent> signal;
    private boolean isPaused = false;
    private RayHandler rayHandler;
    private Json json;

    public WorldBuilder(Boolean isGameLoaded, InputMultiplexer inputMultiplexer) {
        ItemFactory.getInstance().setAssetLoader(Annihilation.getAssetsLoader());


        //Game camera
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(18, 10, camera);
        viewport.apply(false);
        SpriteBatch batch = new SpriteBatch();
        //Box2d world & light handler
        world = new World(new Vector2(Constants.WORLD_GRAVITY), true);

        rayHandler = new RayHandler(world);
        RayHandler.useDiffuseLight(true);
        rayHandler.setShadows(true);

        engine = new PooledEngine();
        engine.addEntityListener(this);

        EntityFactory.getInstance().setEngine(engine);
        EntityFactory.getInstance().setWorld(world);
        signal = new Signal<GameEvent>();

        mapLoader = new CosmaMapLoader(world, rayHandler, engine);
//        mapLoader.loadMap("map/lab.map");
        mapLoader.loadMap("map/forest_test.map");
        json = new Json();
        json.setSerializer(Entity.class, new GameEntitySerializer(world, engine));




        ScriptManager scriptManager = new ScriptManager(engine,world);
        scriptManager.runScript("script_test");



        engine.addSystem(new UserInterfaceSystem(engine, world, this));
        engine.addSystem(new ActionSystem(this, camera,batch));
        engine.addSystem(new ShootingSystem(world, rayHandler, batch, camera));
        engine.addSystem(new SpriteRenderSystem(camera, batch));
        engine.addSystem(new RenderSystem(camera, world, rayHandler, batch));
        engine.addSystem(new SecondRenderSystem(camera, batch));
        engine.addSystem(new HealthSystem(camera));
        engine.addSystem(new CollisionSystem(world));
        engine.addSystem(new PhysicsSystem(world));
        engine.addSystem(new PlayerControlSystem(world));
        engine.addSystem(new CameraSystem(camera));
        engine.addSystem(new TileMapRender(camera, mapLoader.getMap()));
        engine.addSystem(new AnimationSystem());
        engine.addSystem(new DebugRenderSystem(camera, world));
        engine.addSystem(new AiSystem(world, batch, camera));

        engine.addEntityListener(this);



        engine.getSystem(PlayerControlSystem.class).addListenerSystems();
        engine.getSystem(CollisionSystem.class).addListenerSystems(this);

        signal.add(getEngine().getSystem(ActionSystem.class));
        signal.add(getEngine().getSystem(ShootingSystem.class));
        signal.add(getEngine().getSystem(UserInterfaceSystem.class));

        inputMultiplexer.addProcessor(engine.getSystem(UserInterfaceSystem.class).getStage());
        inputMultiplexer.addProcessor(engine.getSystem(PlayerControlSystem.class));

    }

    public void update(float delta) {
        debugInput();
        if (!isPaused) {
            engine.update(delta);
            camera.update();
        }
    }

    public void resize(int w, int h) {
        viewport.update(w, h, false);
        engine.getSystem(UserInterfaceSystem.class).resizeHUD(w, h);
        rayHandler.getLightMapTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
    }

    public OrthographicCamera getCamera() {
        return camera;
    }


    private void debugInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) camera.translate(0, 1);
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) camera.translate(0, -1);
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) camera.translate(-1, 0);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) camera.translate(1, 0);
        if (Gdx.input.isKeyPressed(Input.Keys.Z)) camera.zoom = camera.zoom + 0.2f;
        if (Gdx.input.isKeyPressed(Input.Keys.X)) camera.zoom = camera.zoom - 0.2f;
        if (Gdx.input.isKeyPressed(Input.Keys.P)) {
            loadMap();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.V)) {
            StateManager.debugMode = !StateManager.debugMode;
        }

        camera.update();
    }


    public void saveMap(boolean isPlayerGoToNewLocation) {
        FileHandle mapFile = Gdx.files.local("save/" + mapLoader.getMap().getMapName());
        FileHandle playerFile = Gdx.files.local("save/player.json");
        Entity playerEntity = engine.getEntitiesFor(Family.all(PlayerComponent.class).get()).first();
        if (!isPlayerGoToNewLocation) {
            playerEntity.getComponent(PlayerComponent.class).mapName = mapLoader.getMap().getMapName();
        }
        playerFile.writeString(json.prettyPrint(playerEntity), false);


        json.setIgnoreUnknownFields(false);
        System.out.println("save entity");
        for (Entity entity : engine.getEntities()) {
            if (!mapLoader.getMap().getEntityArrayList().contains(entity)) {
                mapLoader.getMap().getEntityArrayList().add(entity);
            }
        }
        System.out.println("entity saved");
        mapLoader.getMap().getEntityArrayList().remove(playerEntity);
        System.out.println("write file");
        mapFile.writeString(json.prettyPrint(mapLoader.getMap()), false);
    }

    public void loadMap() {
        isPaused = true;
        System.out.println("e " + engine.getEntities().size());
        System.out.println("b " + world.getBodyCount());
        for (Entity entity : engine.getEntities()) {
            for (Component component : entity.getComponents()) {
                if (component instanceof BodyComponent) {
                    world.destroyBody(((BodyComponent) component).body);
                    ((BodyComponent) component).body = null;
                }
            }
        }
        Array<Body> bodies = new Array<>();
        world.getBodies(bodies);
        for (Body body : bodies) {
            world.destroyBody(body);
        }
        rayHandler.removeAll();
        bodies.clear();
        engine.removeAllEntities();
        System.out.println("e " + engine.getEntities().size());
        System.out.println("b " + world.getBodyCount());
//        mapLoader.loadMap("save/save.json");
//
//
//
//        isPaused = false;

        FileHandle playerFile = Gdx.files.local("save/player.json");
        Entity playerEntity = json.fromJson(Entity.class, playerFile);

        mapLoader.loadMap("map/" + playerEntity.getComponent(PlayerComponent.class).mapName);
        mapLoader.getMap().getEntityArrayList().add(playerEntity);
        isPaused = false;

    }

    public void goToMap() {
        saveMap(true);
        isPaused = true;
        System.out.println("e " + engine.getEntities().size());
        System.out.println("b " + world.getBodyCount());
        for (Entity entity : engine.getEntities()) {
            for (Component component : entity.getComponents()) {
                if (component instanceof BodyComponent) {
                    world.destroyBody(((BodyComponent) component).body);
                    ((BodyComponent) component).body = null;
                }
            }
        }
        Array<Body> bodies = new Array<>();
        world.getBodies(bodies);
        for (Body body : bodies) {
            world.destroyBody(body);
        }
        rayHandler.removeAll();
        bodies.clear();
        engine.removeAllEntities();
        System.out.println("e " + engine.getEntities().size());
        System.out.println("b " + world.getBodyCount());

        FileHandle playerFile = Gdx.files.local("save/player.json");
        Entity playerEntity = json.fromJson(Entity.class, playerFile);

        mapLoader.loadMap("map/" + playerEntity.getComponent(PlayerComponent.class).mapName);
        mapLoader.getMap().getEntityArrayList().add(playerEntity);
        isPaused = false;

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

    @Override
    public void receive(Signal<GameEvent> signal, GameEvent object) {
        if (object.equals(GameEvent.PLAYER_GO_TO_NEW_MAP)) {
            goToMap();
        }
    }
}
