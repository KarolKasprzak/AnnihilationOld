package com.cosma.annihilation.Editor;


import box2dLight.Light;
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
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.cosma.annihilation.Annihilation;
import com.cosma.annihilation.Editor.CosmaMap.CosmaEditorLights.MapLight;
import com.cosma.annihilation.Editor.CosmaMap.LightsMapLayer;
import com.cosma.annihilation.Screens.MapEditor;
import com.cosma.annihilation.Utils.GfxAssetDescriptors;
import com.cosma.annihilation.Utils.Utilities;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.util.adapter.AbstractListAdapter;
import com.kotcrab.vis.ui.util.adapter.ArrayAdapter;
import com.kotcrab.vis.ui.util.adapter.ListSelectionAdapter;
import com.kotcrab.vis.ui.widget.*;
import com.kotcrab.vis.ui.widget.color.ColorPicker;
import com.kotcrab.vis.ui.widget.color.ColorPickerAdapter;

public class LightsAdvWindow extends VisWindow{

    private MapLight selectedLight;
    private Light selectedBox2dLight;
    private VisTable layersTable;
    private LightsListWindow lightsListWindow;
    private Color selectedColor;
    private final Drawable white = VisUI.getSkin().getDrawable("white");
    private ColorPicker picker;

    public LightsAdvWindow(final MapLight selectedLight, final Light selectedBox2dLight, LightsListWindow lightsListWindow) {
        super("Adv. light set.");
        this.lightsListWindow = lightsListWindow;


        final Image image = new Image(white);
        image.setColor(selectedLight.getColor());
        picker = new ColorPicker("color picker", new ColorPickerAdapter() {
            @Override
            public void finished(Color newColor) {
                selectedColor = newColor;
                image.setColor(newColor);
            }
        });



        picker.setColor(selectedLight.getColor());
        image.setColor(selectedLight.getColor());
        selectedColor = selectedLight.getColor();


        addCloseButton();


        VisTextButton saveButton = new VisTextButton("save");
        VisTextButton cancelButton = new VisTextButton("cancel");


        add(image).top().size(25).center().top().expandX().expandY();
        row();
        add(saveButton);
        add(cancelButton);

        image.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                getStage().addActor(picker.fadeIn());
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        saveButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
            selectedLight.setColor(selectedColor);
            selectedBox2dLight.setColor(selectedColor);
            }
        });
        cancelButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
             close();
            }
        });

        pack();
        setMovable(true);
        setResizable(false);
        setPosition(200,200);
    }

    @Override
    protected void close() {
        super.close();
        this.remove();
        lightsListWindow.isAdvWindowOpen =false;
//        mapEditor.lightsPanel.setLightListWindowOpen(false);
//        mapEditor.getInputMultiplexer().removeProcessor(this);
//        if (!adapter.getSelection().isEmpty()) {
//            adapter.getSelectionManager().deselectAll();
//        }
    }
}
