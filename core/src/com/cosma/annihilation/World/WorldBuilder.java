package com.cosma.annihilation.World;

import box2dLight.RayHandler;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.cosma.annihilation.Annihilation;
import com.cosma.annihilation.Components.BodyComponent;
import com.cosma.annihilation.Components.PlayerComponent;
import com.cosma.annihilation.Editor.CosmaMap.CosmaMapLoader;
import com.cosma.annihilation.Editor.CosmaMap.GameMap;
import com.cosma.annihilation.Editor.CosmaMap.MapLayer;
import com.cosma.annihilation.Entities.EntityFactory;
import com.cosma.annihilation.Items.ItemFactory;
import com.cosma.annihilation.Systems.*;
import com.cosma.annihilation.Utils.AssetLoader;
import com.cosma.annihilation.Utils.Constants;
import com.cosma.annihilation.Utils.Enums.GameEvent;
import com.cosma.annihilation.Utils.Serialization.EntitySerializer;
import com.cosma.annihilation.Utils.Serialization.GameEntitySerializer;
import com.cosma.annihilation.Utils.StateManager;


public class WorldBuilder implements Disposable, EntityListener, InputProcessor {


    private Engine engine;
    public World world;
    private OrthographicCamera camera;
    private Viewport viewport;
    private CosmaMapLoader loader;
    private Signal<GameEvent> signal;
    private boolean isPaused = false;
    private RayHandler rayHandler;

    public WorldBuilder(Boolean isGameLoaded, InputMultiplexer inputMultiplexer) {
        ItemFactory.getInstance().setAssetLoader(Annihilation.getAssetsLoader());

        camera = new OrthographicCamera();
        viewport = new ExtendViewport(9,5,camera);
        viewport.apply(false);
        SpriteBatch batch = new SpriteBatch();

        world = new World(new Vector2(Constants.WORLD_GRAVITY), true);
        rayHandler = new RayHandler(world);
        engine = new PooledEngine();
        engine.addEntityListener(this);


        EntityFactory.getInstance().setEngine(engine);
        EntityFactory.getInstance().setWorld(world);
        signal = new Signal<GameEvent>();

        loader = new CosmaMapLoader("map/map.map", world, rayHandler, engine);

//        if (isGameLoaded) {
//            gui.loadGame();
//
//        }

        engine.addSystem(new ActionSystem(world));
        engine.addSystem(new ShootingSystem(world, rayHandler,batch,camera));
        engine.addSystem(new UserInterfaceSystem(engine,world,this));
        engine.addSystem(new RenderSystem(camera, world, rayHandler, batch));
        engine.addSystem(new SecondRenderSystem(camera, batch));
        engine.addSystem(new HealthSystem(camera));
        engine.addSystem(new CollisionSystem(world));
        engine.addSystem(new PhysicsSystem(world));
        engine.addSystem(new PlayerControlSystem());
        engine.addSystem(new CameraSystem(camera));
        engine.addSystem(new TileMapRender(camera, loader.getMap()));
        engine.addSystem(new AnimationSystem());
        engine.addSystem(new DebugRenderSystem(camera, world));
        engine.addSystem(new AiSystem(world,batch,camera));

        engine.addEntityListener(this);

        inputMultiplexer.addProcessor(engine.getSystem(UserInterfaceSystem.class).getStage());
        inputMultiplexer.addProcessor(this);

        engine.getSystem(PlayerControlSystem.class).addListenerSystems();
        engine.getSystem(CollisionSystem.class).addListenerSystems();

        signal.add(getEngine().getSystem(ActionSystem.class));
        signal.add(getEngine().getSystem(ShootingSystem.class));
        signal.add(getEngine().getSystem(UserInterfaceSystem.class));

    }

    public void update(float delta) {
        debugInput();
        if(!isPaused){
            engine.update(delta);
            camera.update();
        }
    }

    public void resize(int w, int h) {
        viewport.update(w, h, false);
        engine.getSystem(UserInterfaceSystem.class).resizeHUD(w, h);
    }

    public OrthographicCamera getCamera() {
        return camera;
    }


    private void debugInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) camera.translate(0, 1);
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) camera.translate(0, -1);
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) camera.translate(-1, 0);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) camera.translate(1, 0);
        if (Gdx.input.isKeyPressed(Input.Keys.S)) this.saveMap();
        if (Gdx.input.isKeyPressed(Input.Keys.Z)) camera.zoom = camera.zoom + 0.2f;
        if (Gdx.input.isKeyPressed(Input.Keys.X)) camera.zoom = camera.zoom - 0.2f;
        if (Gdx.input.isKeyPressed(Input.Keys.P)){
            loadMap();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)){
            if (Gdx.input.isKeyPressed(Input.Keys.D)){
                StateManager.debugMode = true;
            }
        }
        camera.update();
    }


    public void saveMap(){
        Json json = new Json();
        FileHandle file = Gdx.files.local("save/save.json");
        json.setIgnoreUnknownFields(false);
        json.setSerializer(Entity.class, new GameEntitySerializer(engine,world));
        file.writeString(json.prettyPrint(loader.getMap()), false);

    }

    public void loadMap(){
        isPaused = true;
        System.out.println("e "+engine.getEntities().size());
        System.out.println("b "+world.getBodyCount());
        for(Entity entity: engine.getEntities()){
            for(Component component: entity.getComponents()){
                if(component instanceof BodyComponent){
                    world.destroyBody(((BodyComponent) component).body);
                    ((BodyComponent) component).body = null;
                }
            }
        }
        Array<Body> bodies = new Array<>();
        world.getBodies(bodies);
        for(Body body: bodies){
            world.destroyBody(body);
            body = null;
        }
        rayHandler.removeAll();
        bodies.clear();
        bodies = null;
        engine.removeAllEntities();
        System.out.println("e "+engine.getEntities().size());
        System.out.println("b "+world.getBodyCount());

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
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.I || keycode == Input.Keys.ESCAPE){
            signal.dispatch(GameEvent.OPEN_MENU);
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(button == Input.Buttons.LEFT){
            signal.dispatch(GameEvent.ACTION_BUTTON_TOUCH_DOWN);
            signal.dispatch(GameEvent.PERFORM_ACTION);
        }

        //Weapon take out/hide
        if(button == Input.Buttons.RIGHT){
            signal.dispatch(GameEvent.WEAPON_TAKE_OUT);
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(button == Input.Buttons.LEFT){
            signal.dispatch(GameEvent.ACTION_BUTTON_TOUCH_UP);
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
