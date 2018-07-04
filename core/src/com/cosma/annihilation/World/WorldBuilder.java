package com.cosma.annihilation.World;

import com.badlogic.ashley.core.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.cosma.annihilation.Components.TextureComponent;
import com.cosma.annihilation.Entities.EntityFactory;
import com.cosma.annihilation.Entities.PlayerEntity;
import com.cosma.annihilation.Systems.*;
import com.cosma.annihilation.Utils.AssetsLoader;
import com.cosma.annihilation.Utils.BodyID;
import com.cosma.annihilation.Utils.Constants;
import net.dermetfan.gdx.graphics.g2d.Box2DSprite;

public class WorldBuilder implements Disposable, EntityListener {
    private Engine engine;
    public World world;
    private OrthographicCamera camera;
    private Viewport viewport;
    private SpriteBatch sb;
    private Body body ;
    private TiledMap tiledMap;
    private WorldLoader worldLoader;

        public WorldBuilder(){
        start();
        loadMap();
        sb = new SpriteBatch();
        viewport = new ExtendViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT,camera);
        viewport.apply(true);
        camera.update();
        sb.setProjectionMatrix(camera.combined);
        // add all entity
        PlayerEntity playerEntity = new PlayerEntity(engine,world);
        EntityFactory entityFactory = new EntityFactory(world,engine,camera);
        entityFactory.cameraEntity();
        addMap();
        WorldLoader worldLoader = new WorldLoader(engine,world,tiledMap);
        worldLoader.loadMap();
    }
    public void start(){
        //Create camera
        camera = new OrthographicCamera();
        //Load map
        tiledMap = AssetsLoader.manager.get("Map/1/bunker.tmx", TiledMap.class);
        //Create a pooled engine & world
        world = new World(new Vector2(Constants.WORLD_GRAVITY), true);
        world.setContactListener(new ContactListenerSystem());
        engine = new PooledEngine();
        //Add all the relevant systems our engine should run
        engine.addSystem(new RenderSystem(camera,world));
        engine.addSystem(new DebugRenderSystem(camera,world));
        engine.addSystem(new PhysicsSystem(world));
        engine.addSystem(new PlayerControlSystem());
//        engine.addSystem(new CameraSystem(camera));
        engine.addSystem(new TileMapRender(camera,tiledMap));
    }
    public void loadMap(){
     worldLoader = new WorldLoader(engine,world,tiledMap);

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

    private void debugInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) camera.translate(0, 1);
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) camera.translate(0, -1);
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) camera.translate(-1, 0);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) camera.translate(1, 0);
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) applyZoom(camera.zoom + 0.02f);
        if (Gdx.input.isKeyPressed(Input.Keys.W)) applyZoom(camera.zoom - 0.02f);
    }
    public void applyZoom(float amount) {
        //Bound the zoom to min and max values 0.8 - 2.5
        amount = Math.max(0.8f, Math.min(2.5f, amount));
        camera.zoom = amount;
        camera.update();
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
    private void addMap(){

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(1, -2);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(25f, 1);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef).setUserData(BodyID.GROUND);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        texture.texture = (Texture) AssetsLoader.getResource("ladder");

        Box2DSprite box2DSprite = new Box2DSprite(texture.texture);
        body.setUserData(box2DSprite);

    }
}
