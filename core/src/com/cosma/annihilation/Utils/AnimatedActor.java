package com.cosma.annihilation.Utils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;


public class AnimatedActor extends Image {
    private float stateTime = 0;
    AnimatedSprite animatedSprite;
    Animation<TextureRegion> animation;

    public AnimatedActor(Animation animation) {
        this.animation = animation;
        animation.getKeyFrame(stateTime);
        animation.setPlayMode(Animation.PlayMode.LOOP);
    }


    @Override
    public void act(float delta) {
        setDrawable(new TextureRegionDrawable(animation.getKeyFrame(delta+=stateTime,true)));
        System.out.println(animation.getKeyFrameIndex(delta));
        super.act(delta);
    }
}
