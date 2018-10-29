package com.cosma.annihilation.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class StateManager {
    public static boolean canJump = true;
    public static boolean onGround = false;
    public static boolean climbing = false;
    public static boolean canClimb = false;
    public static boolean pause = false;
    //------Control-----
    public static boolean goLeft = false;
    public static boolean goRight = false;
    public static boolean goUp = false;
    public static boolean goDown = false;


    public static boolean debugMode = false;
    public static boolean debugModeGui = false;
    public static boolean canMoveOnSide = true;
    public static boolean canClimbDown = false;

    // false = left side
    // true = right side
    public static boolean playerDirection = true;
    public static boolean Open = false;


//    public static Skin skin = new Skin(Gdx.files.internal("interface/skin/pixthulhu-ui.json"));
    public static Skin skin = new Skin(Gdx.files.internal("interface/comadore/uiskin.json"));
    }
