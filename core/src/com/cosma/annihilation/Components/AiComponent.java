package com.cosma.annihilation.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.cosma.annihilation.Ai.ArtificialIntelligence;
import com.cosma.annihilation.Ai.HumanAi;

public class AiComponent implements Component {
    public boolean isActive = false;
    public boolean reactToPlayerWeapon;
    public boolean onPatrol;
    public Vector2 startPosition = new Vector2();

    public ArtificialIntelligence ai = new HumanAi();

}
