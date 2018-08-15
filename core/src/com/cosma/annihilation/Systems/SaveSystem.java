package com.cosma.annihilation.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Json;
import com.cosma.annihilation.Components.BodyComponent;
import com.cosma.annihilation.Components.PlayerComponent;
import com.cosma.annihilation.Components.TransformComponent;

public class SaveSystem extends IteratingSystem {
    private ComponentMapper<TransformComponent> transformMapper;
    private Json json;
    private World world;
    private  FileHandle file;
    Entity player;
    public SaveSystem(World world) {
        super(Family.all(PlayerComponent.class).get());
        transformMapper = ComponentMapper.getFor(TransformComponent.class);
        json = new Json();
        file = Gdx.files.local("save/save.json");
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        player = this.getEngine().getEntitiesFor(Family.all(PlayerComponent.class).get()).first();

        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            saveGame();

        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.L)) {
            loadGame();
        }


    }

    public void saveGame(){
        file.writeString(json.prettyPrint(player.getComponent(TransformComponent.class).position),false);
    }
    public void loadGame(){
        this.getEngine().removeAllEntities();

    }



}
