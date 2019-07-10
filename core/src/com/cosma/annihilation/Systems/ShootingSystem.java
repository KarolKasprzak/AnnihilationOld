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
import com.badlogic.gdx.graphics.Camera;
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
import com.cosma.annihilation.Items.WeaponItem;
import com.cosma.annihilation.Utils.Constants;
import com.cosma.annihilation.Utils.Animation.AnimationStates;
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
    private BodyComponent bodyComponent;
    private PlayerInventoryComponent playerInventoryComponent;
    private PlayerStatsComponent statsComponent;
    private StateComponent stateComponent;

    private Batch batch;

    private Body body;
    private WeaponMagazine weaponMagazine;
    private RayCastCallback callback;
    private boolean isWeaponShooting;
    private Entity targetEntity;
    private PointLight weaponLight;
    private int direction = 1;
    private float weaponReloadTimer = 0;
    private boolean isMeleeAttackFinish = true;
    private Signal<GameEvent> signal;
    private Vector2 raycastEnd;
    ParticleEffect pe;
    Camera camera;
    public ShootingSystem(World world, RayHandler rayHandler, Batch batch, Camera camera) {
        super(Family.all(PlayerComponent.class).get(),Constants.SHOOTING_SYSTEM);
        this.world = world;
        this.batch = batch;
        this.camera = camera;

        pe = new ParticleEffect();


        pe.load(Gdx.files.internal("particle/gun.p"),Gdx.files.internal("particle/"));
        pe.getEmitters().first().setPosition(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
        pe.getEmitters().add(new ParticleEmitterBox2D(world,pe.getEmitters().first()));
        pe.getEmitters().removeIndex(0);
        pe.scaleEffect(0.1f);


        weaponMagazine = new WeaponMagazine();
        bodyMapper = ComponentMapper.getFor(BodyComponent.class);
        playerMapper = ComponentMapper.getFor(PlayerComponent.class);
        playerDateMapper = ComponentMapper.getFor(PlayerInventoryComponent.class);
        playerStatsMapper = ComponentMapper.getFor(PlayerStatsComponent.class);
        playerAnimMapper = ComponentMapper.getFor(AnimationComponent.class);

        raycastEnd = new Vector2();

        weaponLight = new PointLight(rayHandler, 45, new Color(1,0.8f,0,1), 0.8f,0,0);
        weaponLight.setStaticLight(false);
        Filter filter = new Filter();
        filter.maskBits = CollisionID.CAST_SHADOW;
        weaponLight.setContactFilter(filter);
        weaponLight.setSoftnessLength(0.3f);
        weaponLight.setSoft(true);
        weaponLight.setActive(false);

        signal = new Signal<GameEvent>();

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
    public void update(float deltaTime) {

        super.update(deltaTime);

        pe.setPosition(body.getPosition().x,body.getPosition().y);

        batch.begin();
        pe.update(deltaTime);
        pe.draw(batch);
        batch.end();

    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        playerComponent = playerMapper.get(entity);
        playerInventoryComponent = playerDateMapper.get(entity);
        statsComponent = playerStatsMapper.get(entity);
        animationComponent = playerAnimMapper.get(entity);
        bodyComponent = bodyMapper.get(entity);
        body = bodyMapper.get(entity).body;

        weaponReloadTimer += deltaTime;


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
            isMeleeAttackFinish = false;
            animationComponent.isAnimationFinish = false;
            animationComponent.time = 0;
            world.rayCast(callback, body.getPosition(), new Vector2(body.getPosition().x + direction, body.getPosition().y));
            animationComponent.animationState = AnimationStates.MELEE;
            float animationTimer = animationComponent.animationMap.get(AnimationStates.MELEE.toString()).getAnimationDuration();
            playerComponent.canMoveOnSide = false;

            Timer.schedule(new Timer.Task() {
                               @Override
                               public void run() {
                                   world.rayCast(callback, body.getPosition(), new Vector2(body.getPosition().x + direction, body.getPosition().y));
                                   if (calculateAttackAccuracy() && targetEntity != null) {
                                       targetEntity.getComponent(HealthComponent.class).hp -= playerComponent.activeWeapon.getDamage();
                                   }
                                   animationComponent.isAnimationFinish = true;
                                   playerComponent.canMoveOnSide = true;
                                   isMeleeAttackFinish = true;
                               }
                           }, animationTimer);
        }
    }

    private void startShooting(){
        if(playerComponent.activeWeapon.isAutomatic()){
            isWeaponShooting = true;
            automaticWeaponShoot();
        }else
            semiAutomaticShoot();
    }

    private void semiAutomaticShoot(){
     if(weaponReloadTimer > playerComponent.activeWeapon.getReloadTime()){
        weaponShoot();
        weaponReloadTimer = 0;
     }
    }

    private void weaponShoot() {

        if (weaponMagazine.hasAmmo()) {
            world.rayCast(callback,body.getPosition(),raycastEnd.set(body.getPosition().x+15*direction,body.getPosition().y));
            if(calculateAttackAccuracy() && targetEntity != null){
                System.out.println("hit");
                targetEntity.getComponent(HealthComponent.class).hp -= playerComponent.activeWeapon.getDamage();
                targetEntity.getComponent(HealthComponent.class).isHit = true;
                targetEntity.getComponent(HealthComponent.class).attackerPosition =  bodyComponent.body.getPosition();
            }
            targetEntity = null;
            shootingLight();
            createShellAndBullet();
            Sound sound = Annihilation.getAssets().get("sfx/cg1.wav");
            sound.play();
            weaponMagazine.removeAmmoFromMagazine();
        } else {
            weaponMagazine.reload();
        }
    }

    private void createShellAndBullet(){
        WeaponItem.ItemID weaponID = playerComponent.activeWeapon.getItemID();
        switch (weaponID) {
            case P38:
                this.getEngine().addEntity(EntityFactory.getInstance().createBulletShellEntity(body.getPosition().x + 0.7f*direction, body.getPosition().y + 0.63f));
                this.getEngine().addEntity(EntityFactory.getInstance().createBulletEntity(body.getPosition().x + 1.1f*direction, body.getPosition().y + 0.63f,20, animationComponent.spriteDirection));
                this.getEngine().addEntity(EntityFactory.getInstance().createShootSplashEntity(body.getPosition().x + 1.1f*direction,body.getPosition().y + 0.59f,animationComponent.spriteDirection));
                break;
            case MP44:
                this.getEngine().addEntity(EntityFactory.getInstance().createBulletShellEntity(body.getPosition().x + 0.4f*direction, body.getPosition().y + 0.3f));
                this.getEngine().addEntity(EntityFactory.getInstance().createBulletEntity(body.getPosition().x + 1.1f*direction, body.getPosition().y + 0.3f,20, animationComponent.spriteDirection));
                this.getEngine().addEntity(EntityFactory.getInstance().createShootSplashEntity(body.getPosition().x + 1.1f*direction,body.getPosition().y + 0.29f,animationComponent.spriteDirection));
                break;
        }


    }

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


    private void shootingLight(){
        switch (playerComponent.activeWeapon.getItemID()){
            case P38:
                weaponLight.attachToBody(body,direction-0.1f,0.5f);
                break;
            case MP44:
                weaponLight.attachToBody(body,direction-0.1f,0.3f);
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


