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
    private TextButton debugButtonGui;
    private TextButton menuButton;
    private TextButton stateCheckButton;
    private Window window;
    Label label;
    Label label1;
    private Label label2;
    private Label label3;
    private Label label4;
    private Label label5;

    public OnScreenGui(){

        camera = new OrthographicCamera();
        camera.update();
        viewport = new ExtendViewport(Gdx.graphics.getWidth() , Gdx.graphics.getHeight(),camera);
        viewport.apply(true);
        setViewport(viewport);
        skin = new Skin(Gdx.files.internal("UI/uiskin.json"));
        tpad = new Touchpad(2,skin,"default");
        addActors();
    }

    private void addActors(){
        Table table = new Table();
        table.center();
        table.setFillParent(true);
        //Table
        window = new Window("Character",skin);
        window.setPosition(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
        window.setSize(Gdx.graphics.getWidth()/4,Gdx.graphics.getHeight()/4);
        window.setVisible(false);
        addActor(window);
        stateCheckButton = new TextButton("State check", skin);
        stateCheckButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (stateCheckButton.isChecked()) {
                    window.setVisible(true);
                } else
                    window.setVisible(false);
            }
        });
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
        debugButtonGui = new TextButton("Debug mode GUI", skin);
        debugButtonGui.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(debugButtonGui.isChecked()){
                    debugButtonGui.setText("Debug mode - enabled");
                    StateManager.debugModeGui = true;
                }
                else {
                    debugButtonGui.setText("Debug mode GUI");
                    StateManager.debugModeGui = false;
                }
            }

        });
        debugButton = new TextButton("Debug mode ", skin);
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
        label = new Label("1" ,skin);
        label1 = new Label("1" ,skin);
        label2 = new Label("1" ,skin);
        label3 = new Label("1" ,skin);
        label4 = new Label("1" ,skin);
        label5 = new Label("1" ,skin);
        table.add(debugButton).padTop(10).padLeft(10).left().width(150).height(50);
        table.add(menuButton).padTop(10).padRight(10).right().width(150).height(50);
        table.row();
        table.add(debugButtonGui).padTop(10).padLeft(10).left().width(150).height(50);
        table.row();
        table.add(stateCheckButton).padTop(10).padLeft(10).left().width(150).height(50);
        table.row();
        table.add(label).padTop(10).padLeft(10).left();
        table.row();
        table.add(label1).padTop(10).padLeft(10).left();
        table.row();
        table.add(label2).padTop(10).padLeft(10).left();
        table.row();
        table.add(label3).padTop(10).padLeft(10).left();
        table.row();
        table.add(label4).padTop(10).padLeft(10).left();
        table.row();
        table.add(label5).padTop(10).padLeft(10).left();
        table.row();
        table.add(tpad).expandX().padBottom(10).padLeft(10).width(400).height(400).fillY().expandY().bottom().left();
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
   public void actlab(){
       label.setText("canClimb ="  + StateManager.canClimb);
       label1.setText("climbing ="  + StateManager.climbing);
       label2.setText("canMoveOnSide ="  + StateManager.canMoveOnSide);
       label3.setText("canJump ="  + StateManager.canJump);
       label4.setText("onGround ="  + StateManager.onGround);
       label5.setText("canClimbDown ="  + StateManager.canClimbDown);
   }


    @Override
    public void dispose() {
    }
}
