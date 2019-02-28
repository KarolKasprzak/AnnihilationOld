package com.cosma.annihilation.Editor;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.cosma.annihilation.Editor.CosmaMap.CosmaEditorObject.MapObject;
import com.cosma.annihilation.Editor.CosmaMap.ObjectMapLayer;
import com.cosma.annihilation.Screens.MapEditor;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.util.adapter.AbstractListAdapter;
import com.kotcrab.vis.ui.util.adapter.ArrayAdapter;
import com.kotcrab.vis.ui.util.adapter.ListSelectionAdapter;
import com.kotcrab.vis.ui.widget.*;

public class ObjectListWindow extends VisWindow implements InputProcessor {

    private MapEditor mapEditor;
    private VisTable layersTable;
    private ListView<MapObject> view;

    public void rebuildView(){
        view.rebuildView();
    }

    @Override
    protected void close() {
        super.close();
        mapEditor.objectPanel.setObjectListWindowOpen(false);
        mapEditor.getInputMultiplexer().removeProcessor(this);
    }

    public ObjectListWindow(final MapEditor mapEditor) {
        super("Map layers");
        this.mapEditor = mapEditor;
        TableUtils.setSpacingDefaults(this);
        columnDefaults(0).left();

        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.HorizontalResize);

        addCloseButton();

        final ObjectAdapter adapter = new ObjectAdapter(mapEditor.layersPanel.getSelectedLayer(ObjectMapLayer.class).getObjects().getObjects(), mapEditor);
        view = new ListView<MapObject>(adapter);
        view.setUpdatePolicy(ListView.UpdatePolicy.MANUAL);

        VisTable footerTable = new VisTable();
        footerTable.addSeparator();
        footerTable.add("");
        view.setFooter(footerTable);

        row();
        add(view.getMainTable()).fill().expand().center();
        row();

        setSize(Gdx.graphics.getWidth()*0.1f, Gdx.graphics.getHeight()*0.5f);
        setMovable(true);
        setResizable(false);

        adapter.setSelectionMode(AbstractListAdapter.SelectionMode.SINGLE);
        view.setItemClickListener(new ListView.ItemClickListener<MapObject>() {
            @Override
            public void clicked(MapObject item) {
            }
        });
        adapter.getSelectionManager().setListener(new ListSelectionAdapter<MapObject, VisTable>() {
            @Override
            public void selected(MapObject item, VisTable view) {
                item.setHighlighted(true);

            }

            @Override
            public void deselected(MapObject item, VisTable view) {
                        item.setHighlighted(false);
            }
        });


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

    private static class ObjectAdapter extends ArrayAdapter<MapObject, VisTable> {
        private final Drawable bg = VisUI.getSkin().getDrawable("window-bg");
        private final Drawable selection = VisUI.getSkin().getDrawable("list-selection");
        private MapEditor mapEditor;

        private ObjectAdapter(Array<MapObject> array, MapEditor mapEditor) {
            super(array);
            setSelectionMode(SelectionMode.SINGLE);
            this.mapEditor = mapEditor;
        }

        @Override
        protected void selectView(VisTable view) {
            view.setBackground(selection);
        }

        @Override
        protected void updateView(VisTable view, MapObject item) {
            super.updateView(view, item);
        }

        @Override
        protected void deselectView(VisTable view) {
            view.setBackground(bg);
        }

        @Override
        protected VisTable createView(final MapObject item) {
//            final MapObject mapObject = mapEditor.layersPanel.getSelectedLayer(ObjectMapLayer.class).getObjects().getObject(item.getName());
            VisLabel label = new VisLabel(item.getName());
            VisTextButton delete = new VisTextButton("x");
            delete.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                           mapEditor.layersPanel.getSelectedLayer(ObjectMapLayer.class).getObjects().remove(item.getName());
                           view.rebuildView();
                }
            });
            VisTable table = new VisTable();
            table.center();
            table.add(label).fill().expandX();
            table.add(delete);
            return table;
        }
    }
}
