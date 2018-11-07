package com.cosma.annihilation.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.cosma.annihilation.Annihilation;
import com.cosma.annihilation.Utils.LoaderOLD;


public class MenuScreen implements Screen {

    private Annihilation game;
    private Stage stage;
    private Camera camera;
    private Viewport viewport;


    public MenuScreen(final Annihilation game){
        Skin skin = (Skin) LoaderOLD.getResource("skin");
        this.game = game;
        camera = new OrthographicCamera();
        camera.update();
        viewport = new ScreenViewport(camera);
        stage = new Stage(viewport);
        viewport.apply(true);

        Table table = new Table();
        table.setFillParent(true);
        table.center();

        stage.addActor(table);

        TextButton continueGameButton = new TextButton("Continue", skin);
        checkSaveFileExist(continueGameButton);
        table.add(continueGameButton).size(getButtonWidth(2),getButtonHeight(2));
        table.row();

        TextButton newGameButton = new TextButton("New Game", skin);
        table.add(newGameButton).size(getButtonWidth(2),getButtonHeight(2));
        table.row();

        TextButton optionsButton = new TextButton("Options", skin);
        table.add(optionsButton).size(getButtonWidth(2),getButtonHeight(2));
        table.row();

        TextButton exitGameButton = new TextButton("Exit", skin);
        table.add(exitGameButton).size(getButtonWidth(2),getButtonHeight(2));

        exitGameButton.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();
                return true;
            }
        });

        newGameButton.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setGameScreen();
                return true;
            }
        });

        continueGameButton.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setGameState(true);
                game.setGameScreen();
                return true;
            }
        });

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
        camera.update();

    }

    @Override
    public void resize(int width, int height) {
            viewport.update(width,height,true);
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
        stage.dispose();
    }

    private float getButtonWidth(float scale){
        float screenWidth = Gdx.app.getGraphics().getWidth();
        System.out.println(screenWidth);
        float size = (screenWidth/12.8f)*scale;
        return size;
    }

    private float getButtonHeight(float scale){
        float screenWidth = Gdx.graphics.getHeight();
        float size = (screenWidth/24)*scale;
        return size;
    }

    private void checkSaveFileExist(Button button){
        button.setVisible(false);
        if(Gdx.files.local("save.json").exists()){
            button.setVisible(true);
        }
    }
}
