package com.cosma.annihilation.Utils;

import com.badlogic.gdx.Gdx;

public class Utilities {


    public static float setWindowWidth(float scale){
        return (Gdx.app.getGraphics().getWidth())*scale;
    }

    public static float setWindowHeight(float scale){
        return (Gdx.app.getGraphics().getWidth())*scale;
    }

    public static float getButtonWidth(float scale){
        return (Gdx.app.getGraphics().getWidth()/12.8f)*scale;
    }

    public static float getButtonHeight(float scale){
        return (Gdx.graphics.getHeight()/24)*scale;
    }

}
