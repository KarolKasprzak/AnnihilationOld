package com.cosma.annihilation.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.cosma.annihilation.Components.BodyComponent;
import com.cosma.annihilation.Components.PlayerComponent;
import com.cosma.annihilation.Utils.Enums.GameEvent;

public class ShootingSystem extends IteratingSystem implements Listener<GameEvent> {
    private ComponentMapper<BodyComponent> bodyMapper;
    private ComponentMapper<PlayerComponent> playerMapper;
    private Body playerBody;
    private World world;
    private PlayerComponent playerComponent;

    public ShootingSystem(World world) {
        super(Family.all(PlayerComponent.class).get(),11);
        this.world = world;
        bodyMapper = ComponentMapper.getFor(BodyComponent.class);
        playerMapper = ComponentMapper.getFor(PlayerComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        playerComponent = playerMapper.get(entity);
    }

    private void weaponTakeOut(){
        playerComponent.weaponHidden = !playerComponent.weaponHidden;
        }



    private void weaponShoot(){
        if(playerComponent.activeWeapon != null && !playerComponent.weaponHidden){
            System.out.println("shoot for " + playerComponent.activeWeapon.getDamage());
        }
    }

    @Override
    public void receive(Signal<GameEvent> signal, GameEvent event) {

        switch (event){
            case WEAPON_TAKE_OUT:
                weaponTakeOut();
                break;
            case WEAPON_SHOOT:
                weaponShoot();
                break;
        }
    }
}


