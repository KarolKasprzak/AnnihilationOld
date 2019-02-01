package com.cosma.annihilation.Utils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class Utilities {


    public static float setWindowWidth(float scale){
        return (Gdx.app.getGraphics().getWidth())*scale;
    }

    public static float setWindowHeight(float scale){
        return (Gdx.app.getGraphics().getHeight())*scale;
    }

    public static float setButtonWidth(float scale){
        return (Gdx.app.getGraphics().getWidth()/12.8f)*scale;
    }

    public static float setButtonHeight(float scale){
        return (Gdx.graphics.getHeight()/24)*scale;
    }

    public  static float setFontScale(float scale) {return (Gdx.graphics.getHeight()/500)*scale; }

    public static void setButtonColor(TextButton button){
        button.getLabel().setColor(0,82,0,255);
    }




}
