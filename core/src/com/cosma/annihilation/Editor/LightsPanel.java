package com.cosma.annihilation.Editor;

import box2dLight.ConeLight;
import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.cosma.annihilation.Editor.CosmaMap.CosmaEditorLights.MapLight;
import com.cosma.annihilation.Editor.CosmaMap.LightsMapLayer;
import com.cosma.annihilation.Screens.MapEditor;
import com.cosma.annihilation.Utils.Utilities;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.util.dialog.ConfirmDialogListener;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.kotcrab.vis.ui.widget.color.ColorPicker;
import com.kotcrab.vis.ui.widget.color.ColorPickerAdapter;
import com.kotcrab.vis.ui.widget.spinner.SimpleFloatSpinnerModel;
import com.kotcrab.vis.ui.widget.spinner.Spinner;


public class LightsPanel extends VisWindow implements InputProcessor {

    private MapEditor mapEditor;
    private VisTextButton createLightButton, openLightsListWindowButton;
    /**
     * 0 = point , 1 = cone, 2 = directional
     **/
    private int selectedLightType = 0;
    private VisCheckBox setPointLight, setConeLight, setDirectionalLight;
    private Spinner distanceSpinner, softDistanceSpinner;
    private OrthographicCamera camera;
    private RayHandler rayHandler;
    private ColorPicker picker;
    private final Drawable white = VisUI.getSkin().getDrawable("white");
    private Color selectedColor;
    private MapLight selectedLight;
    private Light selectedBox2dLight;
    private boolean canDragObject, isLeftButtonPressed, isRightButtonPressed, canCreateLight = false, isLightListWindowOpen = false;

    void setLightListWindowOpen(boolean isLightListWindowOpen) {
        this.isLightListWindowOpen = isLightListWindowOpen;
    }

    public LightsPanel(final MapEditor mapEditor, RayHandler rayHandler) {
        super("Lights:");
        this.rayHandler = rayHandler;
        this.mapEditor = mapEditor;
        this.camera = mapEditor.getCamera();

        final Image image = new Image(white);
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
                if (focused) {
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

    void setPanelButtonsDisable(Boolean status) {
        createLightButton.setDisabled(status);
        setConeLight.setDisabled(status);
        setDirectionalLight.setDisabled(status);
        setPointLight.setDisabled(status);
        openLightsListWindowButton.setDisabled(status);
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

        if (button == Input.Buttons.LEFT) isLeftButtonPressed = true;
        if (button == Input.Buttons.RIGHT && selectedLight != null) {
            final int delete = 1;
            final int options = 2;
            final int filter = 3;
            final int cancel = 4;
            Dialogs.showConfirmDialog(getStage(), "Light:", "what do you want?",
                    new String[]{"delete", "options","filter","cancel"}, new Integer[]{delete, options, filter,cancel},
                    new ConfirmDialogListener<Integer>() {
                        @Override
                        public void result(Integer result) {
                            if (result == delete) {
                                mapEditor.layersPanel.getSelectedLayer(LightsMapLayer.class).getLights().remove(selectedLight.getName());
                                mapEditor.getMap().getLight(selectedLight.getName()).remove(true);
                                selectedBox2dLight = null;
                                selectedLight = null;
                            }

                            if (result == options){
                                LightsAdvWindow lightsAdvWindow = new LightsAdvWindow(selectedLight,selectedBox2dLight);
                                lightsAdvWindow.setPosition(Gdx.input.getX(),Gdx.input.getY());
                                getStage().addActor(lightsAdvWindow);
                            }

                            if (result == filter){
                                LightFilterWindow lightFilterWindow = new LightFilterWindow(selectedBox2dLight);
                                lightFilterWindow.setPosition(Gdx.input.getX(),Gdx.input.getY());
                                getStage().addActor(lightFilterWindow);
                            }


                        }
                    }).setPosition(Gdx.input.getX(),Gdx.input.getY());

        }

        if (button == Input.Buttons.LEFT && canCreateLight && mapEditor.isLightsLayerSelected()) {
            Vector3 worldCoordinates = new Vector3(screenX, screenY, 0);
            Vector3 vec = camera.unproject(worldCoordinates);
            if (selectedLightType == 0) {
                mapEditor.getMap().getLayers().getByType(LightsMapLayer.class).first().createPointLight(vec.x, vec.y, selectedColor, 25, getMaxLightDistance());
                PointLight light = new PointLight(rayHandler, 25, selectedColor, getMaxLightDistance(), vec.x, vec.y);
                mapEditor.getMap().putLight(mapEditor.getMap().getLayers().getByType(LightsMapLayer.class).first().getLastLightName(), light);
            }
            if (selectedLightType == 1) {
                mapEditor.getMap().getLayers().getByType(LightsMapLayer.class).first().createConeLight(vec.x, vec.y, selectedColor, 25, getMaxLightDistance(), 270, 45);
                ConeLight light = new ConeLight(rayHandler, 25, selectedColor, getMaxLightDistance(), vec.x, vec.y, 270, 45);
                mapEditor.getMap().putLight(mapEditor.getMap().getLayers().getByType(LightsMapLayer.class).first().getLastLightName(), light);
            }
            canCreateLight = false;

        }
        return false;
    }

    private float getMaxLightDistance() {
        return ((SimpleFloatSpinnerModel) distanceSpinner.getModel()).getValue();
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) isLeftButtonPressed = false;
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer){
        Vector3 worldCoordinates = new Vector3(screenX, screenY, 0);
        Vector3 vec = camera.unproject(worldCoordinates);
        Vector3 deltaWorldCoordinates = new Vector3(screenX - Gdx.input.getDeltaX(), screenY - Gdx.input.getDeltaY(), 0);
        Vector3 deltaVec = camera.unproject(deltaWorldCoordinates);
        float amountX, amountY;

        if (canDragObject && selectedBox2dLight != null && isLeftButtonPressed) {
            amountX = vec.x - deltaVec.x;
            amountY = vec.y - deltaVec.y;
            selectedLight.setX(selectedLight.getX() + amountX);
            selectedLight.setY(selectedLight.getY() + amountY);
            selectedBox2dLight.setPosition(selectedBox2dLight.getX()+amountX,selectedBox2dLight.getY()+amountY);
        }

        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        if (mapEditor.isLightsLayerSelected()) {
            Vector3 worldCoordinates = new Vector3(screenX, screenY, 0);
            Vector3 vec = camera.unproject(worldCoordinates);
            MapLight mapLightFound = null;
            boolean lightFound = false;
            for (MapLight mapLight : mapEditor.layersPanel.getSelectedLayer(LightsMapLayer.class).getLights()) {
                float x = mapLight.getX();
                float y = mapLight.getY();

                if (Utilities.isFloatInRange(vec.x, x - 1, x + 1) && Utilities.isFloatInRange(vec.y, y - 1, y + 1)) {
                    lightFound = true;
                    mapLightFound = mapLight;
                }

            }

            if(lightFound){
                selectedLight = mapLightFound;
                selectedBox2dLight = mapEditor.getMap().getLight(mapLightFound.getName());
                mapLightFound.setHighlighted(true);
                canDragObject = true;
            }else {
                if(selectedLight != null){
                    selectedLight.setHighlighted(false);}
                selectedLight = null;
                selectedBox2dLight = null;
                canDragObject = false;
            }
        }
        return false;
}

    @Override
    public boolean scrolled(int amount) {

        return false;
    }
}
