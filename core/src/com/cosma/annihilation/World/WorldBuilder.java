package com.cosma.annihilation.World;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.cosma.annihilation.Components.TextureComponent;
import com.cosma.annihilation.Components.WorldSystem;
import com.cosma.annihilation.Entities.EntityFactory;
import com.cosma.annihilation.Systems.*;
import com.cosma.annihilation.Utils.AssetsLoader;
import net.dermetfan.gdx.graphics.g2d.Box2DSprite;

public class WorldBuilder implements Disposable, EntityListener {
    private Engine engine;
    public World world;
    private OrthographicCamera camera;
    private Viewport viewport;
    private SpriteBatch sb;
    Body body ;

        public WorldBuilder(){
        world = new World(new Vector2(0,-9), true);
        sb = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(16, 9,camera);
        viewport.apply(true);
        camera.update();
        sb.setProjectionMatrix(camera.combined);
        //create a pooled engine
        engine = new PooledEngine();
        // add all the relevant systems our engine should run
        engine.addSystem(new WorldSystem());
        engine.addSystem(new RenderSystem(camera,world));
        engine.addSystem(new DebugRenderSystem(camera,world));
        engine.addSystem(new PhysicsSystem(world));
        engine.addSystem(new PlayerControlSystem());
        engine.addSystem(new CameraSystem(camera));
        // add all entity
        EntityFactory entityFactory = new EntityFactory(world,engine,camera);
        entityFactory.playerEntity();
        entityFactory.cameraEntity();
        addMap();
    }
    public void update(float delta) {

        //Call all the logic
        engine.update(delta);
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
        body.createFixture(fixtureDef);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        texture.texture = (Texture) AssetsLoader.getResorce("ladder");

        Box2DSprite box2DSprite = new Box2DSprite(texture.texture);
        body.setUserData(box2DSprite);
    }
}
