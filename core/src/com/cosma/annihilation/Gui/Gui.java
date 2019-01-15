package com.cosma.annihilation.Gui;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.cosma.annihilation.Annihilation;
import com.cosma.annihilation.Components.ContainerComponent;
import com.cosma.annihilation.Components.PlayerComponent;
import com.cosma.annihilation.Systems.ActionSystem;
import com.cosma.annihilation.Systems.ShootingSystem;
import com.cosma.annihilation.Utils.AssetLoader;
import com.cosma.annihilation.Utils.Enums.ActionID;
import com.cosma.annihilation.Utils.Enums.GameEvent;
import com.cosma.annihilation.Utils.GfxAssetDescriptors;
import com.cosma.annihilation.Utils.StateManager;
import com.cosma.annihilation.Utils.Utilities;

public class Gui implements Screen {
    private Stage stage;
    private Skin skin;
    private Label fpsLabel;
    private Label footContact;
    private Label onground;
    private Label climbing;
    private Label canClimb;
    private Label canJump;
    private Label canClimbDown;
    private Label weaponHidden;


    private String fpsNumber;
    private ImageButton actionButtonRightUp;
    private ImageButton actionButtonRightDown;
    private ImageButton actionButtonRightCenter;
    private ImageButton actionButtonUp;
    private ImageButton actionButtonDown;
    private ImageButton actionButtonLeft;
    private ImageButton actionButtonRight;
    private TextButton debugButton;
    private TextButton menuButton;
    private ProgressBar healthBar;
    private Engine engine;
    private Entity player;
    private World world;
    private Signal<GameEvent> signal;
    private Label actionNameDisplayed;
    private ContainerWindow containerWindow;
    private AssetLoader assetLoader;
    private MenuWindow menuWindow;
    private boolean rightUpButtonPressed = false;


