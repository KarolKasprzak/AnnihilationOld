package com.cosma.annihilation.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.cosma.annihilation.Components.*;
import com.cosma.annihilation.Utils.*;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;

public class AnimationSystem extends IteratingSystem {

    private ComponentMapper<PlayerComponent> playerMapper;
    private ComponentMapper<BodyComponent> bodyMapper;
    private ComponentMapper<TextureComponent> textureMapper;
    private ComponentMapper<PlayerStateComponent> stateMapper;
    private ComponentMapper<AnimationComponent> animationMapper;

    private Body playerBody;
    private AssetLoader assetLoader;


    Animation<TextureRegion> walkAnimation;
    Animation startwalkanimation;
    AnimatedSprite animatedSprite;


    private PlayerComponent playerComponent;

    public AnimationSystem(AssetLoader assetLoader) {
        super(Family.all(PlayerComponent.class, TextureComponent.class, PlayerStateComponent.class).get(), Constants.ANIMATION);
        this.assetLoader = assetLoader;
        playerMapper = ComponentMapper.getFor(PlayerComponent.class);
        bodyMapper = ComponentMapper.getFor(BodyComponent.class);
        textureMapper = ComponentMapper.getFor(TextureComponent.class);
        stateMapper = ComponentMapper.getFor(PlayerStateComponent.class);
        animationMapper = ComponentMapper.getFor(AnimationComponent.class);
         TextureAtlas textureAtlasWalk = (TextureAtlas) LoaderOLD.getResource("player_move");
//        textureAtlas2 = (TextureAtlas) LoaderOLD.getResource("player_move_start");
//        playerStand = (Texture) LoaderOLD.getResource("player_stand");
//        playerWeaponStand = (Texture) LoaderOLD.getResource("player_weapon");


        walkAnimation = new Animation(0.1f, textureAtlasWalk.getRegions(), Animation.PlayMode.LOOP);

//        startwalkanimation = new Animation(1 / 10f, textureAtlas2.getRegions());
//        animatedSprite1 = new AnimatedSprite(startwalkanimation);

        animatedSprite = new AnimatedSprite(walkAnimation);
        animatedSprite.getAnimation().getKeyFrame(animatedSprite.getTime());


    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        playerComponent = playerMapper.get(entity);
        PlayerStateComponent stateComponent = stateMapper.get(entity);
        TextureComponent textureComponent = textureMapper.get(entity);
        AnimationComponent animComponent = animationMapper.get(entity);
        playerBody = bodyMapper.get(entity).body;

        if(!stateComponent.playerDirection){
            textureComponent.flipTexture = true;
            System.out.println(textureComponent.flipTexture);
        }else{
            textureComponent.flipTexture = false;
            System.out.println(textureComponent.flipTexture);
        }

        animComponent.time += deltaTime;

//        if(textureComponent.texture_ != null){
//            if (stateComponent.playerDirection) {
//                if (textureComponent.texture_.isFlipX()) {
//                    textureComponent.texture_.flip(true, false);
//                }
//            } else {
//                if (!textureComponent.texture_.isFlipX()) {
//                    textureComponent.texture_.flip(true, false);
//                }
//            }
//        }




        if (animationMapper.get(entity).currentAnimation != null) {
            textureComponent.texture_ = animComponent.currentAnimation.getKeyFrame(animComponent.time);
        }

        if (!stateComponent.climbing) {
            float velocityX = playerBody.getLinearVelocity().x;
            if (velocityX != 0) {
                textureComponent.texture_ = walkAnimation.getKeyFrame(animComponent.time);
            }
        }

//            }
//        }

//        if(StateManager.climbing){
//            float velocityY  =  fixture.getBody().getLinearVelocity().y;
//            if(velocityY != 0  ){
//                fixture.setUserData(animatedBox2DSprite);
//            }else {
//                               box2DSprite.setTexture(playerStand);
//                               fixture.setUserData(box2DSprite);
//
//            }
//        }
//        if(!StateManager.climbing) {
//            float velocityX = fixture.getBody().getLinearVelocity().x;
//            box2DSprite.setTexture(playerStand);
//            fixture.setUserData(box2DSprite);
//            if(!playerComponent.isWeaponHidden){
//                int weaponTag = 1;
//                if(playerComponent.activeWeapon != null) {
//                    weaponTag = playerComponent.activeWeapon.getItemUseType();
//                }
//
//                switch (weaponTag) {
//                    case 32:
//                        box2DSprite.setTexture(playerWeaponStand);
//                        break;
//                    case 64:
////                        box2DSprite.setTexture(assetLoader.manager.get(GfxPlayerAssetDescriptors.player_stand_rifle));
//                        box2DSprite.setTexture((Texture) assetLoader.manager.get("gfx/player/player_rifle_takeout.png"));
//                        break;
//                }
//                fixture.setUserData(box2DSprite);
//                if(!playerComponent.isWeaponHidden){
//                    box2DSprite.setTexture(assetLoader.manager.get(GfxPlayerAssetDescriptors.player_stand_pistol));
//                    fixture.setUserData(box2DSprite);
//                }
//            }
//            if (velocityX != 0) {
//                fixture.setUserData(animatedBox2DSprite);
//                animatedBox2DSprite.setAnimation(walkAnimation);
//            }
//
//            if (!StateManager.playerDirection) {
//                box2DSprite.setFlip(true, false);
//                if(!animatedBox2DSprite.isFlipX()) {
//                    animatedBox2DSprite.flipFrames(true, false);
//                }
//
//            } else {
//                box2DSprite.setFlip(false, false);
//                if(animatedBox2DSprite.isFlipX()) {
//                    animatedBox2DSprite.flipFrames(true, false);
//                }
//
////                animatedBox2DSprite.flipFrames(false, false, true);
//
//
//            }
//        }
                //----------------------Player equip render--------------------
            }
        }
