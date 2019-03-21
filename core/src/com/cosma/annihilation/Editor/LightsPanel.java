package com.cosma.annihilation.Editor;

import box2dLight.ConeLight;
import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.cosma.annihilation.Editor.CosmaMap.LightsMapLayer;
import com.cosma.annihilation.Screens.MapEditor;
import com.kotcrab.vis.ui.FocusManager;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.kotcrab.vis.ui.widget.color.ColorPicker;
import com.kotcrab.vis.ui.widget.color.ColorPickerAdapter;
import com.kotcrab.vis.ui.widget.spinner.SimpleFloatSpinnerModel;
import com.kotcrab.vis.ui.widget.spinner.Spinner;

import java.util.HashMap;

public class LightsPanel extends VisWindow implements InputProcessor {

    private MapEditor mapEditor;
    private VisTextButton createLightButton,openLightsListWindowButton;
    private HashMap<String, Light> lightsMap;
    /** 0 = point , 1 = cone, 2 = directional**/
    private int selectedLightType = 0;
    private VisCheckBox setPointLight, setConeLight, setDirectionalLight;
    private boolean canCreateLight = false;
    private Spinner distanceSpinner, softDistanceSpinner;
    private OrthographicCamera camera;
    private LightsListWindow lightsListWindow;
    private boolean isLightListWindowOpen = false;
    private RayHandler rayHandler;
    private ColorPicker picker;
    private final Drawable white = VisUI.getSkin().getDrawable("white");
    private Color selectedColor;

    public void setLightListWindowOpen(boolean isLightListWindowOpen) {
        this.isLightListWindowOpen = isLightListWindowOpen;
    }

    public LightsPanel(final MapEditor mapEditor, RayHandler rayHandler) {
        super("Lights:");
        this.rayHandler = rayHandler;
        this.mapEditor = mapEditor;
        this.camera = mapEditor.getCamera();

        final Image image = new Image(white);
        lightsMap = new HashMap<>();

        picker = new ColorPicker("color picker", new ColorPickerAdapter() {
            @Override
            public void finished(Color newColor) {
                selectedColor = newColor;
                image.setColor(newColor);
            }
        });

        distanceSpinner = new Spinner("distance", new SimpleFloatSpinnerModel(5f, 1f, 25f, 0.5f, 2));
        distanceSpinner.getTextField().setFocusBorderEnabled(false);
        distanceSpinner.getTextField().addListener(new FocusListener() {
            @Override
            public void scrollFocusChanged(FocusEvent event, Actor actor, boolean focused) {
                super.scrollFocusChanged(event, actor, focused);
                if(focused == true){
                    getStage().setScrollFocus(null);
                }
            }
        });

        TableUtils.setSpacingDefaults(this);
        columnDefaults(0).left();

        createLightButton = new VisTextButton("create light");
        setPointLight = new VisCheckBox("point", true);
        setPointLight.setFocusBorderEnabled(false);
        setDirectionalLight = new VisCheckBox("sun");
        setDirectionalLight.setFocusBorderEnabled(false);
        setConeLight = new VisCheckBox("cone");
        openLightsListWindowButton = new VisTextButton("lights list");

        setPointLight.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                selectedLightType = 0;
                setDirectionalLight.setChecked(false);
                setConeLight.setChecked(false);
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        setDirectionalLight.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                selectedLightType = 2;
                setPointLight.setChecked(false);
                setConeLight.setChecked(false);
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        setConeLight.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                selectedLightType = 1;
                setDirectionalLight.setChecked(false);
                setPointLight.setChecked(false);
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        createLightButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                canCreateLight = true;
            }
        });
        openLightsListWindowButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!getStage().getActors().contains(lightsListWindow, true) && !mapEditor.getMap().getLayers().getByType(LightsMapLayer.class).isEmpty() && mapEditor.isLightsLayerSelected()) {
                    lightsListWindow = new LightsListWindow(mapEditor, camera);
                    getStage().addActor(lightsListWindow);
                    mapEditor.getInputMultiplexer().addProcessor(0, lightsListWindow);
                    setLightListWindowOpen(true);
                }
            }
        });

        image.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                getStage().addActor(picker.fadeIn());
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        picker.setColor(Color.WHITE);
        image.setColor(Color.WHITE);
        selectedColor = Color.WHITE;

        add(setDirectionalLight).left().top();
        row();
        add(setPointLight).left().top();
        row();
        add(setConeLight).left().top();
        row();
        add(distanceSpinner);
        row();
        add(createLightButton).center().top();
        add(openLightsListWindowButton).center().top();
        add(image).top().size(25).center().top().expandX().expandY();
        setPanelButtonsDisable(true);
        pack();
        setSize(getWidth(), getHeight());
        setPosition(1900, 200);
    }

    public void setPanelButtonsDisable(Boolean status) {
        createLightButton.setDisabled(status);
        setConeLight.setDisabled(status);
        setDirectionalLight.setDisabled(status);
        setPointLight.setDisabled(status);
        openLightsListWindowButton.setDisabled(status);
    }

    public HashMap<String, Light> getLightsHashMap() {
        return lightsMap;
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

        if (button == Input.Buttons.LEFT && canCreateLight && mapEditor.isLightsLayerSelected()) {
            Vector3 worldCoordinates = new Vector3(screenX, screenY, 0);
            Vector3 vec = camera.unproject(worldCoordinates);
            if (selectedLightType == 0) {
                mapEditor.getMap().getLayers().getByType(LightsMapLayer.class).first().createPointLight(vec.x, vec.y, selectedColor, 25, getMaxLightDistance());
                PointLight light = new PointLight(rayHandler, 25, selectedColor, getMaxLightDistance(), vec.x, vec.y);

                lightsMap.put(mapEditor.getMap().getLayers().getByType(LightsMapLayer.class).first().getLastLightName(), light);
            }
            if (selectedLightType == 1) {
                mapEditor.getMap().getLayers().getByType(LightsMapLayer.class).first().createConeLight(vec.x, vec.y, selectedColor, 25, getMaxLightDistance(),270,45);
                ConeLight light = new ConeLight(rayHandler, 25, selectedColor, getMaxLightDistance(), vec.x, vec.y,270,45);
                
                lightsMap.put(mapEditor.getMap().getLayers().getByType(LightsMapLayer.class).first().getLastLightName(), light);
            }
            canCreateLight = false;
            if (isLightListWindowOpen) {
                lightsListWindow.rebuildView();
            }
        }
        return false;
    }

    private float getMaxLightDistance(){
        return ((SimpleFloatSpinnerModel) distanceSpinner.getModel()).getValue();
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
