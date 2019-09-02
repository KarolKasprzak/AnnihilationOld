package com.cosma.annihilation.Editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.FileTextureData;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.Node;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.cosma.annihilation.Annihilation;
import com.cosma.annihilation.Editor.CosmaMap.Sprite;
import com.cosma.annihilation.Editor.CosmaMap.SpriteMapLayer;
import com.cosma.annihilation.Screens.MapEditor;
import com.cosma.annihilation.Utils.Util;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTree;
import com.kotcrab.vis.ui.widget.VisWindow;


public class SpriteTreeWindow extends VisWindow implements InputProcessor {

    private MapEditor mapEditor;
    private boolean canAddSprite = false;
    private String texturePath;
    private String textureRegionName;
    private Sprite selectedSprite;

    private boolean canMove = false;
    private boolean isMoving = false;

    private boolean canRotate = false;
    private boolean isRotating = false;

    public SpriteTreeWindow(MapEditor mapEditor) {

        super("Sprite:");
        this.mapEditor = mapEditor;
        TableUtils.setSpacingDefaults(this);
        columnDefaults(0).left();
        final VisTree tree = new VisTree();
        Node treeRoot = new Node(new VisLabel("Sprite"));
        FileHandle file = Gdx.files.local("map/map_sprites");
        for (FileHandle atlasFile : file.list("atlas")) {
            Node node = new Node(new VisLabel(atlasFile.nameWithoutExtension()));
            node.setObject(atlasFile);
            treeRoot.add(node);
            TextureAtlas atlas = Annihilation.getAssets().get(atlasFile.path(),TextureAtlas.class);
            for(TextureAtlas.AtlasRegion textureRegion : atlas.getRegions()){
                VisLabel label = new VisLabel(textureRegion.name);
                label.setName(textureRegion.name);
                Node childrenNode = new Node(label);
                childrenNode.setObject(textureRegion);
                node.add(childrenNode);
            }
        }
        treeRoot.setExpanded(true);
        tree.add(treeRoot);

        ScrollPane scrollPane = new ScrollPane(tree);
        add(scrollPane).expand();
        setSize(150, 380);
        setPosition(0, 0);

        tree.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                if (!tree.getSelection().isEmpty()) {
                    if(tree.getSelection().first().getObject() instanceof TextureAtlas.AtlasRegion && mapEditor.isSpriteLayerSelected()){
                        canAddSprite = true;
                        textureRegionName = ((TextureAtlas.AtlasRegion) tree.getSelection().first().getObject()).name;
                        texturePath = ((FileTextureData) ((TextureAtlas.AtlasRegion) tree.getSelection().first().getObject()).getTexture().getTextureData()).getFileHandle().pathWithoutExtension()+".atlas";
                        Util.setCursorMove();
                    }
                }
            }
        });

    }

    private void findSprite(int x, int y){
        Vector3 worldCoordinates = new Vector3(x, y, 0);
        final Vector3 vec = mapEditor.getCamera().unproject(worldCoordinates);
        for(Sprite sprite: mapEditor.layersPanel.getSelectedLayer(SpriteMapLayer.class).getSpriteArray()){
            if(vec.x >= sprite.getX()- sprite.getWidth()/2 && vec.x <= sprite.getX()+ sprite.getWidth()/2 && vec.y >= sprite.getY()-sprite.getHeight()/2 && vec.y <= sprite.getY()+sprite.getHeight()/2){
                    Util.setCursorMove();
                    canMove = true;
                    canRotate = true;
                    selectedSprite = sprite;
            }else {
                Util.setCursorSystem();
                canMove = false;
                canRotate = false;
                selectedSprite = null;
            }
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.FORWARD_DEL && selectedSprite != null) {
            mapEditor.getMap().getLayers().getByType(SpriteMapLayer.class).first().getSpriteArray().removeValue(selectedSprite,true);
            selectedSprite = null;
            Util.setCursorSystem();
        }
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
    public boolean touchDown(final int screenX, final int screenY, int pointer, int button) {
        Vector3 worldCoordinates = new Vector3(screenX, screenY, 0);
        final Vector3 vec = mapEditor.getCamera().unproject(worldCoordinates);
        if (canAddSprite && button == Input.Buttons.LEFT && mapEditor.isSpriteLayerSelected()) {
            mapEditor.layersPanel.getSelectedLayer(SpriteMapLayer.class).createSprite(textureRegionName,texturePath,vec.x,vec.y,0);
            canAddSprite = false;
            Util.setCursorSystem();
        }
        if (canAddSprite && button == Input.Buttons.RIGHT) {
            canAddSprite = false;
            Util.setCursorSystem();
        }

        if (canMove && button == Input.Buttons.LEFT && selectedSprite != null) {
            isMoving = true;
        }

        if (canRotate && button == Input.Buttons.RIGHT && selectedSprite != null) {
            isRotating = true;
            Util.setCursorSize();
        }

      return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (canMove && button == Input.Buttons.LEFT && selectedSprite != null) {
            isMoving = false;
            selectedSprite = null;
            Util.setCursorSystem();
        }
        if (canRotate && button == Input.Buttons.RIGHT && selectedSprite != null) {
            isRotating = false;
            selectedSprite = null;
            Util.setCursorSystem();
        }

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector3 worldCoordinates = new Vector3(screenX, screenY, 0);
        final Vector3 vec = mapEditor.getCamera().unproject(worldCoordinates);
        float amountY;
        if(isMoving){
                selectedSprite.setSpritePosition(vec.x,vec.y);
        }
        if (isRotating){
            Vector3 deltaWorldCoordinates = new Vector3(screenX - Gdx.input.getDeltaX(), screenY - Gdx.input.getDeltaY(), 0);
            Vector3 deltaVec = mapEditor.getCamera().unproject(deltaWorldCoordinates);
            amountY = vec.y - deltaVec.y;
            selectedSprite.setSpriteAngle(selectedSprite.getAngle() + amountY * 10);
        }

        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        if(mapEditor.isSpriteLayerSelected()){
            findSprite(screenX,screenY);
        }

        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}

