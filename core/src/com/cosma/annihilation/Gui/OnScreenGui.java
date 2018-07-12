package com.cosma.annihilation.Gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.cosma.annihilation.Utils.StateManager;

public class OnScreenGui extends Stage implements Disposable {
    private Stage stage;
    private Viewport viewport;
    private Camera camera;
    static Touchpad tpad;
    private Skin skin;
    private TextButton debugButton;
    private TextButton menuButton;
    private Window window;

    public OnScreenGui(){

        camera = new OrthographicCamera();
        camera.update();
        viewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),camera);
        viewport.apply(true);
        setViewport(viewport);
        skin = new Skin(Gdx.files.internal("UI/uiskin.json"));
        tpad = new Touchpad(0,skin,"default");
        addActor();
    }

    private void addActor(){
        Table table = new Table();
        table.center();
        table.setFillParent(true);
        //Debug Mode
        //Table
        window = new Window("Character",skin);
        window.setPosition(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
        window.setSize(Gdx.graphics.getWidth()/4,Gdx.graphics.getHeight()/4);
        window.setVisible(false);
        addActor(window);

        menuButton = new TextButton("Character menu", skin);
        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (menuButton.isChecked()) {
                    window.setVisible(true);
                } else
                    window.setVisible(false);
            }
        });
        debugButton = new TextButton("Debug mode", skin);
        debugButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(debugButton.isChecked()){
                    debugButton.setText("Debug mode - enabled");
                    StateManager.debugMode = true;
                }
                else {
                    debugButton.setText("Debug mode");
                    StateManager.debugMode = false;
                }
            }
        });
        table.add(debugButton).padTop(10).padLeft(10).left();
        table.add(menuButton).padTop(10).padRight(10).right();
        table.row();
        table.row();
        table.add(tpad).expandX().padBottom(10).padLeft(10).width(200).height(200).fillY().expandY().bottom().left();
        table.row();
        addActor(table);
    }


//    public void resize(int width, int height) {
//        //Update the viewport size
//
////        viewport.update(width, height);
////        viewport.apply(true);
////        camera.update();
//        //Reposition the viewport to the centre
////        Camera stageCam = getViewport().getCamera();
////        stageCam.update();
//    }
        static public Touchpad getTouchpad(){
        return  tpad;
    }



    @Override
    public void dispose() {
    }
}
