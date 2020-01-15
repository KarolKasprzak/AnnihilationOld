package com.cosma.annihilation.Systems;

import box2dLight.RayHandler;
import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;
import com.cosma.annihilation.Components.BodyComponent;
import com.cosma.annihilation.Components.BulletComponent;
import com.cosma.annihilation.Components.PlayerComponent;
import com.cosma.annihilation.Components.TextureComponent;
import com.cosma.annihilation.Utils.Constants;
import com.cosma.annihilation.Utils.RenderComparator;
import com.cosma.annihilation.Utils.Util;


public class RenderSystem extends SortedIteratingSystem implements Disposable {


    private OrthographicCamera camera;
    private SpriteBatch batch;
    private World world;
    private RayHandler rayHandler;
    private BitmapFont font;

    private ComponentMapper<TextureComponent> textureMapper;
    private ComponentMapper<BodyComponent> bodyMapper;


    public RenderSystem(OrthographicCamera camera, World world, RayHandler rayHandler, SpriteBatch batch) {
        super(Family.all(TextureComponent.class, BodyComponent.class).get(), new RenderComparator(),Constants.RENDER);
        this.batch = batch;
        this.camera = camera;
        this.world = world;
        this.rayHandler = rayHandler;

        textureMapper = ComponentMapper.getFor(TextureComponent.class);
        bodyMapper = ComponentMapper.getFor(BodyComponent.class);
        font = new BitmapFont();
        font.getData().setScale(1f * font.getScaleY() / font.getLineHeight());
    }

    @Override
    public void update(float deltaTime) {

        batch.setProjectionMatrix(camera.combined);

        super.update(deltaTime);

        rayHandler.setCombinedMatrix(camera);
        rayHandler.updateAndRender();
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        TextureComponent textureComponent = textureMapper.get(entity);
        Body body = bodyMapper.get(entity).body;

        Vector2 position = body.getPosition();


        batch.begin();
        if (textureComponent.texture != null && !textureComponent.renderAfterLight) {
            position.x = position.x - (float) textureComponent.texture.getWidth()/Constants.PPM/2;
            position.y = position.y - (float) textureComponent.texture.getHeight()/Constants.PPM/2;
            batch.draw(new TextureRegion(textureComponent.texture), position.x, position.y, (float) textureComponent.texture.getWidth()/Constants.PPM/2, (float) textureComponent.texture.getHeight()/Constants.PPM/2,
                    textureComponent.texture.getWidth()/Constants.PPM, textureComponent.texture.getHeight()/Constants.PPM,
                    1, 1, body.getAngle() * MathUtils.radiansToDegrees);

        }

        if (textureComponent.textureRegion != null && !textureComponent.renderAfterLight) {
            position.x = position.x - textureComponent.textureRegion.getRegionWidth()/Constants.PPM/2;
            position.y = position.y - textureComponent.textureRegion.getRegionHeight()/Constants.PPM/2;


            batch.draw(textureComponent.textureRegion, position.x + (textureComponent.flipTexture ? textureComponent.textureRegion.getRegionWidth()/Constants.PPM: 0), position.y, (float) textureComponent.textureRegion.getRegionWidth()/2, (float) textureComponent.textureRegion.getRegionHeight()/2,
                    textureComponent.textureRegion.getRegionWidth()/Constants.PPM * (textureComponent.flipTexture ? -1 : 1), textureComponent.textureRegion.getRegionHeight()/Constants.PPM,
                    1, 1, body.getAngle() * MathUtils.radiansToDegrees);
//            batch.draw(textureComponent.textureRegion, position.x, position.y,(float)textureComponent.textureRegion.getRegionWidth()/2,(float)textureComponent.textureRegion.getRegionHeight()/2,
//                     textureComponent.textureRegion.getRegionWidth()/32*(textureComponent.flipTexture ? -1 : 1), textureComponent.textureRegion.getRegionHeight()/32,
//                    1, 1, body.getAngle() * MathUtils.radiansToDegrees);
        }
        batch.end();

    }

    @Override
    public void dispose() {
        batch.dispose();
    }

}