package com.cosma.annihilation.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.cosma.annihilation.Items.WeaponItem;
import com.cosma.annihilation.Utils.Enums.AnimationStates;

import java.util.ArrayList;

public class PlayerComponent implements Component {

    public float velocity = 2;
    public int numFootContacts = 0;
    public boolean hidde = false;
    public String mapName;

    public ArrayList<Entity> collisionEntityList = new ArrayList<Entity>();
    public WeaponItem activeWeapon;
    public int activeWeaponAmmo;
    public Entity processedEntity;

    public boolean isWeaponHidden = true;

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
    public boolean crouch = false;


}
