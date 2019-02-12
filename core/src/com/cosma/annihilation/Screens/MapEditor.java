package com.cosma.annihilation.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.cosma.annihilation.Annihilation;
import com.cosma.annihilation.Editor.*;
import com.cosma.annihilation.Utils.GfxAssetDescriptors;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.Menu;
import com.kotcrab.vis.ui.widget.MenuBar;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.file.FileChooser;

public class MapEditor implements Screen, InputProcessor {

    private Skin skin;
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
    List list;
    List objectList;
    int zoom = 5;
    private Vector3 touchPosition;
    boolean canPan = false;
    boolean canMove = false;
    boolean canDrag = false;
    TextButton buttonSave;
    TextButton buttonLoad;
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

        gameMap = new GameMap(10,10,10);
        gameMap.getLayers().getLayer(gameMap.createMapLayerAndReturnName()).setColor(Color.OLIVE);
        gameMap.getLayers().getLayer(gameMap.createMapLayerAndReturnName()).setColor(Color.BLUE);
        gameMap.getLayers().getLayer(gameMap.createMapLayerAndReturnName()).setVisible(false);
        mapRender = new MapRender(shapeRenderer,gameMap);


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
        stage.addActor(new RightPanel(this));



        camera = new OrthographicCamera();
        viewport = new ExtendViewport(16/1.3f, 9/1.3f,camera);
        camera.update();
        viewport.apply(true);
        world = new World(new Vector2(0,9),false);
        camera.zoom = zoom;
        skin = Annihilation.getAssets().get(GfxAssetDescriptors.skin);


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

    }
    public GameMap getMap(){
        return gameMap;
    }


    void setupGui (){

        Table table = new Table();
        stage.addActor(table);
        table.setFillParent(true);
        table.setDebug(true);

        //Fps

        //Buttons
        buttonSave = new TextButton("Save", skin, "default");
        buttonSave.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

            }
        });
        buttonLoad = new TextButton("Load", skin, "default");
        buttonLoad.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

            }
        });



//        final CheckBox checkBox1 = new CheckBox("Drag & Move", skin, "default");
//        checkBox1.addListener(new ChangeListener() {
//            @Override
//            public void changed (ChangeEvent event, Actor actor) {
//                Gdx.graphics.setContinuousRendering(checkBox1.isChecked());
//                if(checkBox1.isChecked()) {
//                    canDrag = true;
//                }else {
//                    canDrag = false;
//                }
//            }
//        });
        //----------------------------------------------------------------------------------
        list = new List(skin, "default");
        String[] myList = {"Select","player","wall","ladder"};
        list.setItems(myList);
        list.setAlignment(25);
        ScrollPane scrollPane = new ScrollPane(list, skin, "default");
        scrollPane.setColor(0,1,1,1);
        //-------------------------LayerList-------------------------------------

        List layerList = new List(skin, "default");
        String[] layerList1 = {"Layer1","Layer2","Layer3"};
        layerList.setItems(layerList1);
        layerList.setAlignment(20);

        //-----------------------------------------------------
//        ButtonGroup buttonGroup = new ButtonGroup(checkBox,checkBox1);
//        buttonGroup.setMaxCheckCount(1);
//        buttonGroup.setMinCheckCount(0);
//        buttonGroup.setUncheckLast(true);
        //-----------------------------------------------------
        objectList = new List(skin, "default");
        objectList.setItems(myList);

        Table tableRightPanel = new Table();

        tableRightPanel.add(layerList);
        Table tableLeftPanel = new Table();
        tableLeftPanel.add(scrollPane);

        table.add(tableLeftPanel).left().expandY().expandX();
        table.add(tableRightPanel).right().expandY();


//        table.add(buttonSave).height(30).width(80).left();
//        table.add(buttonLoad).height(30).width(80).left();
//        table.row();
//        table.add(objectList).left().expandX();
//        table.add(tableRightPanel).right().expandY().width(setScaledWidth(0.4f));
//        table.add();
//        table.add();
//        table.add().expandX();
//        table.row();





    }

    public static float setScaledWidth(float scale){
        return (Gdx.app.getGraphics().getWidth()/12.8f)*scale;
    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(im);
        touchPosition = new Vector3();
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
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
