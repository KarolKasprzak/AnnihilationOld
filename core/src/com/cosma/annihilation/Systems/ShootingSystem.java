package com.cosma.annihilation.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.cosma.annihilation.Components.*;
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
    private ComponentMapper<PlayerStateComponent> stateMapper;
    private Body body;
    private WeaponMagazine weaponMagazine;
    private RayCastCallback callback;
    private boolean isWeaponShooting;
    private PlayerStateComponent stateComponent;


    public ShootingSystem(World world, AssetLoader assetLoader) {
        super(Family.all(PlayerComponent.class).get(),11);
        this.world = world;
        this.assetLoader = assetLoader;

        weaponMagazine = new WeaponMagazine();
        stateMapper = ComponentMapper.getFor(PlayerStateComponent.class);
        bodyMapper = ComponentMapper.getFor(BodyComponent.class);
        playerMapper = ComponentMapper.getFor(PlayerComponent.class);
        playerDateMapper = ComponentMapper.getFor(PlayerDateComponent.class);

        callback = new RayCastCallback() {
            @Override
            public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                System.out.println("dsa");
                Entity entity =(Entity)fixture.getBody().getUserData();
                System.out.println(entity.getComponent(SerializationComponent.class).type);

                return 0;
            }
        };


    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        playerComponent = playerMapper.get(entity);
        playerDateComponent = playerDateMapper.get(entity);
        stateComponent = stateMapper.get(entity);
        body = bodyMapper.get(entity).body;
    }

    private void weaponTakeOut() {
        if (playerComponent.activeWeapon != null) {
            weaponMagazine.setAmmoInMagazine(playerComponent.activeWeapon.getAmmoInMagazine());
            weaponMagazine.setMaxAmmoInMagazine(playerComponent.activeWeapon.getMaxAmmoInMagazine());
            playerComponent.isWeaponHidden = !playerComponent.isWeaponHidden;
            System.out.println(body.getPosition());
        }
    }

    private void weaponShoot() {
        if (playerComponent.activeWeapon != null && !playerComponent.isWeaponHidden) {
                if (weaponMagazine.hasAmmo()) {
                    if (stateComponent.playerDirection) {
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
                weaponSelector();
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

    private void weaponSelector() {
        if (playerComponent.activeWeapon != null) {
            int weaponType = playerComponent.activeWeapon.getItemUseType();

            switch (weaponType) {
                case 4:
                    meleeAttack();
                    break;
                case 8:
                case 16:
                case 32:
                case 64:
                    startShooting();
                    break;
            }
        }
    }


    private void meleeAttack(){
        if (!playerComponent.isWeaponHidden) {
            if(stateComponent.playerDirection){
                world.rayCast(callback,body.getPosition(),new Vector2(body.getPosition().x+1,body.getPosition().y));
            }else
                world.rayCast(callback,body.getPosition(),new Vector2(body.getPosition().x-1,body.getPosition().y));


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
        com.badlogic.gdx.utils.Timer.schedule(new com.badlogic.gdx.utils.Timer.Task() {
            @Override
            public void run() {
                if(isWeaponShooting){
                    weaponShoot();
                }else{
                    this.cancel();
                    System.out.println("canel");
                }
            }
        }, 0,playerComponent.activeWeapon.getReloadTime());
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


