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
    AnimatedBox2DSprite animatedBox2DSprite1;
    AnimatedSprite playerWalkSpriteAnim;
    AnimatedSprite animatedSprite1;
    Texture playerStand;
    Texture tex1;

    Box2DSprite box2DSprite;
    TextureRegion textureRegion;
    TextureAtlas textureAtlas1;
    TextureAtlas textureAtlas2;
    public AnimationSystem() {
        super(Family.all(PlayerComponent.class).get(), Constants.ANIMATION);
        textureAtlas1 = (TextureAtlas) AssetsLoader.getResource("player_move");
        textureAtlas2 = (TextureAtlas) AssetsLoader.getResource("player_move_start");
        playerStand = (Texture) AssetsLoader.getResource("player_stand");
        playerMapper = ComponentMapper.getFor(PlayerComponent.class);
        bodyMapper = ComponentMapper.getFor(BodyComponent.class);

        Animation walkAnimation = new Animation(1 / 10f,textureAtlas1.getRegions(),Animation.PlayMode.LOOP);
        Animation startwalkanimation = new Animation(1 / 10f,textureAtlas2.getRegions(),Animation.PlayMode.LOOP);
        animatedSprite1 = new AnimatedSprite(walkAnimation);
        AnimatedSprite animatedSprite = new AnimatedSprite(startwalkanimation);
                animatedBox2DSprite = new AnimatedBox2DSprite(animatedSprite1);
                animatedBox2DSprite1 = new AnimatedBox2DSprite(animatedSprite);
                animatedBox2DSprite.setScale(1f);
                box2DSprite = new Box2DSprite(playerStand);
                box2DSprite.setScale(1f);
        }

    @Override
    protected void processEntity(Entity entity, float deltaTime)
    {

        playerBody = bodyMapper.get(entity).body;
        Fixture fixture = playerBody.getFixtureList().get(3);


        if(StateManager.climbing){
            float velocityY  =  fixture.getBody().getLinearVelocity().y;
            if(velocityY != 0  ){
                fixture.setUserData(animatedBox2DSprite);
            }else {
                               box2DSprite.setTexture(playerStand);
                               fixture.setUserData(box2DSprite);
            }
        }
        if(!StateManager.climbing) {
            float velocityX = fixture.getBody().getLinearVelocity().x;
            box2DSprite.setTexture(playerStand);
            fixture.setUserData(box2DSprite);
            if (velocityX != 0) {
                fixture.setUserData(animatedBox2DSprite);
            }

            if (!StateManager.playerDirection) {
                box2DSprite.setFlip(true, false);
                if(!animatedBox2DSprite.isFlipX()) {
                    animatedBox2DSprite.flipFrames(true, false);
                }

            } else {
                box2DSprite.setFlip(false, false);
                if(animatedBox2DSprite.isFlipX()) {
                    System.out.println("adsasd");
                    animatedBox2DSprite.flipFrames(true, false);
                }

//                animatedBox2DSprite.flipFrames(false, false, true);


            }
        }
     //----------------------Player equip render--------------------
    }
}
