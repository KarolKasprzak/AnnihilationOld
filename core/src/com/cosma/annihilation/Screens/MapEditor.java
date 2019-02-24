package com.cosma.annihilation.Screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.*;
import com.cosma.annihilation.Annihilation;
import com.cosma.annihilation.Editor.*;
import com.cosma.annihilation.Editor.CosmaMap.*;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.*;
import com.kotcrab.vis.ui.widget.file.FileChooser;

public class MapEditor implements Screen, InputProcessor {

    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private Stage stage;
    private Viewport viewport, viewportUi;
    private OrthographicCamera camera, cameraUi;
    private World world;
    private final float TIME_STEP = 1 / 300f;
    private float accumulator = 0f;
    public InputMultiplexer im;

    private boolean canCameraDrag = false;
    float zoomLevel = 0.3f;

    private GameMap gameMap;
    private MapCreatorWindow mapCreatorWindow;

    private TilesPanel tilesPanel;
    public LayersPanel layersPanel;
    public ObjectPanel objectPanel;
    private MapRender mapRender;

    private boolean isTileLayerSelected;
    private boolean isObjectLayerSelected;
    private boolean isLightsLayerSelected;
    private boolean isEntityLayerSelected;

//    private boolean startDraw = false;
    private boolean canDraw = false;

    private VisLabel editorModeLabel;

    private VisTable rightTable;

    private MenuBar menuBar;
    private FileChooser fileChooser;

    private float x1;
    private float y1;
    private float x2;
    private float y2;

    public MapEditor(Annihilation game) {
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);

        cameraUi = new OrthographicCamera();
        cameraUi.update();
        viewportUi = new ScreenViewport(cameraUi);
        stage = new Stage(viewportUi);
        VisUI.load(VisUI.SkinScale.X1);

        mapCreatorWindow = new MapCreatorWindow(this);

        final Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        camera = new OrthographicCamera();
        viewport = new ExtendViewport(16 / 1.3f, 9 / 1.3f, camera);
        camera.update();
        camera.zoom = 5;
        viewport.apply(true);
        world = new World(new Vector2(0, 9), false);

        im = new InputMultiplexer();
        im.addProcessor(stage);
        im.addProcessor(this);
        batch = new SpriteBatch();

        editorModeLabel = new VisLabel("");
        editorModeLabel.setColor(Color.ORANGE);

        rightTable = new VisTable();
        VisTable leftTable = new VisTable();

        menuBar = new MenuBar();
//        menuBar.getTable().add().expandX();
        menuBar.getTable().add(editorModeLabel).center().expand();

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

    public void createNewMap() {
        gameMap = new GameMap(5,5, 32);
        mapRender = new MapRender(shapeRenderer, gameMap, batch);

        layersPanel = new LayersPanel(this);
        rightTable.add(layersPanel).fillX().top().minHeight(layersPanel.getParent().getHeight()*0.25f).maxHeight(layersPanel.getParent().getHeight()*0.35f);
        rightTable.row();

        tilesPanel = new TilesPanel(this);
        rightTable.add(tilesPanel).fillX().top().minHeight(tilesPanel.getParent().getHeight()*0.25f).maxHeight(tilesPanel.getParent().getHeight()*0.35f);
        rightTable.row();

        objectPanel = new ObjectPanel(this);
        rightTable.add(objectPanel).fillX().top().minHeight(objectPanel.getParent().getHeight()*0.25f).maxHeight(objectPanel.getParent().getHeight()*0.35f);
        rightTable.row();
        rightTable.add().expandY();
        setCameraOnMapCenter();
    }


    public void createNewMap(int x, int y, int scale) {
        gameMap = new GameMap(x, y, scale);
        mapRender = new MapRender(shapeRenderer, gameMap, batch);

        layersPanel = new LayersPanel(this);
        layersPanel.setModal(false);
        rightTable.add(layersPanel).fillX().top().minHeight(layersPanel.getParent().getHeight()*0.25f).maxHeight(layersPanel.getParent().getHeight()*0.35f);
        rightTable.row();
        tilesPanel = new TilesPanel(this);
        rightTable.add(tilesPanel).fillX().top().minHeight(layersPanel.getParent().getHeight()*0.25f).maxHeight(layersPanel.getParent().getHeight()*0.35f);
        rightTable.row();
        rightTable.add().expandY();
        setCameraOnMapCenter();
    }

