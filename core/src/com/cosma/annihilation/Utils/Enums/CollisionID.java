package com.cosma.annihilation.Utils.Enums;

public class CollisionID {
    public static final short LIGHT = 0x0001;
    public static final short PLAYER = 0x0002;
    public static final short SCENERY = 0x0004;
    public static final short SCENERY_BACKGROUND_OBJECT = 0x0008;
    public static final short ENEMY = 0x0016;
    public static final short NPC = 0x0032;
    public static final short SCENERY_PHYSIC_OBJECT = 0x0064;

    public static final short MASK_LIGHT = SCENERY;
    public static final short MASK_PLAYER = ENEMY|SCENERY;
    public static final short MASK_SCENERY = -1;
    public static final short MASK_SCENERY_PHYSIC_OBJECT = -1;
    public static final short MASK_SCENERY_BACKGROUND_OBJECT = SCENERY;
    public static final short MASK_ENEMY = SCENERY|PLAYER;
    public static final short MASK_NPC = SCENERY|ENEMY;

}
