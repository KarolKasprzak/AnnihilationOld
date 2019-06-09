package com.cosma.annihilation.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.cosma.annihilation.Annihilation;

import java.util.HashMap;

public class AnimationFactory {

    private HashMap<AnimationId,HashMap> animationMap;

    public AnimationFactory() {
        animationMap = new HashMap<>();

        //Player animations
        HashMap<String,Animation> playerAnimationMap = new HashMap<>();

        Animation<TextureRegion> playerWalkAnimation = new Animation(0.1f,
                Annihilation.getAssets().get("gfx/player/player_move.atlas",TextureAtlas.class).getRegions()
                ,Animation.PlayMode.LOOP);
        playerAnimationMap.put("WALK",playerWalkAnimation);

        Animation<TextureRegion> playerWalkSmallWeaponAnimation = new Animation(0.1f,
                Annihilation.getAssets().get("gfx/player/walk_weapon_small.atlas",TextureAtlas.class).getRegions()
                ,Animation.PlayMode.LOOP);
        playerAnimationMap.put("WALK_WEAPON_SMALL",playerWalkSmallWeaponAnimation);

        Animation<TextureRegion> playerWeaponMpWalkAnimation = new Animation(0.1f,
                Annihilation.getAssets().get("gfx/player/player_weapon_mp_walk.atlas",TextureAtlas.class).getRegions()
                ,Animation.PlayMode.LOOP);
        playerAnimationMap.put("WALK_WEAPON_MP",playerWeaponMpWalkAnimation);

        TextureRegion standNoWeapon = new TextureRegion(Annihilation.getAssets().get("gfx/player/player_stand.png",Texture.class));
        Animation<TextureRegion> playerIdleAnimation = new Animation(0.1f,standNoWeapon);
        playerAnimationMap.put("IDLE",playerIdleAnimation);

        TextureRegion standWeaponSmall = new TextureRegion(Annihilation.getAssets().get("gfx/player/player_small_weapons_stand.png",Texture.class));
        Animation<TextureRegion> playerIdleWeaponSmallAnimation = new Animation(0.1f,standWeaponSmall);
        playerAnimationMap.put("IDLE_WEAPON_SMALL",playerIdleWeaponSmallAnimation);

        TextureRegion jumpNoWeapon = new TextureRegion(Annihilation.getAssets().get("gfx/player/player_jump.png",Texture.class));
        Animation<TextureRegion> playerJumpAnimation = new Animation(0.1f,jumpNoWeapon);
        playerAnimationMap.put("JUMP",playerJumpAnimation);

        Animation<TextureRegion> playerMeleeAnimation = new Animation(0.1f,Annihilation.getAssets().get("gfx/player/player_melee.atlas",
                TextureAtlas.class).getRegions(),Animation.PlayMode.NORMAL);
        playerAnimationMap.put("MELEE",playerMeleeAnimation);

        //Test enemy animation
        HashMap<String,Animation> zombieAnimationMap = new HashMap<>();
        TextureRegion zombieIdleTexture = new TextureRegion(Annihilation.getAssets().get("gfx/textures/enemy.png",Texture.class));
        Animation<TextureRegion> zombieIdle = new Animation(0.1f,zombieIdleTexture);
        zombieAnimationMap.put("IDLE",zombieIdle);





        animationMap.put(AnimationId.PLAYER,playerAnimationMap);
        animationMap.put(AnimationId.TEST_ZOMBIE,zombieAnimationMap);



    }
    public HashMap createAnimationMap(AnimationId animationId){
        if(animationMap.keySet().contains(animationId)){
            return  animationMap.get(animationId);
        }
        Gdx.app.log("MyTag", "animation map not found");
        return null;
    }
    public enum AnimationId{
        PLAYER,TEST_ZOMBIE
    }
}
