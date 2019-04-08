package com.cosma.annihilation.Editor;


import box2dLight.Light;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
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

public class LightsListWindow extends VisWindow implements InputProcessor {

    public LightsAdvWindow lightsAdvWindow;
    public boolean isAdvWindowOpen = false;
    private MapEditor mapEditor;
    private MapLight selectedLight;
    private Light selectedBox2dLight;
    private VisTable layersTable;
    private ListView<MapLight> view;
    private OrthographicCamera camera;
    private final ObjectAdapter adapter;
    private boolean  canDragObject,isLeftButtonPressed, isRightButtonPressed;



    @Override
    protected void close() {
        super.close();
        mapEditor.lightsPanel.setLightListWindowOpen(false);
        mapEditor.getInputMultiplexer().removeProcessor(this);
        if (!adapter.getSelection().isEmpty()) {
            adapter.getSelectionManager().deselectAll();
        }
    }

    public LightsListWindow(final MapEditor mapEditor, OrthographicCamera camera) {
        super("Lights");
        this.mapEditor = mapEditor;
        this.camera = camera;
        TableUtils.setSpacingDefaults(this);
        columnDefaults(0).left();
        addCloseButton();
        adapter = new ObjectAdapter(mapEditor.layersPanel.getSelectedLayer(LightsMapLayer.class).getLights().getLights(), mapEditor,this);
        view = new ListView<MapLight>(adapter);
        view.setUpdatePolicy(ListView.UpdatePolicy.MANUAL);
        VisTable footerTable = new VisTable();
        footerTable.addSeparator();
        footerTable.add("");
        view.setFooter(footerTable);
        row();
        add(view.getMainTable()).fill().expand().center();
        row();

        setSize(Gdx.graphics.getWidth() * 0.15f, Gdx.graphics.getHeight() * 0.5f);
        setMovable(true);
        setResizable(true);

        adapter.setSelectionMode(AbstractListAdapter.SelectionMode.SINGLE);
        view.setItemClickListener(new ListView.ItemClickListener<MapLight>() {
            @Override
            public void clicked(MapLight item) {
            }
        });
        adapter.getSelectionManager().setListener(new ListSelectionAdapter<MapLight, VisTable>() {
            @Override
            public void selected(MapLight item, VisTable view) {
                item.setHighlighted(true);
                selectedLight = item;
                selectedBox2dLight = mapEditor.getMap().getLight(item.getName());
                if(selectedBox2dLight == null){
                }
            }
            @Override
            public void deselected(MapLight item, VisTable view) {
                item.setHighlighted(false);
                selectedLight = null;
                selectedBox2dLight = null;
            }
        });
    }

    public void rebuildView() {
        view.rebuildView();
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
        if (button == Input.Buttons.RIGHT) isRightButtonPressed = true;



        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) isLeftButtonPressed = false;
        if (button == Input.Buttons.RIGHT) isRightButtonPressed = false;
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector3 worldCoordinates = new Vector3(screenX, screenY, 0);
        Vector3 vec = camera.unproject(worldCoordinates);
        Vector3 deltaWorldCoordinates = new Vector3(screenX - Gdx.input.getDeltaX(), screenY - Gdx.input.getDeltaY(), 0);
        Vector3 deltaVec = camera.unproject(deltaWorldCoordinates);
        float amountX, amountY, startX, startY;

        if (canDragObject && isLeftButtonPressed && selectedBox2dLight != null) {
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
        Vector3 worldCoordinates = new Vector3(screenX, screenY, 0);
        Vector3 vec = camera.unproject(worldCoordinates);

        if (selectedLight != null) {
            float x = selectedLight.getX();
            float y = selectedLight.getY();

            if (Utilities.isFloatInRange(vec.x, x -1, x +1) && Utilities.isFloatInRange(vec.y, y -1, y +1)) {
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Hand);
                canDragObject = true;
            } else {
                canDragObject = false;
            }
            if (!canDragObject) {
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
            }
        }
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    private static class ObjectAdapter extends ArrayAdapter<MapLight, VisTable> {
        private final Drawable bg = VisUI.getSkin().getDrawable("window-bg");
        private final Drawable selection = VisUI.getSkin().getDrawable("list-selection");
        private MapEditor mapEditor;
        private LightsListWindow window;

        private ObjectAdapter(Array<MapLight> array, MapEditor mapEditor, LightsListWindow lightsListWindow) {
            super(array);
            setSelectionMode(SelectionMode.SINGLE);
            this.mapEditor = mapEditor;
            this.window = lightsListWindow;
        }

        @Override
        protected void selectView(VisTable view) {
            view.setBackground(selection);
        }

        @Override
        protected void updateView(VisTable view, MapLight item) {
            super.updateView(view, item);
        }

        @Override
        protected void deselectView(VisTable view) {
            view.setBackground(bg);
        }

        @Override
        protected VisTable createView(final MapLight item) {
            VisLabel label = new VisLabel(item.getName());
            final VisImageButton lightSetting = new VisImageButton(new TextureRegionDrawable(Annihilation.getAssets().get(GfxAssetDescriptors.editor_icons).findRegion("settings_")));
            lightSetting.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if(!window.getStage().getActors().contains(window.lightsAdvWindow,true)){
                        window.isAdvWindowOpen = true;
                        window.lightsAdvWindow = new LightsAdvWindow(window.selectedLight,window.selectedBox2dLight,window);
                        window.getStage().addActor(window.lightsAdvWindow);
                    }
                }
            });
            VisTextButton delete = new VisTextButton("x");
            delete.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if(!window.isAdvWindowOpen){
                        mapEditor.layersPanel.getSelectedLayer(LightsMapLayer.class).getLights().remove(item.getName());
                        mapEditor.getMap().getLight(item.getName()).remove(true);
                        window.selectedBox2dLight = null;
                        window.selectedLight = null;
                        view.rebuildView();
                    }
                }
            });
            VisTable table = new VisTable();
            table.center();
            table.add(label).fill().expandX();
            table.add(lightSetting).size(29);
            table.add(delete).size(29);
            return table;
        }
    }
}
