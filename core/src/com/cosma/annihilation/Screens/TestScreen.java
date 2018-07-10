package com.cosma.annihilation.Screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.cosma.annihilation.Annihilation;
import com.cosma.annihilation.Gui.OnScreenGui;
import com.cosma.annihilation.World.WorldBuilder;

public class TestScreen implements Screen, InputProcessor {
    private WorldBuilder worldBuilder;
    private OnScreenGui onScreenGui;
    private InputMultiplexer im;
    //gui test
    private Viewport viewport;
    private Camera camera;
    private Touchpad tpad;
    Stage stage;


    public  TestScreen() {
        //stage gui test
        stage = new Stage();
        camera = new OrthographicCamera();
        camera.update();
        viewport = new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),camera);
        stage.setViewport(viewport);
        viewport.apply(true);
        Skin skin = new Skin(Gdx.files.internal("UI/uiskin.json"));
        Table table = new Table();
        table.top();
        table.setFillParent(true);
        //Debug Mode
        table.setDebug(true);

        table.row();
        table.row();

        Touchpad
        tpad = new Touchpad(0,skin,"default");
        table.add(tpad).expandX().padBottom(10).padLeft(10).width(50).height(50).fillY().expandY().bottom().left();
        table.row();
        stage.addActor(table);


        worldBuilder = new WorldBuilder();
        onScreenGui = new OnScreenGui();
        im = new InputMultiplexer();
        im.addProcessor(this);

        }

    @Override
    public void show() {
        viewport.apply(true);
        Gdx.input.setInputProcessor(im);
        System.out.println(Gdx.graphics.getHeight());
        System.out.println(Gdx.graphics.getWidth());
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
        camera.update();
//        worldBuilder.update(delta);
        }

    @Override
    public void resize(int width, int height) {
        worldBuilder.resize(width,height);
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
