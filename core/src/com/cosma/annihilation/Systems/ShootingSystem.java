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
import com.cosma.annihilation.Utils.StateManager;

import java.util.Timer;
import java.util.TimerTask;

public class ShootingSystem extends IteratingSystem implements Listener<GameEvent> {
    private ComponentMapper<BodyComponent> bodyMapper;
    private ComponentMapper<PlayerComponent> playerMapper;
    private ComponentMapper<PlayerDateComponent> playerDateMapper;
    private AssetLoader assetLoader;
    private World world;
    private PlayerComponent playerComponent;
    private PlayerDateComponent playerDateComponent;
    private Body body;
    private WeaponMagazine weaponMagazine;
    private boolean isWeaponShooting;


    public ShootingSystem(World world, AssetLoader assetLoader) {
        super(Family.all(PlayerComponent.class).get(),11);
        this.world = world;
        this.assetLoader = assetLoader;

        weaponMagazine = new WeaponMagazine();
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
            weaponMagazine.setAmmoInMagazine(playerComponent.activeWeapon.getAmmoInMagazine());
            weaponMagazine.setMaxAmmoInMagazine(playerComponent.activeWeapon.getMaxAmmoInMagazine());
            playerComponent.isWeaponHidden = !playerComponent.isWeaponHidden;
        }
    }

    private void weaponShoot() {
        if (playerComponent.activeWeapon != null && !playerComponent.isWeaponHidden) {
                if (weaponMagazine.hasAmmo()) {
                    if (StateManager.playerDirection) {
                        EntityFactory.getInstance().createBulletEntity(body.getPosition().x + 1.1f, body.getPosition().y + 0.63f, 20, false,playerComponent.activeWeapon.getDamage(),playerComponent.activeWeapon.getAccuracy());
                        EntityFactory.getInstance().createBulletShellEntity(body.getPosition().x + 0.7f, body.getPosition().y + 0.63f);
                    } else {
                        EntityFactory.getInstance().createBulletEntity(body.getPosition().x - 1.1f, body.getPosition().y + 0.63f, -20, true,playerComponent.activeWeapon.getDamage(),playerComponent.activeWeapon.getAccuracy());
                        EntityFactory.getInstance().createBulletShellEntity(body.getPosition().x - 0.7f, body.getPosition().y + 0.63f);
                    }
                    Sound sound = assetLoader.manager.get(SfxAssetDescriptors.pistolSound);
                    sound.play();
                    weaponMagazine.removeAmmoFromMagazine();
                } else {
                    weaponMagazine.reload();
                }
            }
        }


    private int addAmmoFromInventory() {
        int ammoInInventory = 0;
        for (InventoryItemLocation item : playerDateComponent.inventoryItem) {
            if (item.getItemID().equals(playerComponent.activeWeapon.getAmmoID().toString())) {
                ammoInInventory = item.getItemsAmount();
                if (ammoInInventory < playerComponent.activeWeapon.getMaxAmmoInMagazine()) {
                    playerDateComponent.inventoryItem.removeValue(item, false);
                    return ammoInInventory;
                } else {
                    item.setItemsAmount(item.getItemsAmount() - playerComponent.activeWeapon.getMaxAmmoInMagazine());
                    return playerComponent.activeWeapon.getMaxAmmoInMagazine();
                }
            }
        }
        return ammoInInventory;
    }

    @Override
    public void receive(Signal<GameEvent> signal, GameEvent event) {

        switch (event) {
            case ACTION_BUTTON_TOUCH_DOWN:
                startShooting();
                break;
            case ACTION_BUTTON_TOUCH_UP:
                 isWeaponShooting= false;
                break;
            case WEAPON_TAKE_OUT:
                weaponTakeOut();
                break;
            case WEAPON_SHOOT:
//                startShooting();
                break;
            case WEAPON_STOP_SHOOT:
//                isWeaponShooting = false;
                break;
        }
    }

    private void startShooting(){
        if(playerComponent.activeWeapon.isAutomatic()){
            isWeaponShooting = true;
            automaticWeaponShoot();
        }else
            weaponShoot();
    }

    private void automaticWeaponShoot(){
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if(isWeaponShooting){
                    weaponShoot();
                }else{
                    this.cancel();
                    System.out.println("canel");
                }

            }
        };
        new Timer().scheduleAtFixedRate(timerTask,0,Math.round(playerComponent.activeWeapon.getReloadTime()*1000));
    }



    private class WeaponMagazine{
        private int ammoInMagazine;
        private int maxAmmoInMagazine;

        private WeaponMagazine(){

        }

        void reload(){
            setAmmoInMagazine(addAmmoFromInventory());
        }

        boolean hasAmmo(){
            if(ammoInMagazine > 0){
                return true;
            }
            return false;
        }

        int getAmmoInMagazine() {

            return ammoInMagazine;
        }

        void setAmmoInMagazine(int ammoInMagazine) {
            this.ammoInMagazine = ammoInMagazine;
        }

        void removeAmmoFromMagazine(){
            ammoInMagazine --;
        }

        void setMaxAmmoInMagazine(int maxAmmoInMagazine) {
            this.maxAmmoInMagazine = maxAmmoInMagazine;
        }
    }




}


