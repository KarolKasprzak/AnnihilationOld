package com.cosma.annihilation.Utils;

import com.badlogic.gdx.Gdx;
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
        playerAnimationMap.put("walk",playerWalkAnimation);

        Animation<TextureRegion> playerMeleeAnimation = new Animation(0.1f,Annihilation.getAssets().get("gfx/player/player_melee.atlas",TextureAtlas.class).getRegions()
                ,Animation.PlayMode.LOOP);
        playerAnimationMap.put("melee",playerMeleeAnimation);
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
