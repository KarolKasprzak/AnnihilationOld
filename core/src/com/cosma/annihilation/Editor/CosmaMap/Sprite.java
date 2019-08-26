package com.cosma.annihilation.Editor.CosmaMap;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FileTextureData;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.cosma.annihilation.Annihilation;

public class Sprite{
    private float x,y,width,height,angle;
    private String atlasRegionName,atlasPath;
    private TextureRegion textureRegion;
    private boolean flipX = false,flipY = false;

    public boolean isFlipX() {
        return flipX;
    }

    public boolean isFlipY() {
        return flipY;
    }

    public Sprite() {
    }

     void setTextureRegion(String region, String path) {
        for(String string: Annihilation.getAssets().getAssetNames()){
            if(string.contains(path)){
                this.textureRegion = Annihilation.getAssets().get(string,TextureAtlas.class).findRegion(region);
                this.atlasRegionName= region;
                this.width = textureRegion.getRegionWidth()/32;
                this.height = textureRegion.getRegionHeight()/32;
                break;
            }else{
                System.out.println("texture not loaded");
            }
        }
    }



    void setSpritePosition(float x,float y, float angle){
        this.x = x;
        this.y = y;
        this.angle = angle;
    }

    String getTextureDate(){
        return ((FileTextureData)textureRegion.getTexture().getTextureData()).getFileHandle().nameWithoutExtension()+".atlas"+","+atlasRegionName;
    }

    public TextureRegion getTextureRegion() {
        return textureRegion;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getAngle() {
        return angle;
    }
}
