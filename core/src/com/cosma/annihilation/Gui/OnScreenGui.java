package com.cosma.annihilation.Gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class OnScreenGui extends Stage implements Disposable {
    private Stage stage;
    private Viewport viewport;
    private Camera camera;
    private Touchpad tpad;

    public OnScreenGui(){

        camera = new OrthographicCamera();
        viewport = new ExtendViewport(16, 9,camera);
        viewport.apply(true);
    }

    private void addActor(){

        Skin skin = new Skin(Gdx.files.internal("UI/uiskin.json"));
        Table table = new Table();
        table.top();
        table.setFillParent(true);
        //Debug Mode
        table.setDebug(true);

        table.row();
        table.row();

        //Touchpad
//        tpad = new Touchpad(0,skin,"default");
//        table.add(tpad).expandX().padBottom(10).padLeft(10).width(50).height(50).fillY().expandY().bottom().left();
//        table.row();
        //Button
        addActor(table);
    }


    public void render(float deltaTime) {

    }

    public void resize(int width, int height) {
        //Update the viewport size
        Viewport viewport = stage.getViewport();
        viewport.update(width, height);

        //Reposition the viewport to the centre
        Camera stageCam = stage.getViewport().getCamera();
        stageCam.position.x = Gdx.graphics.getWidth() / 2;
        stageCam.position.y = Gdx.graphics.getHeight() / 2;
        stageCam.update();
    }


    public Stage getStage() {
        return stage;
    }

    public Camera getCamera() {
        return camera;
    }



    @Override
    public void dispose() {

    }
}
