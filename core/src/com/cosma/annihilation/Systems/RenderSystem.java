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
import com.cosma.annihilation.Components.BodyComponent;
import com.cosma.annihilation.Components.TextureComponent;
import com.cosma.annihilation.Components.TransformComponent;
import com.cosma.annihilation.Utils.Constants;
import net.dermetfan.gdx.graphics.g2d.Box2DSprite;

import java.util.Comparator;

public class RenderSystem extends IteratingSystem implements Disposable {


    private OrthographicCamera camera;
    public SpriteBatch batch;
    private World world;


    private ComponentMapper<TextureComponent> textureMapper;
    private ComponentMapper<TransformComponent> transformMapper;

    @SuppressWarnings("unchecked")
    public RenderSystem(OrthographicCamera camera,World world) {

        super(Family.all(TransformComponent.class, TextureComponent.class).get(),Constants.RENDER);
        this.camera = camera;
        this.world = world;
        batch = new SpriteBatch();

       textureMapper = ComponentMapper.getFor(TextureComponent.class);
       transformMapper = ComponentMapper.getFor(TransformComponent.class);
    }

    @Override
    public void update(float deltaTime) {

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        super.update(deltaTime);
        Box2DSprite.draw(batch,world);

        batch.end();
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {

            TransformComponent transformComponent = transformMapper.get(entity);
            TextureComponent textureComponent = textureMapper.get(entity);
            if(textureComponent.texture != null){
                batch.draw(textureComponent.texture,transformComponent.position.x-transformComponent.sizeX/2,transformComponent.position.y-transformComponent.sizeY/2,textureComponent.texture.getHeight() /32,textureComponent.texture.getWidth()/32);
            }else
                System.out.println("Texture is null!");




    }
    @Override
    public void dispose() {
        batch.dispose();
    }
}