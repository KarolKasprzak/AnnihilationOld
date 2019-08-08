package com.cosma.annihilation.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.cosma.annihilation.Ai.ArtificialIntelligence;
import com.cosma.annihilation.Ai.HumanAi;

public class AiComponent implements Component {

    public Vector2 startPosition = new Vector2();
    public int patrolRange = 3;
    public ArtificialIntelligence ai;

}
