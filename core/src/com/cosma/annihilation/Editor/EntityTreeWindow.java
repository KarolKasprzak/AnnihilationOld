package com.cosma.annihilation.Editor;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Json;
import com.cosma.annihilation.Components.BodyComponent;
import com.cosma.annihilation.Screens.MapEditor;
import com.cosma.annihilation.Utils.Serialization.EntitySerializer;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTree;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.Node;

import java.util.HashMap;

public class EntityTreeWindow extends VisWindow implements InputProcessor {

    private HashMap<String,FileHandle> jsonList;
    private World world;
    private MapEditor mapEditor;
    private boolean canAddEntity = false;
    private String selectedEntity;

    public EntityTreeWindow(World world, MapEditor mapEditor)  {
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
        setPosition(0, 303);

        tree.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!tree.getSelection().isEmpty()) {
                    VisLabel label = ((VisLabel) tree.getSelection().first().getActor());
                    if (label.getName() != null) {
                        selectedEntity = label.getName();
                        System.out.println(selectedEntity);
                        canAddEntity = true;
                        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Crosshair);
                    }
                }
            }
        });
    }

    private void createEntity(String key,float x, float y){
        Json json = new Json();
        json.setSerializer(Entity.class,new EntitySerializer(world));
        Entity entity = json.fromJson(Entity.class, jsonList.get(key));
        entity.getComponent(BodyComponent.class).body.setTransform(new Vector2(x,y),0);
        mapEditor.getMap().addEntity(entity);
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
        if(canAddEntity && button == Input.Buttons.LEFT){
            Vector3 worldCoordinates = new Vector3(screenX, screenY, 0);
            Vector3 vec = mapEditor.getCamera().unproject(worldCoordinates);
            createEntity(selectedEntity,vec.x,vec.y);
            canAddEntity = false;
            Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
        }
        if(canAddEntity && button == Input.Buttons.RIGHT){
            canAddEntity = false;
            Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
        }
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
        Vector3 worldCoordinates = new Vector3(screenX, screenY, 0);
        Vector3 vec = mapEditor.getCamera().unproject(worldCoordinates);

//        if (selectedLight != null) {
//            float x = selectedLight.getX();
//            float y = selectedLight.getY();
//
//            if (Utilities.isFloatInRange(vec.x, x -1, x +1) && Utilities.isFloatInRange(vec.y, y -1, y +1)) {
//                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Hand);
//                canDragObject = true;
//            } else {
//                canDragObject = false;
//            }
//            if (!canDragObject) {
//                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
//            }
//        }
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
