package com.cosma.annihilation.Screens;

import box2dLight.RayHandler;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.*;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.*;
import com.cosma.annihilation.Annihilation;
import com.cosma.annihilation.Components.HealthComponent;
import com.cosma.annihilation.Components.TagComponent;
import com.cosma.annihilation.Editor.*;
import com.cosma.annihilation.Editor.CosmaMap.*;
import com.cosma.annihilation.Utils.Serialization.GameEntitySerializer;
import com.kotcrab.vis.ui.FocusManager;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.*;
import com.kotcrab.vis.ui.widget.file.FileChooser;

import java.util.ArrayList;
import java.util.List;

public class MapEditor implements Screen, InputProcessor {
    private RayHandler rayHandler;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private Stage stage;
    private Viewport viewport, viewportUi;
    private OrthographicCamera camera, cameraUi;
    private World world;
    private static final float MAX_STEP_TIME = 1 / 45f;
    private static float accumulator = 0f;
    private InputMultiplexer im;
    private boolean canCameraDrag = false;
    float zoomLevel = 0.3f;
    private GameMap gameMap;
    private Engine engine;
    private MapCreatorWindow mapCreatorWindow;
    private TilesPanel tilesPanel;
    public LayersPanel layersPanel;
    public ObjectPanel objectPanel;
    private MapRender mapRender;
    public LightsPanel lightsPanel;

    private boolean isTileLayerSelected, isObjectLayerSelected, isLightsLayerSelected, isEntityLayerSelected, isLightsRendered, drawGrid = true;
    private VisLabel editorModeLabel;
    private VisTable rightTable;
    private MenuBar menuBar;
    private FileChooser fileChooser;
    private Box2DDebugRenderer debugRenderer;
    private CareTaker careTaker;

