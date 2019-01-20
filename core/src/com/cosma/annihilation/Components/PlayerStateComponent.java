package com.cosma.annihilation.Components;

import com.badlogic.ashley.core.Component;

public class PlayerStateComponent implements Component {
    public float time = 0f;

    public boolean canJump = true;
    public boolean onGround = false;
    public boolean climbing = false;
    public boolean canClimb = false;
    //------Control-----
    public boolean goLeft = false;
    public boolean goRight = false;
    public boolean goUp = false;
    public boolean goDown = false;

    public boolean canMoveOnSide = true;
    public boolean canClimbDown = false;


    /** right = true, left = false  */
    public boolean playerDirection = true;
}
