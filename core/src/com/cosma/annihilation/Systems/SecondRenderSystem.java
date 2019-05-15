package com.cosma.annihilation.Systems;

import box2dLight.RayHandler;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.cosma.annihilation.Components.BodyComponent;
import com.cosma.annihilation.Components.TextureComponent;
import com.cosma.annihilation.Utils.Constants;


public class SecondRenderSystem extends IteratingSystem implements Disposable {


    private OrthographicCamera camera;
    private SpriteBatch batch;
    private ShaderProgram shaderOutline2;


    private ComponentMapper<TextureComponent> textureMapper;
    private ComponentMapper<BodyComponent> bodyMapper;



    public SecondRenderSystem(OrthographicCamera camera, SpriteBatch batch) {
        super(Family.all(BodyComponent.class, TextureComponent.class).get(), 12);
        this.batch = batch;
        this.camera = camera;

        textureMapper = ComponentMapper.getFor(TextureComponent.class);
        bodyMapper = ComponentMapper.getFor(BodyComponent.class);

        loadShader();


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
        TextureComponent textureComponent = textureMapper.get(entity);
        Body body = bodyMapper.get(entity).body;

        Vector2 position = body.getPosition();


        if (textureComponent.texture != null && textureComponent.renderWithShader) {
            shaderOutline2.begin();
            shaderOutline2.setUniformf("u_viewportInverse", new Vector2(1f / 32, 1f / 32));
            shaderOutline2.setUniformf("u_offset", 0.3f);
            shaderOutline2.setUniformf("u_step", Math.min(1f, 0.1f));
            shaderOutline2.setUniformf("u_color", new Vector3(1, 1, 1));
            shaderOutline2.end();
            batch.setShader(shaderOutline2);
            position.x = position.x - (float)textureComponent.texture.getWidth()/32/2;
            position.y = position.y - (float)textureComponent.texture.getHeight()/32/2;
            batch.draw(new TextureRegion(textureComponent.texture), position.x, position.y, (float)textureComponent.texture.getWidth()/32/2, (float)textureComponent.texture.getHeight()/32/2,
                    textureComponent.texture.getWidth()/32, textureComponent.texture.getHeight()/32,
                    1, 1, body.getAngle() * MathUtils.radiansToDegrees);
            batch.setShader(null);
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        shaderOutline2.dispose();
    }

    private void loadShader() {
        String vertexShader;
        String fragmentShader;
        vertexShader = Gdx.files.internal("shader/df_vertex.glsl").readString();
        fragmentShader = Gdx.files.internal("shader/outline_border_fragment.glsl").readString();

        shaderOutline2 = new ShaderProgram(vertexShader, fragmentShader);
        if (!shaderOutline2.isCompiled())
            throw new GdxRuntimeException("Couldn't compile shader: "
                    + shaderOutline2.getLog());
    }

}