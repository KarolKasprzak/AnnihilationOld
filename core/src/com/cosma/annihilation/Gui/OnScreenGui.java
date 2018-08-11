package com.cosma.annihilation.Gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.cosma.annihilation.Utils.StateManager;

public class OnScreenGui extends Stage implements Disposable {

    private Stage stage;
    private Viewport viewport;
    private Camera camera;
    private ImageButton actionButtonUper;
    private ImageButton actionButtonUp;
    private ImageButton actionButtonDown;
    private ImageButton actionButtonLeft;
    private ImageButton actionButtonRight;
    private Skin skin;
    private TextButton debugButton;
    private TextButton debugButtonGui;
    private TextButton menuButton;
    private TextButton stateCheckButton;
    private Window window;
    private Label label;
    private Label label1;
    private Label label2;
    private Label label3;
    private Label label4;
    private Label label5;
    private static Label actionLabel;
    private InventoryWindow inventoryWindow;

    public OnScreenGui(Camera camera){

        this.camera = camera;
        camera.update();
        viewport = new ScreenViewport(camera);
//        viewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),camera);
        viewport.apply(true);
        this.setViewport(viewport);

        skin = new Skin(Gdx.files.internal("UI/skin/pixthulhu-ui.json"));
        createUI();
        setInventoryWindow();
        System.out.println(this.getWidth());
    }

    private void setInventoryWindow(){
        inventoryWindow = new InventoryWindow("Inventory", skin);
        float x = Gdx.graphics.getWidth() * 0.6f;
        float y = Gdx.graphics.getHeight() * 0.8f;
        inventoryWindow.setZIndex(10);
        inventoryWindow.setSize(300,300);
        inventoryWindow.setPosition(this.getWidth()/2,0);

        inventoryWindow.setMovable(false);
        inventoryWindow.setVisible(false);
        addActor(inventoryWindow);
    }

    private void createUI(){
        Table table = new Table();
        table.center();
        table.setFillParent(true);

        Table bTable = new Table();
        bTable.bottom().left();
        bTable.setFillParent(true);
        addActor(table);
        addActor(bTable);

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
                    inventoryWindow.setVisible(true);
                } else
                    inventoryWindow.setVisible(false);
            }
        });

        label = new Label("1" ,skin);
        label1 = new Label("1" ,skin);
        label2 = new Label("1" ,skin);
        label3 = new Label("1" ,skin);
        label4 = new Label("1" ,skin);
        label5 = new Label("1" ,skin);
        actionLabel = new Label("test",skin);

        addActor(actionLabel);
        table.add(debugButton).padTop(10).padLeft(10).left().width(150).height(50);
        table.add(menuButton).padTop(10).padRight(10).right().width(150).height(50);
        table.row();
        table.add(debugButtonGui).padTop(10).padLeft(10).left().width(150).height(50);
        table.row();
        table.add(stateCheckButton).padTop(10).padLeft(10).left().width(150).height(50);
        table.row();
        table.add(label).padTop(10).padLeft(10).left();
        table.row();
        table.add(label1).padTop(10).padLeft(10).left().expandX();
        table.row();
        table.add(label2).padTop(10).padLeft(10).left();
        table.row();
        table.add(label3).padTop(10).padLeft(10).left();
        table.row();
        table.add(label4).padTop(10).padLeft(10).left();
        table.row();
        table.add(label5).padTop(10).padLeft(10).left().colspan(3);
        table.row();
        table.add(bTable).left().bottom().expandY().padBottom(15).padLeft(15).size(300);
            bTable.add(actionButtonUp).width(150).height(150).center().colspan(3);
            bTable.row();
            bTable.add(actionButtonLeft).width(150).height(150).left().pad(10);
            bTable.add(actionButtonDown).width(150).height(150).left().pad(10);
            bTable.add(actionButtonRight).width(150).height(150).left().pad(10);


//        table.add(tpad).expandX().padBottom(10).padLeft(10).width(450).height(450).fillY().expandY().bottom().left();
//        table.add(actionButtonUper).expandX().padBottom(10).padRight(10).width(150).height(150).fillY().expandY().bottom().right();
        table.row();



    }
   public static void setLabelText(String text){
        actionLabel.setText(text);
   }
   public static void setLabelposition(float x,float y){
        actionLabel.setPosition(x,y);
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
