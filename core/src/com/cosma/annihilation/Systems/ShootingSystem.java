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
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.graphics.ParticleEmitterBox2D;
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
    private StateComponent stateComponent;
    private Thread thread;

    private Batch batch;

    private Body body;
    private WeaponMagazine weaponMagazine;
    private RayCastCallback callback;
    private boolean isWeaponShooting;
    private Entity targetEntity;
    private PointLight weaponLight;
    private int direction = 1;
    private boolean isMeleeAttackFinish = true;
    private Signal<GameEvent> signal;
    private Vector2 raycastEnd;
    ParticleEffect pe;

    public ShootingSystem(World world, RayHandler rayHandler, Batch batch) {
        super(Family.all(PlayerComponent.class).get(),Constants.SHOOTING_SYSTEM);
        this.world = world;
        this.batch = batch;

//        pe = new ParticleEffect();
//
//
//        pe.load(Gdx.files.internal("particle/gun.p"),Gdx.files.internal(""));
//        pe.getEmitters().first().setPosition(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
//        pe.getEmitters().add(new ParticleEmitterBox2D(world,pe.getEmitters().first()));
//        pe.getEmitters().removeIndex(0);




        weaponMagazine = new WeaponMagazine();
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

        signal = new Signal<GameEvent>();

        //TODO
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        playerComponent.canMoveOnSide = true;
                        isMeleeAttackFinish = true;
                        world.rayCast(callback, body.getPosition(), new Vector2(body.getPosition().x + direction, body.getPosition().y));
                        if(calculateAttackAccuracy() && targetEntity != null){
                            targetEntity.getComponent(HealthComponent.class).hp -= playerComponent.activeWeapon.getDamage();
                        }

                    }
                }, animationComponent.currentAnimation.getAnimationDuration());
                thread.interrupt();
            }
        });



        callback = new RayCastCallback() {
            @Override
            public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                if(fixture.getBody().getUserData() instanceof Entity && ((Entity) fixture.getBody().getUserData()).getComponent(HealthComponent.class ) != null){
                    targetEntity =(Entity)fixture.getBody().getUserData();
                    System.out.println(targetEntity.getComponent(HealthComponent.class).hp);
                }else targetEntity = null;
                return 0;
            }
        };
    }

    @Override
    public void update(float deltaTime) {

        super.update(deltaTime);

        if(isMeleeAttackFinish){
            animationComponent.isAnimationPlayed = false;
        }else{
            animationComponent.isAnimationPlayed = true;
        }

    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        playerComponent = playerMapper.get(entity);
        playerInventoryComponent = playerDateMapper.get(entity);
        statsComponent = playerStatsMapper.get(entity);
        animationComponent = playerAnimMapper.get(entity);

        body = bodyMapper.get(entity).body;

        if (!animationComponent.spriteDirection) {
            direction = -1;
        }else direction = 1;



        if (Gdx.input.isKeyPressed(Input.Keys.R)) {
            meleeAttack();
        }

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
            signal.dispatch(GameEvent.START_ATTACK_ANIMATION);
            isMeleeAttackFinish = false;
            animationComponent.isAnimationPlayed = true;

            world.rayCast(callback, body.getPosition(), new Vector2(body.getPosition().x + direction, body.getPosition().y));

            animationComponent.time = 0;

            animationComponent.animationState = AnimationStates.MELEE;
            float timer = animationComponent.currentAnimation.getAnimationDuration();
            playerComponent.canMoveOnSide = false;



            thread.start();


//            Timer.schedule(new Timer.Task() {
//                @Override
//                public void run() {
//                    playerComponent.canMoveOnSide = true;
//                    isMeleeAttackFinish = true;
//                    System.out.println("end attack");
//                    world.rayCast(callback, body.getPosition(), new Vector2(body.getPosition().x + direction, body.getPosition().y));
//                    if(calculateAttackAccuracy() && targetEntity != null){
//                        targetEntity.getComponent(HealthComponent.class).hp -= playerComponent.activeWeapon.getDamage();
//                    }
//
//                }
//            }, animationComponent.currentAnimation.getAnimationDuration());

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
            System.out.println(this.getEngine().getEntities().size());
            this.getEngine().addEntity(EntityFactory.getInstance().createBulletShellEntity(body.getPosition().x + 0.7f*direction, body.getPosition().y + 0.63f));
            Sound sound = Annihilation.getAssets().get("sfx/cg1.wav");
            sound.play();
            weaponMagazine.removeAmmoFromMagazine();
            System.out.println(this.getEngine().getEntities().size());
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
        switch (playerComponent.activeWeapon.getItemID()){
            case P38:
                weaponLight.attachToBody(body,direction-0.1f,0.5f);

                break;

        }



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


