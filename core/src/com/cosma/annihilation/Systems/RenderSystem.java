package com.cosma.annihilation.Systems;

import box2dLight.RayHandler;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.cosma.annihilation.Components.BodyComponent;
import com.cosma.annihilation.Components.TextureComponent;
import com.cosma.annihilation.Utils.Constants;
import net.dermetfan.gdx.graphics.g2d.Box2DSprite;


public class RenderSystem extends IteratingSystem implements Disposable {


    private OrthographicCamera camera;
    private SpriteBatch batch;
    private World world;
    private ShaderProgram shaderOutline;
    private RayHandler rayHandler;
    private BitmapFont font;

    private ComponentMapper<TextureComponent> textureMapper;
    private ComponentMapper<BodyComponent> bodyMapper;



    public RenderSystem(OrthographicCamera camera, World world, RayHandler rayHandler,SpriteBatch batch) {
        super(Family.all(BodyComponent.class, TextureComponent.class).get(), Constants.RENDER);
        this.batch = batch;
        this.camera = camera;
        this.world = world;
        this.rayHandler = rayHandler;

        rayHandler.useDiffuseLight(true);
        textureMapper = ComponentMapper.getFor(TextureComponent.class);
        bodyMapper = ComponentMapper.getFor(BodyComponent.class);
        font = new BitmapFont();
        font.getData().setScale(1f * font.getScaleY() / font.getLineHeight());
        loadShader();
    }

    @Override
    public void update(float deltaTime) {

        batch.setProjectionMatrix(camera.combined);

        super.update(deltaTime);

        batch.begin();
        font.draw(batch,"Hello World",130,100);
        batch.end();
        rayHandler.setCombinedMatrix(camera);
        rayHandler.updateAndRender();
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        TextureComponent textureComponent = textureMapper.get(entity);
        Body body = bodyMapper.get(entity).body;

        Fixture fixture = body.getFixtureList().first();
        Vector2 position = body.getPosition();

//       if (textureComponent.texture == null || fixture == null) return;


//        position.x = position.x - textureComponent.texture.getWidth()/2;
//        position.y = position.y - textureComponent.texture.getHeight()/2;



        batch.begin();
        if (textureComponent.texture != null ) {
            position.x = position.x - (float)textureComponent.texture.getWidth()/32/2;
            position.y = position.y - (float)textureComponent.texture.getHeight()/32/2;
             batch.draw(new TextureRegion(textureComponent.texture), position.x, position.y, (float)textureComponent.texture.getWidth()/32/2, (float)textureComponent.texture.getHeight()/32/2,
                     textureComponent.texture.getWidth()/32, textureComponent.texture.getHeight()/32,
                     1, 1, body.getAngle() * MathUtils.radiansToDegrees);

        }

        if (textureComponent.texture_ != null ) {
            position.x = position.x - textureComponent.texture_.getRegionWidth()/32/2;
            position.y = position.y - textureComponent.texture_.getRegionHeight()/32/2;
            batch.draw(textureComponent.texture_, position.x, position.y,(float)textureComponent.texture_.getRegionWidth()/2,(float)textureComponent.texture_.getRegionHeight()/2,
                    textureComponent.texture_.getRegionWidth()/32, textureComponent.texture_.getRegionHeight()/32,
                    1, 1, body.getAngle() * MathUtils.radiansToDegrees);
        }
        batch.end();

        if (textureComponent.texture != null && textureComponent.renderWithShader) {
            shaderOutline.begin();
            shaderOutline.setUniformf("u_viewportInverse", new Vector2(1f / 32, 1f / 32));
            shaderOutline.setUniformf("u_offset", 0.4f);
            shaderOutline.setUniformf("u_step", Math.min(1f, 32 / 70f));
            shaderOutline.setUniformf("u_color", new Vector3(1, 1, 1));
            shaderOutline.end();
            batch.setShader(shaderOutline);
            batch.begin();
            position.x = position.x - (float)textureComponent.texture.getWidth()/32/2;
            position.y = position.y - (float)textureComponent.texture.getHeight()/32/2;
            batch.draw(new TextureRegion(textureComponent.texture), position.x, position.y, (float)textureComponent.texture.getWidth()/32/2, (float)textureComponent.texture.getHeight()/32/2,
                    textureComponent.texture.getWidth()/32, textureComponent.texture.getHeight()/32,
                    1, 1, body.getAngle() * MathUtils.radiansToDegrees);
            batch.end();
            batch.setShader(null);
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    private void loadShader() {
        String vertexShader;
        String fragmentShader;
        vertexShader = Gdx.files.internal("shader/df_vertex.glsl").readString();
        fragmentShader = Gdx.files.internal("shader/outline_border_fragment.glsl").readString();
        shaderOutline = new ShaderProgram(vertexShader, fragmentShader);
        if (!shaderOutline.isCompiled())
            throw new GdxRuntimeException("Couldn't compile shader: "
                    + shaderOutline.getLog());
    }
}