package com.cosma.annihilation.Screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.utils.viewport.*;
import com.cosma.annihilation.Annihilation;
import com.cosma.annihilation.Gui.OnScreenGui;
import com.cosma.annihilation.Systems.ActionSystem;
import com.cosma.annihilation.Systems.PlayerControlSystem;
import com.cosma.annihilation.Utils.StateManager;
import com.cosma.annihilation.World.WorldBuilder;

public class TestScreen implements Screen, InputProcessor {
    private WorldBuilder worldBuilder;
    private OnScreenGui gui;
    private InputMultiplexer im;
    private Viewport guiViewport;
    private Viewport viewport;
    private Camera camera;
    Stage guiStage;
    public  TestScreen() {


        gui = new OnScreenGui();
        worldBuilder = new WorldBuilder();
        im = new InputMultiplexer();
        im.addProcessor(gui);
        im.addProcessor( worldBuilder.getEngine().getSystem(PlayerControlSystem.class));
        im.addProcessor( worldBuilder.getEngine().getSystem(ActionSystem.class));
        im.addProcessor(this);

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(im);
        System.out.println(Gdx.graphics.getHeight());
        System.out.println(Gdx.graphics.getWidth());
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        worldBuilder.update(delta);
        //Gui render
        gui.draw();
        if(StateManager.debugModeGui){
            gui.setDebugAll(true);
        }else gui.setDebugAll(false);
        gui.act(delta);
        gui.actlab();


        }

    @Override
    public void resize(int width, int height) {
        worldBuilder.resize(width,height);
        gui.getViewport().update(width, height,true);
    }


    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

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
//        Vector3 worldCoordinates = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
//        Vector3 vec = worldBuilder.getCamera().unproject(worldCoordinates);
//               System.out.println(vec);

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
