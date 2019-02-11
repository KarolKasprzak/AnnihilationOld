package com.cosma.annihilation.Editor;


import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.Node;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.cosma.annihilation.Screens.MapEditor;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.widget.*;

public class RightPanel extends VisWindow {

    private Node tileLayersNode;
    private VisTree tree;
    private MapEditor mapEditor;
    private VisTable layersTable;

    public RightPanel(MapEditor mapEditor) {
        super("");
        this.mapEditor = mapEditor;
        TableUtils.setSpacingDefaults(this);
        columnDefaults(0).left();

        VisTextButton addLayerButton = new VisTextButton("Add");
        VisTextButton removeLayerButton = new VisTextButton("Remove");

        VisTable buttonTable = new VisTable(true);
        buttonTable.add(addLayerButton);
        buttonTable.add(removeLayerButton);

        layersTable = new VisTable(true);
        layersTable.addSeparator();

        row();
        add(layersTable).fill();
        row();
        add(buttonTable).fill().padBottom(3).expand();


        setSize(150, 380);
        setPosition(774, 303);


        addLayerButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
              addLayer();
            }
        });
    }

    public void addLayer() {
            final VisCheckBox checkBox = new VisCheckBox("Layer",true);
            final MapLayer mapLayer = mapEditor.getMap().createMapLayer("layer");

            layersTable.add(checkBox);
            layersTable.row();

            checkBox.addListener(new ChangeListener() {
                @Override
                public void changed (ChangeEvent event, Actor actor) {
                  if(!checkBox.isChecked()){
                      for(MapLayer mapLayer_: mapEditor.getMap().getMapLayers())
                      {
                          if(mapLayer_ == mapLayer){
                              mapLayer_.setVisible(false);
                          }
                      }
                  }

                }
            });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

    }


    private void addVisWidgets() {
        tree = new VisTree();
        tileLayersNode = new Node(new VisLabel("Tile layers"));
        Node item2 = new Node(new VisLabel("Image layers"));
        Node item3 = new Node(new VisLabel("Object layers"));
        Node item4 = new Node(new VisLabel("Light layers"));

        tileLayersNode.setExpanded(true);

        tree.add(tileLayersNode);
        tree.add(item2);
        tree.add(item3);
        tree.add(item4);
        add(tree).fill();
    }
}
