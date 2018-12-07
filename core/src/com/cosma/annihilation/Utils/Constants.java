package com.cosma.annihilation.Utils;

import com.badlogic.gdx.math.Vector2;

public class Constants {
    // --------------- UI / Window ------------



    // -------------- Physics -----------------
    // -------------- Physics -----------------

    public static final float PPM = 32f;
    public static final float PIXEL_TO_METRE = 1 / PPM;



    public static final float BOX2D_FPS = 60.0f;
    public static final int BOX2D_VELOCITY_ITERATIONS = 6;
    public static final int BOX2D_POSITION_ITERATIONS = 2;

    public static final Vector2 WORLD_GRAVITY = new Vector2(0, -10);
    public static final   short PLAYER_COLIDED = 0x1;
    public static final   short  NOT_COLIDED= 0x2;

    //--------ENGINE-----------
    public static final int PHYSIC_SYSTEM                  = 1;
    public static final int COLLISION_SYSTEM               = 2;
    public static final int PLAYER_CONTROL_SYSTEM          = 3;
    public static final int CAMERA_SYSTEM                  = 4;
    public static final int ANIMATION                      = 5;
    public static final int TILE_MAP_RENDER                = 6;
    public static final int RENDER                         = 12;
    public static final int HEALTHSYSTEM                   = 8;
    public static final int LIGHT_RENDER                   = 9;
    public static final int DEBUG_RENDER                   = 11;

}
