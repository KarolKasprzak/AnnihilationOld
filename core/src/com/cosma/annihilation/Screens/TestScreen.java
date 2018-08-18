package com.cosma.annihilation.Screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector3;
import com.cosma.annihilation.Gui.PlayerGUI;
import com.cosma.annihilation.Systems.PlayerControlSystem;
import com.cosma.annihilation.World.WorldBuilder;

public class TestScreen implements Screen, InputProcessor {

    private WorldBuilder worldBuilder;
    private InputMultiplexer im;
    private PlayerGUI playerGUI;
    public  TestScreen() {

        worldBuilder = new WorldBuilder();
        im = new InputMultiplexer();
        playerGUI = new PlayerGUI();
        im.addProcessor(playerGUI.getStage());
        im.addProcessor( worldBuilder.getEngine().getSystem(PlayerControlSystem.class));
        im.addProcessor(this);

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(im);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        worldBuilder.update(delta);
        playerGUI.render(delta);
        }

    @Override
    public void resize(int width, int height) {
        worldBuilder.resize(width,height);
        playerGUI.resize(width,height);
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
        Vector3 worldCoordinates = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        Vector3 vec = worldBuilder.getCamera().unproject(worldCoordinates);
//               System.out.println(screenX + " " + screenY);

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
