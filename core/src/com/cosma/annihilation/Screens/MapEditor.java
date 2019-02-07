package com.cosma.annihilation.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.cosma.annihilation.Annihilation;
import com.cosma.annihilation.Editor.Grid;
import com.cosma.annihilation.Editor.Map;
import com.cosma.annihilation.Editor.MapLayer;
import com.cosma.annihilation.Editor.MapRender;
import com.cosma.annihilation.Gui.BackgroundColor;
import com.cosma.annihilation.Utils.GfxAssetDescriptors;
import com.cosma.annihilation.Utils.Utilities;

public class MapEditor implements Screen, InputProcessor {
    private Skin skin;
    private SpriteBatch batch, batchui;
    private ShapeRenderer shapeRenderer;
    private Stage stage, stageui;
    private Viewport viewport, viewportUi;
    private OrthographicCamera camera,cameraui;
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
    private Map map;
    private MapRender mapRender;

    public MapEditor(Annihilation game){
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        stage = new Stage();
        stageui = new Stage();
        cameraui = new OrthographicCamera();
        cameraui.update();
        viewportUi = new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),cameraui);
        stageui.setViewport(viewportUi);
        viewportUi.apply(true);


        map = new Map(50,50,32);
        map.addMapLayer();
        mapRender = new MapRender(shapeRenderer,map);
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(16/1.3f, 9/1.3f,camera);
        camera.update();
        System.out.println();
        viewport = new ExtendViewport(16/1.3f, 9/1.3f,camera);
        viewport.apply(true);
        stage.setViewport(viewport);
        world = new World(new Vector2(0,9),false);
        camera.zoom = zoom;

        skin = Annihilation.getAssets().get(GfxAssetDescriptors.skin);


        im = new InputMultiplexer();
        im.addProcessor(stageui);
        im.addProcessor(stage);
        im.addProcessor(this);
        batch = new SpriteBatch();
        batchui = new SpriteBatch();

        setupGui();

    }

    void setupGui (){
        Table table = new Table();
        table.top();
        table.setFillParent(true);
        table.setDebug(true);
        stageui.addActor(table);
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
        mapRender.renderMap();
        shapeRenderer.end();

        stageui.act(delta);
        stage.act(delta);
        if(runSim) {
            act(delta);
        }
        stage.draw();
        stageui.draw();

        camera.update();
        cameraui.update();
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