    public Gui(final Engine engine, World world, AssetLoader assetLoader) {
        this.world = world;
        this.engine = engine;
        this.assetLoader = assetLoader;
        skin = assetLoader.manager.get(GfxAssetDescriptors.skin);
        signal = new Signal<GameEvent>();

        Camera camera = new OrthographicCamera();
        camera.update();
        Viewport viewport = new ScreenViewport(camera);
        stage = new Stage(viewport);
        viewport.apply(true);




        containerWindow = new ContainerWindow("", skin, 4, engine);
        containerWindow.setSize(Utilities.setWindowWidth(0.4f), Utilities.setWindowHeight(0.5f));
        containerWindow.setVisible(false);
        containerWindow.setPosition(Gdx.graphics.getWidth() / 2 - (Utilities.setWindowWidth(0.4f) / 2), Gdx.graphics.getHeight() / 2 - (Utilities.setWindowHeight(0.5f) / 2));
        stage.addActor(containerWindow);

        fpsLabel = new Label(fpsNumber, skin);
        climbing = new Label("", skin);
        onground = new Label("", skin);
        footContact = new Label("", skin);

        canClimb = new Label("", skin);
        canJump = new Label("", skin);
        canClimbDown = new Label("", skin);
        weaponHidden = new Label("", skin);



        actionNameDisplayed = new Label("", skin);
        actionButtonRightUp = new ImageButton(new TextureRegionDrawable(new TextureRegion(Annihilation.getAssets().get(GfxAssetDescriptors.gui_buttons).findRegion("button_b"))));
        actionButtonRightDown = new ImageButton(new TextureRegionDrawable(new TextureRegion(Annihilation.getAssets().get(GfxAssetDescriptors.gui_buttons).findRegion("button_a"))));
        actionButtonRightCenter = new ImageButton(new TextureRegionDrawable(new TextureRegion(Annihilation.getAssets().get(GfxAssetDescriptors.gui_buttons).findRegion("button_x"))));
        actionButtonUp = new ImageButton(new TextureRegionDrawable(new TextureRegion(Annihilation.getAssets().get(GfxAssetDescriptors.gui_buttons).findRegion("button_up"))));
        actionButtonDown = new ImageButton(new TextureRegionDrawable(new TextureRegion(Annihilation.getAssets().get(GfxAssetDescriptors.gui_buttons).findRegion("button_down"))));
        actionButtonLeft = new ImageButton(new TextureRegionDrawable(new TextureRegion(Annihilation.getAssets().get(GfxAssetDescriptors.gui_buttons).findRegion("button_left"))));
        actionButtonRight = new ImageButton(new TextureRegionDrawable(new TextureRegion(Annihilation.getAssets().get(GfxAssetDescriptors.gui_buttons).findRegion("button_right"))));

//        actionButtonRightUp.addListener(new ActorGestureListener() {
//            @Override
//            public void tap(InputEvent event, float x, float y, int count, int button) {
//                super.tap(event, x, y, count, button);
//                player = engine.getEntitiesFor(Family.all(PlayerComponent.class).get()).first();
//                if (player.getComponent(PlayerComponent.class).isWeaponHidden) {
//                    signal.dispatch(GameEvent.PERFORM_ACTION);
//                } else
//                    signal.dispatch(GameEvent.WEAPON_SHOOT);
//            }
//
//            @Override
//            public boolean longPress(Actor actor, float x, float y) {
//                signal.dispatch(GameEvent.WEAPON_TAKE_OUT);
//                return true;
//            }
//        });
        actionButtonRightUp.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//                player = engine.getEntitiesFor(Family.all(PlayerComponent.class).get()).first();
//                if (player.getComponent(PlayerComponent.class).isWeaponHidden) {
//                    signal.dispatch(GameEvent.PERFORM_ACTION);
//                } else
//                    signal.dispatch(GameEvent.WEAPON_SHOOT);
                signal.dispatch(GameEvent.ACTION_BUTTON_TOUCH_DOWN);
                signal.dispatch(GameEvent.PERFORM_ACTION);
                return true;
            }


            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                   signal.dispatch(GameEvent.ACTION_BUTTON_TOUCH_UP);
            }
        });

        actionButtonRightCenter.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                signal.dispatch(GameEvent.WEAPON_TAKE_OUT);
                return true;
            }
        });
        actionButtonRightDown.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                StateManager.goUp = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                StateManager.goUp = false;
            }
        });
        actionButtonUp.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                {
                    StateManager.goUp = true;
                }
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                StateManager.goUp = false;
            }

        });
        actionButtonDown.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                StateManager.goDown = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                StateManager.goDown = false;
            }

        });
        actionButtonLeft.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                StateManager.goLeft = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                StateManager.goLeft = false;
            }

        });
        actionButtonRight.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                StateManager.goRight = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                StateManager.goRight = false;
            }
        });

        createHUD();
        createMenuWindow();
    }

    private void createMenuWindow() {
        menuWindow = new MenuWindow("", skin, world, engine, Utilities.setWindowWidth(0.95f), Utilities.setWindowHeight(0.95f), this);
        menuWindow.setPosition(Gdx.graphics.getWidth() / 2 - (Utilities.setWindowWidth(0.95f) / 2), Gdx.graphics.getHeight() / 2 - (Utilities.setWindowHeight(0.95f) / 2));
        menuWindow.setMovable(true);
        menuWindow.setVisible(false);
        menuWindow.setFillParent(true);
        stage.addActor(menuWindow);
    }

    public void addEngine() {
        signal.add(engine.getSystem(ActionSystem.class));
        signal.add(engine.getSystem(ShootingSystem.class));
    }


    public void showLootWindow(Entity entity) {
        containerWindow.setVisible(true);
        InventoryWindow.fillInventory(containerWindow.containerSlotsTable, entity.getComponent(ContainerComponent.class).itemLocations, containerWindow.dragAndDrop);
    }

    public void setDisplayedActionName(ActionID actionID) {
        switch (actionID) {
            case NOTHING:
                actionNameDisplayed.setText("");
                break;
            case OPEN:
                actionNameDisplayed.setText("Open box");
                break;
        }
    }

    private void createHUD() {
        Table table = new Table();
        table.center();
        table.setFillParent(true);
        Table bTable = new Table();
        bTable.bottom().left();
        bTable.setFillParent(true);



        stage.addActor(table);
        stage.addActor(bTable);

        menuButton = new TextButton("Menu", skin);
        Utilities.setButtonColor(menuButton);
        menuButton.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!menuWindow.isVisible()) {
                    menuWindow.setVisible(true);
                }
                return true;
            }
        });
        debugButton = new TextButton("Debug mode ", skin);
        Utilities.setButtonColor(debugButton);

        debugButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (debugButton.isChecked()) {
                    debugButton.setText("Debug mode - enabled");
                    StateManager.debugMode = true;
                } else {
                    debugButton.setText("Debug mode");
                    StateManager.debugMode = false;
                }
            }

        });

        table.add(debugButton).padTop(10).padLeft(10).left().width(180).height(50);
        table.add(actionNameDisplayed).center().expandX();
        table.add(menuButton).padTop(10).padRight(10).right().width(180).height(50);
        table.row();
        table.add(fpsLabel).padTop(0).padLeft(10).left().width(150).height(50);
        table.row();
        table.add(climbing).padTop(0).padLeft(10).left().width(250).height(50);
        table.row();
        table.add(onground).padTop(0).padLeft(10).left().width(250).height(50);
        table.row();
        table.add(canClimb).padTop(0).padLeft(10).left().width(250).height(50);
        table.row();
        table.add(canJump).padTop(0).padLeft(10).left().width(250).height(50);
        table.row();
        table.add(canClimbDown).padTop(10).padLeft(10).left().width(250).height(50);
        table.row();
        table.add(weaponHidden).padTop(0).padLeft(10).left().width(250).height(50);
        table.row();

        //-------------------Gui button table------------------------------------------------

        bTable.add().width(Utilities.setButtonWidth(1f)).height(Utilities.setButtonWidth(1f)).center().pad(10);
        bTable.add(actionButtonUp).width(Utilities.setButtonWidth(1f)).height(Utilities.setButtonWidth(1f)).center().pad(10);
        bTable.add().width(Utilities.setButtonWidth(1f)).height(Utilities.setButtonWidth(1f)).center().pad(10);
        bTable.add(actionButtonRightUp).expandX().right().padBottom(15).padLeft(15).width(Utilities.setButtonWidth(1f)).height(Utilities.setButtonWidth(1f)).padRight(25).colspan(2);
        bTable.row();
        bTable.add(actionButtonLeft).width(Utilities.setButtonWidth(1f)).height(Utilities.setButtonWidth(1f)).left().pad(10);
        bTable.add(actionButtonDown).width(Utilities.setButtonWidth(1f)).height(Utilities.setButtonWidth(1f)).left().pad(10);
        bTable.add(actionButtonRight).width(Utilities.setButtonWidth(1f)).height(Utilities.setButtonWidth(1f)).left().pad(10).expandX();
        bTable.add(actionButtonRightDown).right().padBottom(15).padLeft(15).width(Utilities.setButtonWidth(1f)).height(Utilities.setButtonWidth(1f)).padRight(25).expandX();
        bTable.add(actionButtonRightCenter).right().padBottom(15).padLeft(15).width(Utilities.setButtonWidth(1f)).height(Utilities.setButtonWidth(1f)).padRight(25);
        table.row();
        table.add(bTable).left().bottom().expandY().padBottom(15).padLeft(15);
    }

    public Stage getStage() {
        return stage;
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        float fpss = Gdx.graphics.getFramesPerSecond();
        player = engine.getEntitiesFor(Family.all(PlayerComponent.class).get()).first();
        fpsNumber = Float.toString(fpss);
        fpsLabel.setText(fpsNumber);
        climbing.setText("climbing " + StateManager.climbing);
        onground.setText("onGround " + StateManager.onGround);
        canClimb.setText("canclimb " + StateManager.canClimb);
        canClimbDown.setText("canclimbdown " + StateManager.canClimbDown);
        canJump.setText("can jump " + StateManager.canJump);
        weaponHidden.setText("weapon is hidden " + player.getComponent(PlayerComponent.class).isWeaponHidden);
        fpsLabel.setColor(0, 82, 0, 255);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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

    public void loadGame() {
        menuWindow.loadGame();
    }


}




