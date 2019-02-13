package com.cosma.annihilation.Editor;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.cosma.annihilation.Screens.MapEditor;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.util.adapter.AbstractListAdapter;
import com.kotcrab.vis.ui.util.adapter.ArrayAdapter;
import com.kotcrab.vis.ui.util.adapter.ListSelectionAdapter;
import com.kotcrab.vis.ui.widget.*;
import javafx.scene.control.CheckBox;

public class RightPanel extends VisWindow {

    private MapEditor mapEditor;
    private VisTable layersTable;
    private MapLayer selectedLayer;

    public RightPanel(final MapEditor mapEditor) {
        super("");
        this.mapEditor = mapEditor;
        TableUtils.setSpacingDefaults(this);
        columnDefaults(0).left();

        VisTextButton addLayerButton = new VisTextButton("Add");
        VisTextButton removeLayerButton = new VisTextButton("Remove");
        VisTextButton layerUp = new VisTextButton("^");
        VisTextButton layerDown = new VisTextButton("v");

        final MapLayerAdapter adapter = new MapLayerAdapter(mapEditor.getMap().getLayers().getLayers(), mapEditor);
        final ListView<MapLayer> view = new ListView<MapLayer>(adapter);
        view.setUpdatePolicy(ListView.UpdatePolicy.ON_DRAW);

        VisTable headerTable = new VisTable();
        headerTable.add("Map layers:").expandX().left();
        headerTable.row();
        headerTable.addSeparator();

        VisTable footerTable = new VisTable();
        footerTable.addSeparator();
        footerTable.add("");
        view.setFooter(footerTable);
        view.setHeader(headerTable);

        VisTable buttonTable = new VisTable(true);
        buttonTable.add(addLayerButton).top();
        buttonTable.add(removeLayerButton).top();
        buttonTable.add(layerUp).top();
        buttonTable.add(layerDown).top();

        row();
        add(view.getMainTable()).fill().expandY().center();
        row();
        add(buttonTable).padBottom(3);

        pack();
        setSize(getWidth(), getHeight() * 5f);
        setResizable(true);
        setPosition(1200, Gdx.graphics.getHeight() / 2);



        addLayerButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (mapEditor.getMap() != null) {
                    mapEditor.getMap().createMapLayer();
                    view.rebuildView();
                }
            }
        });

        removeLayerButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (selectedLayer != null && !mapEditor.getMap().getLayers().isEmpty()) {
                    mapEditor.getMap().getLayers().remove(selectedLayer.getName());
                    view.rebuildView();
                }
            }
        });

        layerUp.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (selectedLayer != null && !mapEditor.getMap().getLayers().isEmpty()) {
                    int index = mapEditor.getMap().getLayers().getIndex(selectedLayer.getName());
                    if (index != 0) {
                        mapEditor.getMap().getLayers().swapLayerOrder(index, index - 1);
                        view.rebuildView();
                    }
                }
            }
        });

        layerDown.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (selectedLayer != null && !mapEditor.getMap().getLayers().isEmpty()) {
                    int index = mapEditor.getMap().getLayers().getIndex(selectedLayer.getName());
                    if (index != mapEditor.getMap().getLayers().getCount() - 1) {
                        mapEditor.getMap().getLayers().swapLayerOrder(index, index + 1);
                        view.rebuildView();
                    }
                }
            }
        });

        adapter.setSelectionMode(AbstractListAdapter.SelectionMode.SINGLE);
        view.setItemClickListener(new ListView.ItemClickListener<MapLayer>() {
            @Override
            public void clicked(MapLayer item) {
            }
        });
        adapter.getSelectionManager().setListener(new ListSelectionAdapter<MapLayer, VisTable>() {
            @Override
            public void selected(MapLayer item, VisTable view) {
                selectedLayer = mapEditor.getMap().getLayers().getLayer(item.getName());
            }

            @Override
            public void deselected(MapLayer item, VisTable view) {
            }
        });
    }



    private static class MapLayerAdapter extends ArrayAdapter<MapLayer, VisTable> {
        private final Drawable bg = VisUI.getSkin().getDrawable("window-bg");
        private final Drawable selection = VisUI.getSkin().getDrawable("list-selection");
        private MapEditor mapEditor;

        public MapLayerAdapter(Array<MapLayer> array, MapEditor mapEditor) {
            super(array);
            setSelectionMode(SelectionMode.SINGLE);
            this.mapEditor = mapEditor;
        }

        @Override
        protected void selectView(VisTable view) {
            view.setBackground(selection);
        }

        @Override
        protected void updateView(VisTable view, MapLayer item) {
            super.updateView(view, item);
        }

        @Override
        protected void deselectView(VisTable view) {
            view.setBackground(bg);
        }

        @Override
        protected VisTable createView(final MapLayer item) {
            final MapLayer mapLayer = mapEditor.getMap().getLayers().getLayer(item.getName());
            VisLabel label = new VisLabel(item.getName());
            VisCheckBox box = new VisCheckBox("", mapLayer.isLayerVisible());

            box.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if (mapLayer.isLayerVisible()) {
                        mapLayer.setVisible(false);
                    } else
                        mapLayer.setVisible(true);
                }
            });

            VisTable table = new VisTable();
            table.center();
            table.add(label).fill().expandX();
            table.add(box);
            return table;
        }
    }
}
