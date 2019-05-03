package com.cosma.annihilation.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.cosma.annihilation.Annihilation;

import java.util.ArrayList;
import java.util.HashMap;

public class AnimationFactory {

    private HashMap<AnimationId,HashMap> animationMap;

    public AnimationFactory() {
        animationMap = new HashMap<>();

        //Player animations
        HashMap<String,Animation> playerAnimationMap = new HashMap<>();
        Animation<TextureRegion> playerWalkAnimation = new Animation(0.1f,Annihilation.getAssets().get("gfx/player_move.atlas",TextureAtlas.class).getRegions()
                ,Animation.PlayMode.LOOP);
        playerAnimationMap.put("WALK",playerWalkAnimation);

        TextureRegion standNoWeapon = new TextureRegion(Annihilation.getAssets().get("gfx/player/player_noweapon_stand.png",Texture.class));
        Animation<TextureRegion> playerIdleAnimation = new Animation(0.1f,standNoWeapon);
        playerAnimationMap.put("IDLE",playerIdleAnimation);

        TextureRegion jumpNoWeapon = new TextureRegion(Annihilation.getAssets().get("gfx/player/player_jump.png",Texture.class));
        Animation<TextureRegion> playerJumpAnimation = new Animation(0.1f,jumpNoWeapon);
        playerAnimationMap.put("JUMP",playerJumpAnimation);

        Animation<TextureRegion> playerMeleeAnimation = new Animation(1/6f,Annihilation.getAssets().get(GfxAssetDescriptors.player_attack_melee).getRegions(), Animation.PlayMode.NORMAL);
        playerAnimationMap.put("MELEE",playerMeleeAnimation);

        animationMap.put(AnimationId.PLAYER,playerAnimationMap);




    }
    public HashMap<String,Animation<TextureRegion>> createAnimationMap(AnimationId animationId){
        if(animationMap.keySet().contains(animationId)){
            return  animationMap.get(animationId);
        }
        Gdx.app.log("MyTag", "animation map not found");
        return null;
    }
    public enum AnimationId{
        PLAYER
    }
}
