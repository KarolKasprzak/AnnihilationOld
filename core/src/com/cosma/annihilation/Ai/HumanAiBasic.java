package com.cosma.annihilation.Ai;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;

public class HumanAiBasic extends AiCore implements ArtificialIntelligence{

    private Vector2 startPosition;
    private String aiStatus = "";

    public HumanAiBasic() {

    }

    @Override
    public void update(Entity entity) {
        patrol(entity);
    }

    @Override
    public String getStatus() {
        return aiStatus;
    }
}
