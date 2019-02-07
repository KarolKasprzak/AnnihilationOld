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
import com.cosma.annihilation.Utils.AssetLoader;
import com.cosma.annihilation.Utils.GfxAssetDescriptors;
import com.cosma.annihilation.Utils.Utilities;


public class MenuScreen implements Screen {

    private Annihilation game;
    private Stage stage;
    private Camera camera;
    private Viewport viewport;
    private AssetLoader assetLoader;

    public MenuScreen(final Annihilation game, AssetLoader assetLoader){
        this.assetLoader = assetLoader;
        Skin skin = assetLoader.manager.get(GfxAssetDescriptors.skin);
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
        Utilities.setButtonColor(continueGameButton);
        checkSaveFileExist(continueGameButton);
        table.add(continueGameButton).size(Utilities.setButtonWidth(2.2f),Utilities.setButtonHeight(2.2f));
        table.row();

        TextButton newGameButton = new TextButton("New Game", skin);
        Utilities.setButtonColor(newGameButton);
        table.add(newGameButton).size(Utilities.setButtonWidth(2.2f),Utilities.setButtonHeight(2.2f));
        table.row();

        TextButton optionsButton = new TextButton("Options", skin);
        Utilities.setButtonColor(optionsButton);
        table.add(optionsButton).size(Utilities.setButtonWidth(2.2f),Utilities.setButtonHeight(2.2f));
        table.row();

        TextButton exitGameButton = new TextButton("Exit", skin);
        Utilities.setButtonColor(exitGameButton);
        table.add(exitGameButton).size(Utilities.setButtonWidth(2.2f),Utilities.setButtonHeight(2.2f));
        table.row();

        TextButton editorGameButton = new TextButton("Editor", skin);
        Utilities.setButtonColor(editorGameButton);
        table.add(editorGameButton).size(Utilities.setButtonWidth(2.2f),Utilities.setButtonHeight(2.2f));

        exitGameButton.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();
                return true;
            }
        });

        editorGameButton.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setEditorScreen();
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



    private void checkSaveFileExist(Button button){
        button.setVisible(false);
        if(Gdx.files.local("save.json").exists()){
            button.setVisible(true);
        }
    }
}
