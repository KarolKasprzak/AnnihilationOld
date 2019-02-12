package com.cosma.annihilation.Editor;


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

        VisTable buttonTable = new VisTable(true);
        buttonTable.setDebug(true);

        buttonTable.add(addLayerButton).top().expandY();
        buttonTable.add(removeLayerButton).top();
        buttonTable.add(layerUp).top();
        buttonTable.add(layerDown).top();

        layersTable = new VisTable(true);
        layersTable.add(view.getMainTable());
        layersTable.row();
        layersTable.addSeparator();

        row();
        add(layersTable).fill();
        row();
        add(buttonTable).fill().padBottom(3).expand();

        pack();
        setSize(getWidth(), getHeight() + 200);
        setPosition(774, 303);

//
//        layerVisibleCheckBox.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//                if(selectedLayer != null && !mapEditor.getMap().getLayers().isEmpty()) {
//                if(layerVisibleCheckBox.isChecked()){
//                        selectedLayer.setVisible(false);
//                    }else
//                        selectedLayer.setVisible(true);
//                }
//            }
//        });

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

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

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
            VisCheckBox box = new VisCheckBox("",mapLayer.isLayerVisible());

            box.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if (mapLayer.isLayerVisible()) {
                       mapLayer.setVisible(false);
                    }else
                        mapLayer.setVisible(true);
                }
            });

            VisTable table = new VisTable();
            table.left();
            table.add(label);
            table.add(box);
            return table;
        }
    }
}
