package com.cosma.annihilation.Systems;

import box2dLight.RayHandler;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.cosma.annihilation.Components.TextureComponent;
import com.cosma.annihilation.Components.TransformComponent;
import com.cosma.annihilation.Utils.Constants;
import net.dermetfan.gdx.graphics.g2d.Box2DSprite;


public class RenderSystem extends IteratingSystem implements Disposable {


    private OrthographicCamera camera;
    private SpriteBatch batch;
    private World world;
    private ShaderProgram shaderOutline;
    private RayHandler rayHandler;

    private ComponentMapper<TextureComponent> textureMapper;
    private ComponentMapper<TransformComponent> transformMapper;


    public RenderSystem(OrthographicCamera camera, World world, RayHandler rayHandler) {
        super(Family.all(TransformComponent.class, TextureComponent.class).get(), Constants.RENDER);
        this.camera = camera;
        this.world = world;
        this.rayHandler = rayHandler;
        batch = new SpriteBatch();
        rayHandler.useDiffuseLight(true);

        textureMapper = ComponentMapper.getFor(TextureComponent.class);
        transformMapper = ComponentMapper.getFor(TransformComponent.class);
        loadShader();
    }

    @Override
    public void update(float deltaTime) {
        batch.setProjectionMatrix(camera.combined);
        super.update(deltaTime);
        batch.begin();
        Box2DSprite.draw(batch, world);
        batch.end();
        rayHandler.setCombinedMatrix(camera);
        rayHandler.updateAndRender();
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        TransformComponent transformComponent = transformMapper.get(entity);
        TextureComponent textureComponent = textureMapper.get(entity);

        batch.begin();
        if (textureComponent.texture != null ) {

            batch.draw(textureComponent.texture, transformComponent.position.x - transformComponent.sizeX / 2, transformComponent.position.y - transformComponent.sizeY / 2,
                    textureComponent.texture.getHeight() / 32, textureComponent.texture.getWidth() / 32);
//            batch.draw(textureRegion, x, y, width, height, width, height, 1f, 1f, angle);

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
            batch.draw(textureComponent.texture, transformComponent.position.x - transformComponent.sizeX / 2, transformComponent.position.y - transformComponent.sizeY / 2,
                    textureComponent.texture.getHeight() / 32, textureComponent.texture.getWidth() / 32);
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