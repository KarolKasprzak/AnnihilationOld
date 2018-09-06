package com.cosma.annihilation.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

public class PlayerComponent implements Component {
    public float velocity = 2;
    public int numFootContacts = 0;
    public boolean hidde = false;
    public Entity collisionEntity;

}
