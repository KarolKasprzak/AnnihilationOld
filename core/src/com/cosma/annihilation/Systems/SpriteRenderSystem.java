package com.cosma.annihilation.Systems;

import box2dLight.RayHandler;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.cosma.annihilation.Components.BodyComponent;
import com.cosma.annihilation.Components.SpriteComponent;
import com.cosma.annihilation.Components.TextureComponent;
import com.cosma.annihilation.Utils.Constants;


public class SpriteRenderSystem extends IteratingSystem implements Disposable {


    private OrthographicCamera camera;
    private SpriteBatch batch;
    private ComponentMapper<SpriteComponent> spriteMapper;
    private Vector2 position = new Vector2();

    public SpriteRenderSystem(OrthographicCamera camera, SpriteBatch batch) {
        super(Family.all(SpriteComponent.class).get(), Constants.SPRITE_RENDER);
        this.batch = batch;
        this.camera = camera;
        spriteMapper = ComponentMapper.getFor(SpriteComponent.class);
    }

    @Override
    public void update(float deltaTime) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        super.update(deltaTime);
        batch.end();
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        SpriteComponent spriteComponent = spriteMapper.get(entity);
        position.set(spriteComponent.x, spriteComponent.y);

        if(spriteComponent.isLifeTimeLimited){
            spriteComponent.time += deltaTime;
        }


        if (spriteComponent.texture != null) {
            position.x = position.x - (float) spriteComponent.texture.getWidth() / 32 / 2;
            position.y = position.y - (float) spriteComponent.texture.getHeight() / 32 / 2;
            batch.draw(new TextureRegion(spriteComponent.texture), position.x+(spriteComponent.flipX ? spriteComponent.texture.getWidth() / 32 : 0), position.y, (float) spriteComponent.texture.getWidth() / 32 / 2, (float) spriteComponent.texture.getHeight() / 32 / 2,
                    spriteComponent.texture.getWidth() / 32 * (spriteComponent.flipX ? -1 : 1), spriteComponent.texture.getHeight() / 32,
                    1, 1, spriteComponent.angle);
        }


        if(spriteComponent.isLifeTimeLimited){
           if(spriteComponent.time >= spriteComponent.lifeTime){
               this.getEngine().removeEntity(entity);
           }
        }

    }

    @Override
    public void dispose() {
        batch.dispose();
    }

}