    public MapEditor(Annihilation game) {
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        world = new World(new Vector2(0, -10), true);
        engine = new PooledEngine();


        cameraUi = new OrthographicCamera();
        cameraUi.update();
        viewportUi = new ScreenViewport(cameraUi);
        stage = new Stage(viewportUi);
        VisUI.load(VisUI.SkinScale.X1);
        rayHandler = new RayHandler(world);
        RayHandler.useDiffuseLight(true);
        isLightsRendered = false;

        mapCreatorWindow = new MapCreatorWindow(this);
        debugRenderer = new Box2DDebugRenderer();

        final Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        camera = new OrthographicCamera();
        viewport = new ExtendViewport(16 / 1.3f, 9 / 1.3f, camera);
        camera.update();
        camera.zoom = 5;
        viewport.apply(true);

        im = new InputMultiplexer();
        im.addProcessor(stage);
        im.addProcessor(this);
        batch = new SpriteBatch();

        editorModeLabel = new VisLabel("");
        editorModeLabel.setColor(Color.ORANGE);

        rightTable = new VisTable();
        VisTable leftTable = new VisTable();

        menuBar = new MenuBar();
        menuBar.getTable().add(editorModeLabel).center().expand();

        final VisCheckBox lightsButton = new VisCheckBox("Lights: ");
        lightsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (lightsButton.isChecked()) {
                    isLightsRendered = true;
                } else isLightsRendered = false;
            }
        });

        final VisCheckBox gridButton = new VisCheckBox("Grid: ");
        gridButton.setChecked(true);
        gridButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (gridButton.isChecked()) {
                    drawGrid = true;
                } else drawGrid = false;
            }
        });

        final VisTextButton undoButton = new VisTextButton("<");
        undoButton.setChecked(true);
        undoButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
            }
        });

        final VisTextButton redoButton = new VisTextButton(">");
        redoButton.setChecked(true);
        redoButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                loadEntity();

            }
        });

        menuBar.getTable().add(undoButton);
        menuBar.getTable().add(redoButton);
        menuBar.getTable().add(gridButton);
        menuBar.getTable().add(lightsButton);

        root.add(leftTable).expand().fill();
        root.add(rightTable).fillY();
        leftTable.add(menuBar.getTable()).expandX().fillX().row();
        leftTable.add().expand().fill();


        Menu fileMenu = new Menu("File");
        fileMenu.addItem(new MenuItem("New gameMap", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                createNewMap();
//                stage.addActor(mapCreatorWindow);
            }
        }));
        fileMenu.addItem(new MenuItem("Save", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                saveMap();
            }
        }).setShortcut("ctrl + s"));
        fileMenu.addItem(new MenuItem("Save as"));
        fileMenu.addItem(new MenuItem("Load map", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                loadMap();
            }
        }));
        fileMenu.addItem(new MenuItem("GameMap options"));
        fileMenu.addItem(new MenuItem("Exit"));
        menuBar.addMenu(fileMenu);

    }

    private void loadEntity() {
        FileHandle file = Gdx.files.local("entity/box.json");
        Json json = new Json();
        json.setSerializer(Entity.class,new GameEntitySerializer(world));
        Entity entity = json.fromJson(Entity.class, file);
        System.out.println(entity.getComponent(HealthComponent.class).hp);
        System.out.println(entity.getComponent(TagComponent.class).tag);
    }

    public void createNewMap() {
        gameMap = new GameMap(600, 100, 32);
        mapRender = new MapRender(shapeRenderer, gameMap, batch);

        loadPanels();
        EntityTreeWindow entityTreeWindow = new EntityTreeWindow(world);
        stage.addActor(entityTreeWindow);
    }

    public InputMultiplexer getInputMultiplexer() {
        return im;
    }

    public void createNewMap(int x, int y, int scale) {
        gameMap = new GameMap(x, y, scale);
        mapRender = new MapRender(shapeRenderer, gameMap, batch);

        layersPanel = new LayersPanel(this);
        layersPanel.setModal(false);
        rightTable.add(layersPanel).fillX().top().minHeight(layersPanel.getParent().getHeight() * 0.25f).maxHeight(layersPanel.getParent().getHeight() * 0.35f);
        rightTable.row();
        tilesPanel = new TilesPanel(this);
        rightTable.add(tilesPanel).fillX().top().minHeight(layersPanel.getParent().getHeight() * 0.25f).maxHeight(layersPanel.getParent().getHeight() * 0.35f);
        rightTable.row();
        rightTable.add().expandY();
        setCameraOnMapCenter();

    }

    private void loadMap() {
        CosmaMapLoader loader = new CosmaMapLoader("map/map.json", world, rayHandler);
        this.gameMap = loader.getMap();
        if (rightTable.hasChildren()) {
            rightTable.clear();
        }
        loadPanels();
        mapRender = new MapRender(shapeRenderer, gameMap, batch);
//        if(mapRender == null){
//            mapRender = new MapRender(shapeRenderer, gameMap, batch);
//            tilesPanel = new TilesPanel(this);
//            rightTable.add(tilesPanel).fillX().top().minHeight(tilesPanel.getParent().getHeight() * 0.25f).maxHeight(tilesPanel.getParent().getHeight() * 0.35f);
//        }
    }

    private void saveMap() {
        Json json = new Json();
        FileHandle file = Gdx.files.local("map/map.json");
        file.writeString(json.prettyPrint(gameMap), false);
    }

    private void setEditorModeLabel() {
        if (isTileLayerSelected) {
            editorModeLabel.setText("Tile edit mode");
        }
        if (isEntityLayerSelected) {
            editorModeLabel.setText("Entity edit mode");
        }
        if (isLightsLayerSelected) {
            editorModeLabel.setText("Light edit mode");
        }
        if (isObjectLayerSelected) {
            editorModeLabel.setText("Object edit mode");
        }
    }

    public GameMap getMap() {
        return gameMap;
    }

    public void setCameraOnMapCenter() {
        camera.position.set(4, 4, 0);
    }

    @Override
    public void show() {

        Gdx.input.setInputProcessor(im);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.8f, 0.8f, 0.8f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        float frameTime = Math.min(delta, 0.25f);
        engine.update(delta);
        accumulator += frameTime;
//        if (accumulator >= MAX_STEP_TIME) {
//            world.step(MAX_STEP_TIME, 6, 2);
//            accumulator -= MAX_STEP_TIME;
//        }
        debugRenderer.render(world, camera.combined);
        camera.update();
        cameraUi.update();
        batch.setProjectionMatrix(camera.combined);
        setEditorModeLabel();
        shapeRenderer.setProjectionMatrix(camera.combined);
        stage.act(delta);
        if (gameMap != null) {
            if (drawGrid) {
                mapRender.renderGrid();
            }
            Gdx.gl.glDisable(GL20.GL_BLEND);
            mapRender.renderMap();
        }
        if (isLightsRendered) {
            rayHandler.setCombinedMatrix(camera);
            rayHandler.updateAndRender();
        }
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, false);
        camera.update();
        cameraUi.update();
        viewportUi.update(width, height);
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
        if (keycode == Input.Buttons.MIDDLE) {
        }

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

    private void drawTile(int button) {
        if (Input.Buttons.LEFT == button && isTileLayerSelected) {
            if (gameMap != null && !gameMap.getLayers().isEmpty()) {
                Vector3 pos = getWorldCoordinates();
                int x = (int) pos.x;
                int y = (int) pos.y;

                if (tilesPanel.getAtlasPath() != null && tilesPanel.getAtlasRegionName() != null) {
                    Tile tile = new Tile();
                    tile.setTextureRegion(tilesPanel.getAtlasRegionName(), tilesPanel.getAtlasPath());
                    layersPanel.getSelectedLayer(TileMapLayer.class).setTile(x, y, tile);

                }
            }
        }
    }

    private void removeTile(int button) {
        if (Input.Buttons.RIGHT == button && isTileLayerSelected) {
            if (gameMap != null && !gameMap.getLayers().isEmpty()) {
                Vector3 pos = getWorldCoordinates();
                int x = (int) pos.x;
                int y = (int) pos.y;

                if (layersPanel.getSelectedLayer(TileMapLayer.class) != null) {
                    layersPanel.getSelectedLayer(TileMapLayer.class).setTile(x, y, null);

                }
            }
        }
    }

    public Vector3 getWorldCoordinates() {
        Vector3 worldCoordinates = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        Vector3 vec = camera.unproject(worldCoordinates);
        return vec;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        FocusManager.resetFocus(stage);
        drawTile(button);
        removeTile(button);
        if (button == Input.Buttons.MIDDLE) {
            canCameraDrag = true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.MIDDLE) {
            canCameraDrag = false;

        }
        return false;
    }

    public ShapeRenderer getShapeRenderer() {
        return shapeRenderer;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (canCameraDrag) {
            float x = Gdx.input.getDeltaX();
            float y = Gdx.input.getDeltaY();
            camera.translate(-x * (camera.zoom * 0.02f), y * (camera.zoom * 0.02f));
        }
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {


        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        if (amount == 1) {
            camera.zoom += zoomLevel;
        } else if (amount == -1) {
            camera.zoom -= zoomLevel;
        }
        return false;
    }

    private void loadPanels(){
        layersPanel = new LayersPanel(this);
        rightTable.add(layersPanel).fillX().top().minHeight(layersPanel.getParent().getHeight() * 0.25f).maxHeight(layersPanel.getParent().getHeight() * 0.25f);
        rightTable.row();

        tilesPanel = new TilesPanel(this);
        rightTable.add(tilesPanel).fillX().top().minHeight(tilesPanel.getParent().getHeight() * 0.25f).maxHeight(tilesPanel.getParent().getHeight() * 0.25f);
        rightTable.row();

        objectPanel = new ObjectPanel(this);
        rightTable.add(objectPanel).fillX().top().minHeight(objectPanel.getParent().getHeight() * 0.25f).maxHeight(objectPanel.getParent().getHeight() * 0.25f);
        rightTable.row();

        lightsPanel = new LightsPanel(this, rayHandler);
        rightTable.add(lightsPanel).fillX().top().minHeight(lightsPanel.getParent().getHeight() * 0.25f).maxHeight(lightsPanel.getParent().getHeight() * 0.25f);
        rightTable.row();

        rightTable.add().expandY();
        im.addProcessor(objectPanel);
        im.addProcessor(lightsPanel);
        setCameraOnMapCenter();
    }

    public void setTileLayerSelected(boolean tileLayerSelected) {
        isTileLayerSelected = tileLayerSelected;
    }

    public void setObjectLayerSelected(boolean objectLayerSelected) {
        isObjectLayerSelected = objectLayerSelected;
    }

    public void setLightsLayerSelected(boolean lightsLayerSelected) {
        isLightsLayerSelected = lightsLayerSelected;
    }

    public void setEntityLayerSelected(boolean entityLayerSelected) {
        isEntityLayerSelected = entityLayerSelected;
    }

    public boolean isTileLayerSelected() {
        return isTileLayerSelected;
    }

    public boolean isObjectLayerSelected() {
        return isObjectLayerSelected;
    }

    public boolean isLightsLayerSelected() {
        return isLightsLayerSelected;
    }

    public boolean isEntityLayerSelected() {
        return isEntityLayerSelected;
    }

    public World getWorld() {
        return world;
    }

    public void redoMap() {

    }

    public void undoMap() {
        for(Memento memento: careTaker.mementoList){
            System.out.println(memento.getMap().getLayers().size());
        }
    }

//    public void saveMapState() {
//        careTaker.add(new Memento(this.getMap()));
////        careTaker.clearOldState();
//    }

    private class CareTaker {
        private List<Memento> mementoList = new ArrayList<Memento>();

        public void clearOldState() {
            if (mementoList.size() >= 20) {
                mementoList.remove(0);
            }
        }

        public void add(Memento state) {
            mementoList.add(state);

        }

        public Memento get(int index) {
            return mementoList.get(index);
        }
    }

    private class Memento {
        GameMap map;

        public Memento(GameMap map) {
            this.map = map;
            System.out.println(map.getLayers().size());
        }

        public GameMap getMap() {
            return map;
        }
    }
}
