package com.cosma.annihilation.Editor;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Json;
import com.cosma.annihilation.Screens.MapEditor;
import com.cosma.annihilation.Utils.Serialization.EntitySerializer;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTree;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.Node;

import java.util.HashMap;

public class EntityTreeWindow extends VisWindow {

    private HashMap<String,FileHandle> jsonList;
    private World world;
    private MapEditor mapEditor;

    public EntityTreeWindow(World world, MapEditor mapEditor) {
        super("Entity:");
        this.world = world;
        this.mapEditor = mapEditor;
        TableUtils.setSpacingDefaults(this);
        columnDefaults(0).left();
        jsonList = new HashMap<>();

        final VisTree tree = new VisTree();
        Node treeRoot = new Node(new VisLabel("Entity"));

        FileHandle file = Gdx.files.local("entity");
        for(FileHandle rootDirectory: file.list()){
            if(rootDirectory.isDirectory()){
                Node node = new Node(new VisLabel(rootDirectory.nameWithoutExtension()));
                treeRoot.add(node);
                for(FileHandle childrenDirectory: rootDirectory.list(".json")){
                    VisLabel label = new VisLabel(childrenDirectory.nameWithoutExtension());
                    label.setName(childrenDirectory.nameWithoutExtension());
                    Node childrenNode = new Node(label);
                    jsonList.put(childrenDirectory.nameWithoutExtension(),childrenDirectory);
                    node.add(childrenNode);
                }
            }
        }
        treeRoot.setExpanded(true);

        tree.add(treeRoot);

        add(tree).expand().fill();

        setSize(150, 380);
        setPosition(774, 303);

        tree.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!tree.getSelection().isEmpty()) {
                    VisLabel label = ((VisLabel) tree.getSelection().first().getActor());
                    if (label.getName() != null) {
                        createEntity(label.getName());
                    }
                }
            }
        });
    }

    private void createEntity(String key){
        Json json = new Json();
        json.setSerializer(Entity.class,new EntitySerializer(world));
        Entity entity = json.fromJson(Entity.class, jsonList.get(key));
        mapEditor.getMap().addEntity(entity);
    }

}
