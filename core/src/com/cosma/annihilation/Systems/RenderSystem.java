package com.cosma.annihilation.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.cosma.annihilation.Components.TextureComponent;
import com.cosma.annihilation.Components.TransformComponent;
import net.dermetfan.gdx.graphics.g2d.Box2DSprite;

import java.util.Comparator;

public class RenderSystem extends IteratingSystem implements Disposable {


    private OrthographicCamera camera;
    private SpriteBatch batch;
    private World world;


    private ComponentMapper<TextureComponent> textureMapper;
    private ComponentMapper<TransformComponent> transformMapper;

    @SuppressWarnings("unchecked")
    public RenderSystem(OrthographicCamera camera,World world) {
        // gets all entities with a TransformComponent and TextureComponent
//        super(Family.all(TransformComponent.class, TextureComponent.class).get(), new ZComparator());
        super(Family.all(TransformComponent.class, TextureComponent.class).get(),11);
        this.camera = camera;
        this.world = world;
        batch = new SpriteBatch();
        //creates out componentMappers
        textureMapper = ComponentMapper.getFor(TextureComponent.class);
        transformMapper = ComponentMapper.getFor(TransformComponent.class);
//        this.batch = batch;  // set our batch to the one supplied in constructor
        // set up the camera to match our screen size
//        camera = new OrthographicCamera(FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
//        camera.position.set(FRUSTUM_WIDTH / 2f, FRUSTUM_HEIGHT / 2f, 0);






    }

    @Override
    public void update(float deltaTime) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        Box2DSprite.draw(batch,world);
        super.update(deltaTime);
        batch.end();
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {

    }
    @Override
    public void dispose() {

    }
}