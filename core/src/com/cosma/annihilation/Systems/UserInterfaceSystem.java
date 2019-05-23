package com.cosma.annihilation.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.cosma.annihilation.Annihilation;
import com.cosma.annihilation.Components.ContainerComponent;
import com.cosma.annihilation.Components.HealthComponent;
import com.cosma.annihilation.Components.PlayerComponent;
import com.cosma.annihilation.Gui.ContainerWindow;
import com.cosma.annihilation.Gui.PlayerInventoryWindow;
import com.cosma.annihilation.Gui.GameMainMenuWindow;
import com.cosma.annihilation.Utils.Constants;
import com.cosma.annihilation.Utils.Enums.GameEvent;
import com.cosma.annihilation.Utils.Util;
import com.cosma.annihilation.World.WorldBuilder;

public class UserInterfaceSystem extends IteratingSystem implements Listener<GameEvent> {

    private Stage stage;
    private Label fpsLabel;
    private GameMainMenuWindow gameMainMenuWindow;
    private ContainerWindow containerWindow;
    private Image playerHealthStatusIcon;
    private PlayerComponent playerComponent;
    private ComponentMapper<PlayerComponent> playerMapper;
    private BitmapFont font;

    public UserInterfaceSystem(Engine engine, World world, WorldBuilder worldBuilder) {
        super(Family.all(PlayerComponent.class).get(), Constants.USER_INTERFACE);

        playerMapper = ComponentMapper.getFor(PlayerComponent.class);

        Skin skin = Annihilation.getAssets().get("gfx/interface/uiskin.json", Skin.class);
        Camera camera = new OrthographicCamera();
        camera.update();
        Viewport viewport = new ScreenViewport();
        stage = new Stage(viewport);
        stage.getViewport().apply(true);

        Table coreTable = new Table();
        coreTable.setDebug(true);
        coreTable.setFillParent(true);
        stage.addActor(coreTable);

        Signal<GameEvent> signal = new Signal<GameEvent>();

        containerWindow = new ContainerWindow("", skin, 4, engine);
        containerWindow.setSize(Util.setWindowWidth(0.4f), Util.setWindowHeight(0.5f));
        containerWindow.setVisible(false);
        containerWindow.setPosition(Gdx.graphics.getWidth() / 2 - (Util.setWindowWidth(0.4f) / 2), Gdx.graphics.getHeight() / 2 - (Util.setWindowHeight(0.5f) / 2));
        stage.addActor(containerWindow);

        gameMainMenuWindow = new GameMainMenuWindow("", skin, engine, Util.setWindowWidth(0.95f), Util.setWindowHeight(0.95f),world,worldBuilder);
        gameMainMenuWindow.setPosition(Gdx.graphics.getWidth() / 2 - (Util.setWindowWidth(0.95f) / 2), Gdx.graphics.getHeight() / 2 - (Util.setWindowHeight(0.95f) / 2));
        gameMainMenuWindow.setVisible(false);
        gameMainMenuWindow.setFillParent(true);
        stage.addActor(gameMainMenuWindow);

        fpsLabel = new Label("", skin);
        playerHealthStatusIcon = new Image(Annihilation.getAssets().get("gfx/textures/player_health.png",Texture.class));


        coreTable.add(fpsLabel).left().top().expandX().expandY();
        coreTable.add(playerHealthStatusIcon).top();

        font = new BitmapFont();
        font.setColor(Color.RED);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        fpsLabel.setText(Float.toString(Gdx.graphics.getFramesPerSecond()));
        stage.act(deltaTime);
        stage.draw();

    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        playerMapper.get(entity);

        if(entity.getComponent(HealthComponent.class).hp < entity.getComponent(HealthComponent.class).maxHP/2){
            playerHealthStatusIcon.setDrawable(new TextureRegionDrawable(new TextureRegion(Annihilation.getAssets().get("gfx/textures/player_health_bad.png",Texture.class))));
        }

    }


    void showLootWindow(Entity entity) {
        containerWindow.setVisible(true);
        PlayerInventoryWindow.fillInventory(containerWindow.containerSlotsTable, entity.getComponent(ContainerComponent.class).itemLocations, containerWindow.dragAndDrop);

    }

    public void resizeHUD(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public Stage getStage() {
        return stage;
    }

    @Override
    public void receive(Signal<GameEvent> signal, GameEvent event) {
        switch (event) {
            case OPEN_MENU:
                if (gameMainMenuWindow.isVisible()) {
                    gameMainMenuWindow.setVisible(false);
                } else gameMainMenuWindow.setVisible(true);
                break;
        }
    }
}
