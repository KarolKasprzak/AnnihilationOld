package com.cosma.annihilation.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.physics.box2d.*;
import com.cosma.annihilation.Components.BodyComponent;
import com.cosma.annihilation.Components.PlayerComponent;
import com.cosma.annihilation.Entities.EntityFactory;
import com.cosma.annihilation.Utils.AssetLoader;
import com.cosma.annihilation.Utils.Enums.GameEvent;
import com.cosma.annihilation.Utils.SfxAssetDescriptors;

public class ShootingSystem extends IteratingSystem implements Listener<GameEvent> {
    private ComponentMapper<BodyComponent> bodyMapper;
    private ComponentMapper<PlayerComponent> playerMapper;
    private AssetLoader assetLoader;
    private World world;
    private PlayerComponent playerComponent;
    private Body body;

    public ShootingSystem(World world, AssetLoader assetLoader) {
        super(Family.all(PlayerComponent.class).get(),11);
        this.world = world;
        this.assetLoader = assetLoader;

        bodyMapper = ComponentMapper.getFor(BodyComponent.class);
        playerMapper = ComponentMapper.getFor(PlayerComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        playerComponent = playerMapper.get(entity);
        body = bodyMapper.get(entity).body;
    }

    private void weaponTakeOut() {
        if (playerComponent.activeWeapon != null) {
            playerComponent.weaponHidden = !playerComponent.weaponHidden;
        }
    }

    private void weaponShoot(){
        if(playerComponent.activeWeapon != null && !playerComponent.weaponHidden){
            System.out.println("shoot for " + playerComponent.activeWeapon.getDamage());
            EntityFactory.getInstance().createBulletEntity(body.getPosition().x+1.2f,body.getPosition().y,25);
            Sound sound = assetLoader.manager.get(SfxAssetDescriptors.pistolSound);
            sound.play();
            System.out.println(getEngine().getEntities().size());
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


