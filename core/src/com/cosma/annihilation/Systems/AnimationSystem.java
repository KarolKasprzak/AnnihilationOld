package com.cosma.annihilation.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.cosma.annihilation.Components.BodyComponent;
import com.cosma.annihilation.Components.PlayerComponent;
import com.cosma.annihilation.Utils.AssetsLoader;
import com.cosma.annihilation.Utils.Constants;
import com.cosma.annihilation.Utils.StateManager;
import net.dermetfan.gdx.graphics.g2d.AnimatedBox2DSprite;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;
import net.dermetfan.gdx.graphics.g2d.Box2DSprite;

public class AnimationSystem extends IteratingSystem {

    private ComponentMapper<PlayerComponent> playerMapper;
    private ComponentMapper<BodyComponent> bodyMapper;
    private Body playerBody;
    AnimatedBox2DSprite animatedBox2DSprite;
    AnimatedSprite animatedSprite;
    AnimatedSprite animatedSprite1;
    Texture tex1;
    Texture tex2;
    Texture tex3;
    Texture tex4;
    Texture tex5;
    Box2DSprite box2DSprite;
    public AnimationSystem() {
        super(Family.all(PlayerComponent.class).get(), Constants.ANIMATION);

        tex1 = (Texture) AssetsLoader.getResource("pl_1");
        tex2 = (Texture) AssetsLoader.getResource("pl_2");
        tex3 = (Texture) AssetsLoader.getResource("pl_3");
        tex4 = (Texture) AssetsLoader.getResource("pl_4");
        tex5 = (Texture) AssetsLoader.getResource("pl_5");
        playerMapper = ComponentMapper.getFor(PlayerComponent.class);
        bodyMapper = ComponentMapper.getFor(BodyComponent.class);
        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("character/unnamed.atlas"));
        Animation climbAnimation = new Animation(1 / 3f,textureAtlas.getRegions());

        climbAnimation.setPlayMode(Animation.PlayMode.LOOP);
        animatedSprite1 = new AnimatedSprite(climbAnimation);
                animatedBox2DSprite = new AnimatedBox2DSprite(animatedSprite1);
                animatedBox2DSprite.setAnimation(climbAnimation);
                animatedBox2DSprite.setScale(1.4f);
                box2DSprite = new Box2DSprite(tex3);
                box2DSprite.setScale(1.4f);

        }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        playerBody = bodyMapper.get(entity).body;
        Fixture fixture = playerBody.getFixtureList().get(3);
        if(StateManager.climbing){
            float velocityY =  fixture.getBody().getLinearVelocity().y;
            if(velocityY != 0  ){

                fixture.setUserData(animatedBox2DSprite);

            }else {
                               box2DSprite.setTexture(tex3);
                               fixture.setUserData(box2DSprite);
            }
        }
        if(!StateManager.climbing){
           box2DSprite.setTexture(tex1);
            fixture.setUserData(box2DSprite);
        }

    }
}
