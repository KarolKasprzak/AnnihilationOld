package com.cosma.annihilation.Systems;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.physics.box2d.*;
import com.cosma.annihilation.Annihilation;
import com.cosma.annihilation.Components.*;
import com.cosma.annihilation.Entities.EntityFactory;
import com.cosma.annihilation.Gui.Inventory.InventoryItemLocation;
import com.cosma.annihilation.Utils.Constants;
import com.cosma.annihilation.Utils.Enums.AnimationStates;
import com.cosma.annihilation.Utils.Enums.CollisionID;
import com.cosma.annihilation.Utils.Enums.GameEvent;
import com.cosma.annihilation.Utils.GfxAssetDescriptors;
import com.cosma.annihilation.Utils.SfxAssetDescriptors;
import java.util.concurrent.ThreadLocalRandom;

public class ShootingSystem extends IteratingSystem implements Listener<GameEvent> {
    private ComponentMapper<BodyComponent> bodyMapper;
    private ComponentMapper<PlayerComponent> playerMapper;
    private ComponentMapper<PlayerInventoryComponent> playerDateMapper;
    private ComponentMapper<PlayerStatsComponent> playerStatsMapper;
    private ComponentMapper<AnimationComponent> playerAnimMapper;

    private World world;
    private AnimationComponent animationComponent;
    private PlayerComponent playerComponent;
    private PlayerInventoryComponent playerInventoryComponent;
    private PlayerStatsComponent statsComponent;
    private ComponentMapper<PlayerComponent> stateMapper;
    private Body body;
    private WeaponMagazine weaponMagazine;
    private RayCastCallback callback;
    private boolean isWeaponShooting;
    private Entity targetEntity;
    private RayHandler rayHandler;
    private PointLight weaponLight;
    private int direction = 1;
    private boolean isMeleeAttackFinish = true;
    Animation<TextureRegion> meleeAnimation;
    private Vector2 raycastEnd;
    public ShootingSystem(World world, RayHandler rayHandler) {
        super(Family.all(PlayerComponent.class).get(),Constants.SHOOTING_SYSTEM);
        this.world = world;
        this.rayHandler = rayHandler;

        weaponMagazine = new WeaponMagazine();
        stateMapper = ComponentMapper.getFor(PlayerComponent.class);
        bodyMapper = ComponentMapper.getFor(BodyComponent.class);
        playerMapper = ComponentMapper.getFor(PlayerComponent.class);
        playerDateMapper = ComponentMapper.getFor(PlayerInventoryComponent.class);
        playerStatsMapper = ComponentMapper.getFor(PlayerStatsComponent.class);
        playerAnimMapper = ComponentMapper.getFor(AnimationComponent.class);

        raycastEnd = new Vector2();

        weaponLight = new PointLight(rayHandler, 90, new Color(1,0.8f,0,0.8f), 0.8f,0,0);
        weaponLight.setStaticLight(false);
        Filter filter = new Filter();
        filter.maskBits = CollisionID.CAST_SHADOW;
        weaponLight.setContactFilter(filter);
        weaponLight.setSoftnessLength(0.3f);
        weaponLight.setSoft(true);
        weaponLight.setActive(false);


        meleeAnimation = new Animation(0.1f,Annihilation.getAssets().get(GfxAssetDescriptors.player_attack_melee).getRegions(), Animation.PlayMode.NORMAL);



        callback = new RayCastCallback() {
            @Override
            public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                if(fixture.getBody().getUserData() instanceof Entity && ((Entity) fixture.getBody().getUserData()).getComponent(HealthComponent.class ) != null){
                    targetEntity =(Entity)fixture.getBody().getUserData();
                }else targetEntity = null;
                return 0;
            }
        };
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        playerComponent = playerMapper.get(entity);
        playerInventoryComponent = playerDateMapper.get(entity);
        playerComponent = stateMapper.get(entity);
        statsComponent = playerStatsMapper.get(entity);
        animationComponent = playerAnimMapper.get(entity);

        body = bodyMapper.get(entity).body;

        if (!playerComponent.playerDirection) {
            direction = -1;
        }else direction = 1;

