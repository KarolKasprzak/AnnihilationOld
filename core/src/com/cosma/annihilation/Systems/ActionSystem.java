package com.cosma.annihilation.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.cosma.annihilation.Components.BodyComponent;
import com.cosma.annihilation.Components.ContainerComponent;
import com.cosma.annihilation.Utils.AssetsLoader;
import net.dermetfan.gdx.graphics.g2d.Box2DSprite;

public class ActionSystem extends IteratingSystem implements InputProcessor {
    private ComponentMapper<BodyComponent> bodyMapper;
    private ComponentMapper<ContainerComponent> conatinerMapper;
    Box2DSprite box2DSprite;
    public ActionSystem(Engine engine, World world,OrthographicCamera camera) {
        super(Family.all(ContainerComponent.class).get(),11);

        bodyMapper = ComponentMapper.getFor(BodyComponent.class);
        conatinerMapper = ComponentMapper.getFor(ContainerComponent.class);

    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
       ContainerComponent containerComponent = conatinerMapper.get(entity);
             if(containerComponent.showIcon){
                 Body body = entity.getComponent(BodyComponent.class).body;
                 System.out.println("open");
             }



    }


    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {

        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
