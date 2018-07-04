package com.cosma.annihilation.Utils;

import com.badlogic.gdx.math.Vector2;

public class Constants {
    // --------------- UI / Window ------------

    public static final int DEFAULT_SCREEN_WIDTH = 800;
    public static final int DEFAULT_SCREEN_HEIGHT = 600;

    // -------------- Physics -----------------
    // -------------- Physics -----------------

    public static final float METRE_TO_PIXEL = 64f;
    public static final float PIXEL_TO_METRE = 1 / METRE_TO_PIXEL;

    public static final int WORLD_WIDTH  = (int) (DEFAULT_SCREEN_WIDTH * PIXEL_TO_METRE)+5;
    public static final int WORLD_HEIGHT = (int) (DEFAULT_SCREEN_HEIGHT * PIXEL_TO_METRE)+5;

    public static final float BOX2D_FPS = 60.0f;
    public static final int BOX2D_VELOCITY_ITERATIONS = 6;
    public static final int BOX2D_POSITION_ITERATIONS = 2;

    public static final Vector2 WORLD_GRAVITY = new Vector2(0, -10);
}
