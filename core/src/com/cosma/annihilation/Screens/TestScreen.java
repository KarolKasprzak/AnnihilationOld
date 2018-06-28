package com.cosma.annihilation.Screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.cosma.annihilation.Annihilation;
import com.cosma.annihilation.Gui.OnScreenGui;
import com.cosma.annihilation.World.WorldBuilder;

public class TestScreen implements Screen, InputProcessor {
    private WorldBuilder worldBuilder;
    private OnScreenGui onScreenGui;
    private Stage guiStage;
    private InputMultiplexer im;



    public  TestScreen() {

//        Stage stage = new Stage();
        worldBuilder = new WorldBuilder();
        onScreenGui = new OnScreenGui();
        im = new InputMultiplexer();
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
//        onScreenGui.render(delta);



    }

    @Override
    public void resize(int width, int height) {

        System.out.println(Gdx.graphics.getHeight());
        System.out.println(Gdx.graphics.getWidth());

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
        System.out.println(screenX + " " + screenY);
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
