package com.cosma.annihilation.Components;

import com.badlogic.ashley.core.Component;

public class PlayerComponent implements Component {
    public boolean onGround = false;
    public boolean climbing = false;
    public boolean canClimb = false;
    public boolean playerDirection = false;
}