        weaponLight.attachToBody(body,direction,0.3f);


    }

    @Override
    public void receive(Signal<GameEvent> signal, GameEvent event) {
        switch (event) {
            case ACTION_BUTTON_TOUCH_DOWN:
                weaponSelect();
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

    private void weaponSelect() {
        if (playerComponent.activeWeapon != null && !playerComponent.isWeaponHidden) {
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

    private void meleeAttack() {
        if (!playerComponent.isWeaponHidden & isMeleeAttackFinish) {
            System.out.println("attacksfddfsdfssfdsdffds");
            isMeleeAttackFinish = false;
            playerComponent.isAnimationPlayed = true;
            world.rayCast(callback, body.getPosition(), new Vector2(body.getPosition().x + direction, body.getPosition().y));

            animationComponent.time = 0;
//            animationComponent.currentAnimation = meleeAnimation;
            playerComponent.animationState = AnimationStates.MELEE;
            playerComponent.canMoveOnSide = false;
          
            float timer = animationComponent.currentAnimation.getAnimationDuration();
            System.out.println(timer);

            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    playerComponent.canMoveOnSide = true;
                    isMeleeAttackFinish = true;
                    playerComponent.isAnimationPlayed = false;
                    System.out.println("end attack");
                }
            }, timer);

        }
    }

    private void startShooting(){
        if(playerComponent.activeWeapon.isAutomatic()){
            isWeaponShooting = true;
            automaticWeaponShoot();
        }else
            weaponShoot();
    }

    private void weaponShoot() {

        if (weaponMagazine.hasAmmo()) {
            world.rayCast(callback,body.getPosition(),raycastEnd.set(body.getPosition().x+15*direction,body.getPosition().y));
            if(calculateAttackAccuracy() && targetEntity != null){
                targetEntity.getComponent(HealthComponent.class).hp -= playerComponent.activeWeapon.getDamage();
            }
            shootLight();
            EntityFactory.getInstance().createBulletShellEntity(body.getPosition().x + 0.7f*direction, body.getPosition().y + 0.63f);
            Sound sound = Annihilation.getAssets().get(SfxAssetDescriptors.pistolSound);
            sound.play();
            weaponMagazine.removeAmmoFromMagazine();
        } else {
            weaponMagazine.reload();
        }
    }


//    private void weaponShoot() {
//        if (playerComponent.activeWeapon != null && !playerComponent.isWeaponHidden) {
//            if (weaponMagazine.hasAmmo()) {
//                if (playerComponent.playerDirection) {
//                    EntityFactory.getInstance().createBulletEntity(body.getPosition().x + 1.1f, body.getPosition().y + 0.63f, 20, false,playerComponent.activeWeapon.getDamage(),calculateAttackAccuracy());
//                    EntityFactory.getInstance().createBulletShellEntity(body.getPosition().x + 0.7f, body.getPosition().y + 0.63f);
//                } else {
//                    EntityFactory.getInstance().createBulletEntity(body.getPosition().x - 1.1f, body.getPosition().y + 0.63f, -20, true,playerComponent.activeWeapon.getDamage(),calculateAttackAccuracy());
//                    EntityFactory.getInstance().createBulletShellEntity(body.getPosition().x - 0.7f, body.getPosition().y + 0.63f);
//                }
//                Sound sound = assetLoader.manager.get(SfxAssetDescriptors.pistolSound);
//                sound.play();
//                weaponMagazine.removeAmmoFromMagazine();
//            } else {
//                weaponMagazine.reload();
//            }
//        }
//    }

    private void automaticWeaponShoot(){
        com.badlogic.gdx.utils.Timer.schedule(new com.badlogic.gdx.utils.Timer.Task() {
            @Override
            public void run() {
                if(isWeaponShooting){
                    weaponShoot();
                }else{
                    this.cancel();
                }
            }
        }, 0,playerComponent.activeWeapon.getReloadTime());
    }



    /**true = hit, false = miss */
    private boolean calculateAttackAccuracy(){
        float weaponAccuracy =  playerComponent.activeWeapon.getAccuracy();
        float playerSkill = 0;
        int weaponType = playerComponent.activeWeapon.getItemUseType();

        switch (weaponType) {
            case 4:
                playerSkill = statsComponent.meleeWeapons;
                break;
            case 8:
                playerSkill = statsComponent.energeticWeapons;
                break;
            case 16:
                playerSkill = statsComponent.energeticWeapons;
                break;
            case 32:
                playerSkill = statsComponent.smallWeapons;
                break;
            case 64:
                playerSkill = statsComponent.smallWeapons;
                break;
        }
        float playerAccuracy = ((float)playerSkill*0.005f + weaponAccuracy);
        if(playerAccuracy >=0.95f){
            return true;
        }else{
            double randomBonus = ThreadLocalRandom.current().nextDouble(playerAccuracy,1);
//            float randomBonus =  randomGenerator.nextFloat() * (0.99f - playerAccuracy) + playerAccuracy;
              if (randomBonus >= 0.95f){
                  System.out.println("Player accuracy + bonus: " + randomBonus);
                  return true;
              }
        }
        System.out.println("miss " );
        return false;
    }

    private int calcualteAttackDamage(){
        //TODO
        return 0;
    }

    private void weaponTakeOut() {
        if (playerComponent.activeWeapon != null) {
            weaponMagazine.setAmmoInMagazine(playerComponent.activeWeapon.getAmmoInMagazine());
            weaponMagazine.setMaxAmmoInMagazine(playerComponent.activeWeapon.getMaxAmmoInMagazine());
            playerComponent.isWeaponHidden = !playerComponent.isWeaponHidden;
            System.out.println(body.getPosition());
        }
    }

    private int addAmmoFromInventory() {
        int ammoInInventory = 0;
        for (InventoryItemLocation item : playerInventoryComponent.inventoryItem) {
            if (item.getItemID().equals(playerComponent.activeWeapon.getAmmoID().toString())) {
                ammoInInventory = item.getItemsAmount();
                if (ammoInInventory < playerComponent.activeWeapon.getMaxAmmoInMagazine()) {
                    playerInventoryComponent.inventoryItem.removeValue(item, false);
                    return ammoInInventory;
                } else {
                    item.setItemsAmount(item.getItemsAmount() - playerComponent.activeWeapon.getMaxAmmoInMagazine());
                    return playerComponent.activeWeapon.getMaxAmmoInMagazine();
                }
            }
        }
        return ammoInInventory;
    }


    private void shootLight(){
        weaponLight.setActive(true);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                weaponLight.setActive(false);
            }
        }, 0.1f);
    }

    void playAnimation(Animation animation, float animationTime) {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                weaponLight.setActive(false);
            }
        }, animationTime);
    }


    private class WeaponMagazine {

        private int ammoInMagazine;
        private int maxAmmoInMagazine;

        private WeaponMagazine() {

        }

        void reload() {
            setAmmoInMagazine(addAmmoFromInventory());
        }

        boolean hasAmmo() {
            if (ammoInMagazine > 0) {
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

        void removeAmmoFromMagazine() {
            ammoInMagazine--;
        }

        void setMaxAmmoInMagazine(int maxAmmoInMagazine) {
            this.maxAmmoInMagazine = maxAmmoInMagazine;
        }

    }
}