    private void loadMap() {
        CosmaMapLoader loader = new CosmaMapLoader("map/map.json");
        this.gameMap = loader.getMap();
        mapRender = new MapRender(shapeRenderer, gameMap, batch);
        stage.addActor(layersPanel = new LayersPanel(this));
    }

    private void saveMap() {
        Json json = new Json();
        FileHandle file = Gdx.files.local("map/map.json");
        file.writeString(json.prettyPrint(gameMap), false);
    }

    private void setEditorModeLabel(){
        if(isTileLayerSelected){
            editorModeLabel.setText("Tile edit mode");
        }
        if(isEntityLayerSelected){
            editorModeLabel.setText("Entity edit mode");
        }
        if(isLightsLayerSelected){
            editorModeLabel.setText("Light edit mode");
        }
        if(isObjectLayerSelected){
            editorModeLabel.setText("Object edit mode");
        }
    }

    public GameMap getMap() {
        return gameMap;
    }

    public void setCameraOnMapCenter() {
        camera.position.set(getMap().getHeight() / 2, getMap().getWidth() / 2, 0);
    }

    @Override
    public void show() {

        Gdx.input.setInputProcessor(im);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.8f, 0.8f, 0.8f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        setEditorModeLabel();
        shapeRenderer.setProjectionMatrix(camera.combined);
        if(canDraw){
            drawBox();
        }
        shapeRenderer.begin();
        if (gameMap != null)
            mapRender.renderMap();
        shapeRenderer.end();
        stage.act(delta);
        stage.draw();
        camera.update();
        cameraUi.update();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, false);
        camera.update();
        cameraUi.update();
        viewportUi.update(width, height);
    }


    private void act(float delta) {
        // Fixed time step
        accumulator += delta;
        while (accumulator >= delta) {
            world.step(TIME_STEP, 6, 2);
            accumulator -= TIME_STEP;
        }
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
                    Tile tile = new Tile();
                    layersPanel.getSelectedLayer(TileMapLayer.class).setTile(x, y, tile);
                }
            }
        }
    }


    public Vector3 getWorldCoordinates() {
        Vector3 worldCoordinates = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        Vector3 vec = camera.unproject(worldCoordinates);
        return vec;
    }

    private void drawBox() {
//            Vector3 worldCoordinates = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
//            Vector3 vec_ = camera.unproject(worldCoordinates);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.rect(x1, y1, x2-x1, y2-y1);
            shapeRenderer.end();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        drawTile(button);
        removeTile(button);
        if (button == Input.Buttons.MIDDLE) {
            canCameraDrag = true;
        }

        if (button == Input.Buttons.LEFT && isObjectLayerSelected() && objectPanel.canCreateBox()) {
            Vector3 worldCoordinates= new Vector3(screenX, screenY, 0);
            Vector3 vec = camera.unproject(worldCoordinates);
            canDraw = true;
            x1 = vec.x;
            y1 = vec.y;
        }

        return false;
    }


    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.MIDDLE) {
            canCameraDrag = false;

        }

        if (button == Input.Buttons.LEFT && isObjectLayerSelected() && objectPanel.canCreateBox()) {
            System.out.println(x1 + " " + y1 + " " + x2 + " " + y2 );
            canDraw = false;
            objectPanel.createObject(x1,y1,x2-x1,y2-y1);
        }

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        float x = Gdx.input.getDeltaX();
        float y = Gdx.input.getDeltaY();
        if (canCameraDrag) {
            camera.translate(-x * (camera.zoom * 0.02f), y * (camera.zoom * 0.02f));
        }
        if (canDraw) {
            Vector3 worldCoordinates = new Vector3(screenX, screenY, 0);
            Vector3 vec = camera.unproject(worldCoordinates);
            x2 = vec.x;
            y2 = vec.y;
            System.out.println();
        }
        return true;
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
}
