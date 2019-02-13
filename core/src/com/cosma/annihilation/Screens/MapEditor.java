package com.cosma.annihilation.Screens;

import com.badlogic.gdx.*;
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
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.cosma.annihilation.Annihilation;
import com.cosma.annihilation.Editor.*;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.Menu;
import com.kotcrab.vis.ui.widget.MenuBar;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.file.FileChooser;

public class MapEditor implements Screen, InputProcessor {

    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private Stage stage;
    private Viewport viewport, viewportUi;
    private OrthographicCamera camera,cameraUi;
    private World world;
    private final float TIME_STEP = 1 / 300f;
    private float accumulator = 0f;
    InputMultiplexer im;
    boolean runSim = false;
    int mouseX;
    int mouseY;

    private boolean canCameraDrag = false;
    float zoomLevel = 0.3f;

    private GameMap gameMap;
    private CreateMapWindow createMapWindow;

    private MapRender mapRender;
    private MenuBar menuBar;
    private FileChooser fileChooser;

    public MapEditor(Annihilation game){
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);

        cameraUi = new OrthographicCamera();
        cameraUi.update();
        viewportUi = new ScreenViewport();
        stage = new Stage(viewportUi);
        VisUI.load(VisUI.SkinScale.X1);

//        gameMap = new GameMap(10,10,10);
//        gameMap.getLayers().getLayer(gameMap.createMapLayerAndReturnName()).setColor(Color.OLIVE);
//        gameMap.getLayers().getLayer(gameMap.createMapLayerAndReturnName()).setColor(Color.BLUE);
//        gameMap.getLayers().getLayer(gameMap.createMapLayerAndReturnName()).setVisible(false);
//        mapRender = new MapRender(shapeRenderer,gameMap);


        createMapWindow = new CreateMapWindow(this);

//        gameMap = new GameMap(50,50,32);
//        gameMap.addMapLayer("layer1");

//        Json json = new Json();
//        System.out.println(json.prettyPrint(gameMap));
//        mapRender = new MapRender(shapeRenderer,gameMap);


        final Table root = new Table();
        root.setFillParent(true);
        root.setDebug(false);
        stage.addActor(root);


        camera = new OrthographicCamera();
        viewport = new ExtendViewport(16/1.3f, 9/1.3f,camera);
        camera.update();
        camera.zoom = 5;
        viewport.apply(true);
        world = new World(new Vector2(0,9),false);

        im = new InputMultiplexer();
        im.addProcessor(stage);
        im.addProcessor(this);
        batch = new SpriteBatch();

        menuBar = new MenuBar();
        root.add(menuBar.getTable()).expandX().fillX().row();
        root.add().expand().fill();

        Menu fileMenu = new Menu("File");

        fileMenu.addItem(new MenuItem("New gameMap", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                stage.addActor(createMapWindow);
            }
        }));
        fileMenu.addItem(new MenuItem("Save").setShortcut("ctrl + s"));
        fileMenu.addItem(new MenuItem("Save as"));
        fileMenu.addItem(new MenuItem("GameMap options"));
        fileMenu.addItem(new MenuItem("Exit"));

        menuBar.addMenu(fileMenu);

//        setupGui();

    }

    public void createNewMap(int x,int y,int scale){
        gameMap = new GameMap(x,y,scale);
        mapRender = new MapRender(shapeRenderer,gameMap);
        stage.addActor(new RightPanel(this));
        setCameraOnMapCenter();

    }

    public void loadMap(){
        stage.addActor(new RightPanel(this));
    }

    public GameMap getMap(){
        return gameMap;
    }

    public void setCameraOnMapCenter(){
        camera.position.set(getMap().getHeight()/2,getMap().getWidth()/2,0);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(im);
    }

    @Override
    public void render(float delta) {
//        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        shapeRenderer.begin();
        if(gameMap != null)
        mapRender.renderMap();
        shapeRenderer.end();

        stage.act(delta);
        if(runSim) {
            act(delta);
        }
        stage.draw();

        camera.update();
        cameraUi.update();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update( width,height,false);
        viewportUi.update( width,height);
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
        if (keycode == Input.Buttons.MIDDLE){
            System.out.println("sadsadsad");
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

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 worldCoordinates = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        Vector3 vec = camera.unproject(worldCoordinates);
        System.out.println(vec.x + " " + vec.y );

        if (button == Input.Buttons.MIDDLE){
            canCameraDrag = true;
            System.out.println(canCameraDrag );

        }


        return false;
    }



    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.MIDDLE){
            canCameraDrag = false;
            System.out.println(canCameraDrag );
        }

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        float x = Gdx.input.getDeltaX();
        float y = Gdx.input.getDeltaY();
        if(canCameraDrag){
            camera.translate(-x*(camera.zoom *0.02f),y*(camera.zoom *0.02f));
        }
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {

        if(amount == 1){
            camera.zoom += zoomLevel;
        }
        else if(amount == -1){
            camera.zoom -= zoomLevel;
        }
        return false;
    }
}
