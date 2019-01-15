package com.cosma.annihilation.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.cosma.annihilation.Components.BodyComponent;
import com.cosma.annihilation.Components.PlayerComponent;
import com.cosma.annihilation.Utils.*;
import net.dermetfan.gdx.graphics.g2d.AnimatedBox2DSprite;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;
import net.dermetfan.gdx.graphics.g2d.Box2DSprite;

public class AnimationSystem extends IteratingSystem {

    private ComponentMapper<PlayerComponent> playerMapper;
    private ComponentMapper<BodyComponent> bodyMapper;
    private Body playerBody;
    private AssetLoader assetLoader;
    AnimatedBox2DSprite animatedBox2DSprite;
    AnimatedBox2DSprite animatedBox2DSprite1;
    AnimatedSprite playerWalkSpriteAnim;
    AnimatedSprite animatedSprite1;
    Texture playerStand;
    Texture playerWeaponStand;
    Texture tex1;
    Animation walkAnimation;
    Animation startwalkanimation;
    Box2DSprite box2DSprite;
    TextureRegion textureRegion;
    TextureAtlas textureAtlas1;
    TextureAtlas textureAtlas2;
    private PlayerComponent playerComponent;

    public AnimationSystem(AssetLoader assetLoader) {
        super(Family.all(PlayerComponent.class).get(), Constants.ANIMATION);
        this.assetLoader = assetLoader;
        textureAtlas1 = (TextureAtlas) LoaderOLD.getResource("player_move");
        textureAtlas2 = (TextureAtlas) LoaderOLD.getResource("player_move_start");
        playerStand = (Texture) LoaderOLD.getResource("player_stand");
        playerWeaponStand = (Texture) LoaderOLD.getResource("player_weapon");
        playerMapper = ComponentMapper.getFor(PlayerComponent.class);
        bodyMapper = ComponentMapper.getFor(BodyComponent.class);

        walkAnimation = new Animation(1 / 10f,textureAtlas1.getRegions(),Animation.PlayMode.LOOP);
        startwalkanimation = new Animation(1 / 10f,textureAtlas2.getRegions());
        animatedSprite1 = new AnimatedSprite(startwalkanimation);
                animatedBox2DSprite = new AnimatedBox2DSprite(animatedSprite1);
                animatedBox2DSprite.setScale(1f);
                box2DSprite = new Box2DSprite(playerStand);
                box2DSprite.setScale(1f);
        }

    @Override
    protected void processEntity(Entity entity, float deltaTime)
    {
        playerComponent = playerMapper.get(entity);

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
            if(!playerComponent.isWeaponHidden){
                int weaponTag = 1;
                if(playerComponent.activeWeapon != null) {
                    weaponTag = playerComponent.activeWeapon.getItemUseType();
                }

                switch (weaponTag) {
                    case 32:
                        box2DSprite.setTexture(playerWeaponStand);
                        break;
                    case 64:
//                        box2DSprite.setTexture(assetLoader.manager.get(GfxPlayerAssetDescriptors.player_stand_rifle));
                        box2DSprite.setTexture((Texture) assetLoader.manager.get("gfx/player/player_rifle_takeout.png"));
                        break;
                }
                fixture.setUserData(box2DSprite);
                if(!playerComponent.isWeaponHidden){
                    box2DSprite.setTexture(assetLoader.manager.get(GfxPlayerAssetDescriptors.player_stand_pistol));
                    fixture.setUserData(box2DSprite);
                }
            }
            if (velocityX != 0) {
                fixture.setUserData(animatedBox2DSprite);
                animatedBox2DSprite.setAnimation(walkAnimation);
            }

            if (!StateManager.playerDirection) {
                box2DSprite.setFlip(true, false);
                if(!animatedBox2DSprite.isFlipX()) {
                    animatedBox2DSprite.flipFrames(true, false);
                }

            } else {
                box2DSprite.setFlip(false, false);
                if(animatedBox2DSprite.isFlipX()) {
                    animatedBox2DSprite.flipFrames(true, false);
                }

//                animatedBox2DSprite.flipFrames(false, false, true);


            }
        }
     //----------------------Player equip render--------------------
    }
}
