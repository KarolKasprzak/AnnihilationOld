package com.cosma.annihilation.Editor.CosmaMap;


import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.cosma.annihilation.Screens.MapEditor;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.widget.*;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPane;


public class ObjectPanel extends VisWindow {

    private MapEditor mapEditor;
    private TabbedPane tabbedPane;
    private VisTextButton addBoxButton;
    private VisTextButton addCircleButton;

    public boolean canCreateBox() {
        return canCreateBox;
    }

    public void setCanCreateBox(boolean canCreateBox) {
        this.canCreateBox = canCreateBox;
    }

    private boolean canCreateBox = false;
    private boolean createCircle = false;

    public ObjectPanel(final MapEditor mapEditor) {
        super("Object");
        this.mapEditor = mapEditor;

        TableUtils.setSpacingDefaults(this);
        columnDefaults(0).left();

        addBoxButton = new VisTextButton("Box");
        addCircleButton = new VisTextButton("Circle");

        add(addBoxButton).top();
        add(addCircleButton).top();
        add().expand().fill();

        addBoxButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                canCreateBox = true;
                createCircle = false;
            }
        });

        addCircleButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                canCreateBox = false;
                createCircle = true;
            }
        });

        setPanelButtonsDisable(true);
        pack();
        setSize(getWidth(), getHeight());
        setPosition(1900, 200);
    }


    public void createObject(float x, float y, float w, float h){
        if(canCreateBox){
            createBoxObject(x,y,w,h);
        }
    }

    public void setPanelButtonsDisable(Boolean status){
        addBoxButton.setDisabled(status);
        addCircleButton.setDisabled(status);
    }

    public void createBoxObject(float x, float y, float w, float h){
        if(mapEditor.isObjectLayerSelected()){
            mapEditor.layersPanel.getSelectedLayer(ObjectMapLayer.class).createBoxObject(x,y,w,h);
        }
    }

    public void createCircleObject(float x, float y, float w, float h){
//       //TODO
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

    }
}
