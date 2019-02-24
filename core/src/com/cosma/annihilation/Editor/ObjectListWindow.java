package com.cosma.annihilation.Editor;



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

public class ObjectListWindow extends VisWindow {

    private MapEditor mapEditor;
    private VisTable layersTable;

    public ObjectListWindow(final MapEditor mapEditor) {
        super("Map layers");
        this.mapEditor = mapEditor;
        TableUtils.setSpacingDefaults(this);
        columnDefaults(0).left();


        final ObjectAdapter adapter = new ObjectAdapter(mapEditor.layersPanel.getSelectedLayer(ObjectMapLayer.class).getObjects().getObjects(), mapEditor);
        final ListView<MapObject> view = new ListView<MapObject>(adapter);
        view.setUpdatePolicy(ListView.UpdatePolicy.ON_DRAW);

        VisTable footerTable = new VisTable();
        footerTable.addSeparator();
        footerTable.add("");
        view.setFooter(footerTable);

        row();
        add(view.getMainTable()).fill().expandY().center();
        row();

        pack();
        setSize(getWidth()*2, getHeight() +500);
        setMovable(false);
        setResizable(false);
//        setResizable(true);
//        setPosition(1900, Gdx.graphics.getHeight() / 2);



//        addLayerButton.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//                if (mapEditor.getMap() != null) {
//                    mapEditor.getMap().createTileMapLayer();
//                    view.rebuildView();
//                }
//            }
//        });

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



    private static class ObjectAdapter extends ArrayAdapter<MapObject, VisTable> {
        private final Drawable bg = VisUI.getSkin().getDrawable("window-bg");
        private final Drawable selection = VisUI.getSkin().getDrawable("list-selection");
        private MapEditor mapEditor;

        public ObjectAdapter(Array<MapObject> array, MapEditor mapEditor) {
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
