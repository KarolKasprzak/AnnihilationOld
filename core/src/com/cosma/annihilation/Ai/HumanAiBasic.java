package com.cosma.annihilation.Ai;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.cosma.annihilation.Components.AiComponent;

public class HumanAiBasic extends AiCore implements ArtificialIntelligence{

    private Vector2 startPosition;
    private String aiStatus = "";

    public HumanAiBasic() {

    }

    @Override
    public void update(Entity entity) {
        System.out.println(entity.getComponent(AiComponent.class).isHearEnemy);
        if(isEnemyInSight(entity)){
            if(isEnemyInWeaponRange(entity,5)){
                 shoot(entity);
            }else{
                followEnemy(entity);
            }
        }else{
            patrol(entity);
        }
        isHearEnemy(entity);
    }

    @Override
    public String getStatus() {
        return aiStatus;
    }
}
