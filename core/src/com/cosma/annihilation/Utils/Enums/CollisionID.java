package com.cosma.annihilation.Utils.Enums;

import com.badlogic.gdx.physics.box2d.Filter;

public class CollisionID {
    public static final short CAST_SHADOW = 0x0001;
    public static final short NO_SHADOW = 0x0002;
    public static final short SCENERY = 0x0004;
    public static final short JUMPABLE_OBJECT = 0x0008;
    public static final short PLAYER = 0x0016;
    public static final short OPONENTS = 0x0032;
    public static final short BACKGROUND = 0x0064;


    public static final short MASK_PLAYER = SCENERY | JUMPABLE_OBJECT | OPONENTS;


}
