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
import com.cosma.annihilation.Components.PlayerDateComponent;
import com.cosma.annihilation.Entities.EntityFactory;
import com.cosma.annihilation.Gui.Inventory.InventoryItemLocation;
import com.cosma.annihilation.Utils.AssetLoader;
import com.cosma.annihilation.Utils.Enums.GameEvent;
import com.cosma.annihilation.Utils.SfxAssetDescriptors;

public class ShootingSystem extends IteratingSystem implements Listener<GameEvent> {
    private ComponentMapper<BodyComponent> bodyMapper;
    private ComponentMapper<PlayerComponent> playerMapper;
    private ComponentMapper<PlayerDateComponent> playerDateMapper;
    private AssetLoader assetLoader;
    private World world;
    private PlayerComponent playerComponent;
    private PlayerDateComponent playerDateComponent;
    private Body body;

    public ShootingSystem(World world, AssetLoader assetLoader) {
        super(Family.all(PlayerComponent.class).get(),11);
        this.world = world;
        this.assetLoader = assetLoader;

        bodyMapper = ComponentMapper.getFor(BodyComponent.class);
        playerMapper = ComponentMapper.getFor(PlayerComponent.class);
        playerDateMapper = ComponentMapper.getFor(PlayerDateComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        playerComponent = playerMapper.get(entity);
        playerDateComponent = playerDateMapper.get(entity);
        body = bodyMapper.get(entity).body;
    }

    private void weaponTakeOut() {
        if (playerComponent.activeWeapon != null) {
            playerComponent.weaponHidden = !playerComponent.weaponHidden;
        }
    }

    private void weaponShoot(){
        if(playerComponent.activeWeapon != null && !playerComponent.weaponHidden){
            if(playerComponent.weaponReady & isAmmoAvailable()){
                System.out.println("shoot for " + playerComponent.activeWeapon.getDamage());
                EntityFactory.getInstance().createBulletEntity(body.getPosition().x+1.1f,body.getPosition().y+0.63f,20);
                EntityFactory.getInstance().createBulletShellEntity(body.getPosition().x+0.7f,body.getPosition().y+0.63f);
                Sound sound = assetLoader.manager.get(SfxAssetDescriptors.pistolSound);
                sound.play();
                ammoRemove();
            }else{
                playerComponent.weaponReady = true;
            }
        }
    }

    private boolean isAmmoAvailable(){
        for(InventoryItemLocation item: playerDateComponent.inventoryItem ){
            if(item.getItemID().equals(playerComponent.activeWeapon.getAmmoID().toString())){
                return true;
            }
        }
        System.out.println("no ammo");
        return false;
    }
    private void ammoRemove(){
        for(InventoryItemLocation item: playerDateComponent.inventoryItem ){
            if(item.getItemID().equals(playerComponent.activeWeapon.getAmmoID().toString())){
                if(item.getItemsAmount() > 1){
                    item.setItemsAmount(item.getItemsAmount() -1);
                }else
                    playerDateComponent.inventoryItem.removeValue(item,false);
            }
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


