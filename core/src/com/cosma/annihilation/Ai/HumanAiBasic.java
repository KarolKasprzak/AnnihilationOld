package com.cosma.annihilation.Ai;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.cosma.annihilation.Components.AiComponent;

public class HumanAiBasic extends AiCore implements ArtificialIntelligence {

    private Vector2 startPosition;
    private String aiStatus = "";

    public HumanAiBasic() {

    }

    @Override
    public void update(Entity entity) {
        AiComponent aiComponent = entity.getComponent(AiComponent.class);
        if (isEnemyInSight(entity)) {
            aiStatus = "enemy!!!";
            if (isEnemyInWeaponRange(entity, 8)) {
                shoot(entity);
            } else {
                followEnemy(entity);
                aiStatus = "follow enemy";
            }
        } else {
            if (isHearEnemy(entity)) {
                searchEnemy(entity);
                aiStatus = "hear enemy";
            }else{
                patrol(entity);
                aiStatus = "patrol";
            }
        }
    }


    @Override
    public String getStatus() {
        return aiStatus;
    }
}
