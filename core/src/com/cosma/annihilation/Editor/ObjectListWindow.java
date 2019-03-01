package com.cosma.annihilation.Editor;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.cosma.annihilation.Editor.CosmaMap.CosmaEditorObject.MapObject;
import com.cosma.annihilation.Editor.CosmaMap.CosmaEditorObject.RectangleObject;
import com.cosma.annihilation.Editor.CosmaMap.ObjectMapLayer;
import com.cosma.annihilation.Screens.MapEditor;
import com.cosma.annihilation.Utils.Utilities;
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
    private OrthographicCamera camera;
    private RectangleObject selectedObject;
    private final ObjectAdapter adapter;
    private boolean canDragRight,canDragLeft, canDragUp, canDragDown, canDragObject, canRotateObject;
    float startX, startY, lastX, lastY;

    @Override
    protected void close() {
        super.close();
        mapEditor.objectPanel.setObjectListWindowOpen(false);
        mapEditor.getInputMultiplexer().removeProcessor(this);
        if (!adapter.getSelection().isEmpty()) {
            adapter.getSelectionManager().deselectAll();
        }
    }

    public ObjectListWindow(final MapEditor mapEditor, OrthographicCamera camera) {
        super("Map layers");
        this.mapEditor = mapEditor;
        this.camera = camera;
        TableUtils.setSpacingDefaults(this);
        columnDefaults(0).left();
        addCloseButton();
        adapter = new ObjectAdapter(mapEditor.layersPanel.getSelectedLayer(ObjectMapLayer.class).getObjects().getObjects(), mapEditor);
        view = new ListView<MapObject>(adapter);
        view.setUpdatePolicy(ListView.UpdatePolicy.MANUAL);

        VisTable footerTable = new VisTable();
        footerTable.addSeparator();
        footerTable.add("");
        view.setFooter(footerTable);

//        addListener(new InputListener(){
//            @Override
//            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//                System.out.println("sdafasasf");
//                return super.touchDown(event, x, y, pointer, button);
//            }
//        });

        row();
        add(view.getMainTable()).fill().expand().center();
        row();

        setSize(Gdx.graphics.getWidth() * 0.15f, Gdx.graphics.getHeight() * 0.5f);
        setMovable(true);
        setResizable(true);

        adapter.setSelectionMode(AbstractListAdapter.SelectionMode.SINGLE);
        view.setItemClickListener(new ListView.ItemClickListener<MapObject>() {
            @Override
            public void clicked(MapObject item) {
//
//                    System.out.println(adapter.getSelectionManager().getSelection().first().getName());
            }
        });
        adapter.getSelectionManager().setListener(new ListSelectionAdapter<MapObject, VisTable>() {
            @Override
            public void selected(MapObject item, VisTable view) {
                item.setHighlighted(true);
                selectedObject = (RectangleObject) item;
            }

            @Override
            public void deselected(MapObject item, VisTable view) {
                item.setHighlighted(false);

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
        startX = Utilities.getWorldCoordinates(screenX, screenY, camera).x;
        startY = Utilities.getWorldCoordinates(screenX, screenY, camera).y;
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector3 worldCoordinates = new Vector3(screenX, screenY, 0);
        Vector3 vec = camera.unproject(worldCoordinates);
        Vector3 deltaWorldCoordinates = new Vector3(screenX-Gdx.input.getDeltaX(), screenY-Gdx.input.getDeltaY(), 0);
        Vector3 deltaVec = camera.unproject(deltaWorldCoordinates);
        float amountX, amountY;
        if (canDragRight) {
            float width = vec.x - selectedObject.getX();
            if (width < 0.2f) width = 0.2f;
            selectedObject.setWidth(width);
        }
        if (canDragLeft) {
            amountX = vec.x - deltaVec.x;
            startX = deltaVec.x;
            if (selectedObject.getWidth() - amountX < 0.2f) amountX = 0;
            selectedObject.setX(selectedObject.getX() + amountX);
            selectedObject.setWidth(selectedObject.getWidth() - amountX);
        }
        if (canDragDown) {
            float height = vec.y-selectedObject.getY();
            if(height < 0.2f) height = 0.2f;
            selectedObject.setHeight(height);
        }
        if (canDragUp) {
            amountY = vec.y - deltaVec.y;
            startY = deltaVec.y;
            if (selectedObject.getHeight() - amountY < 0.2f) amountY = 0;
            selectedObject.setY(selectedObject.getY() + amountY);
            selectedObject.setHeight(selectedObject.getHeight() - amountY);
        }
        if (canDragObject) {
            amountX = vec.x - deltaVec.x;
            amountY = vec.y - deltaVec.y;
            selectedObject.setX(selectedObject.getX() + amountX);
            selectedObject.setY(selectedObject.getY() + amountY);
        }

        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        Vector3 worldCoordinates = new Vector3(screenX, screenY, 0);
        Vector3 vec = camera.unproject(worldCoordinates);

        if (selectedObject != null) {
            float x = selectedObject.getX();
            float y = selectedObject.getY();
            float width = selectedObject.getWidth();
            float height = selectedObject.getHeight();
            if (Utilities.roundFloat(x + width, 1) == Utilities.roundFloat(vec.x, 1) && Utilities.isFloatInRange(vec.y, y, y + height)) {
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.HorizontalResize);
                canDragRight = true;
            } else {
                canDragRight = false;
            }
            if (Utilities.roundFloat(x, 1) == Utilities.roundFloat(vec.x, 1) && Utilities.isFloatInRange(vec.y, y, y + height)) {
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.HorizontalResize);
                canDragLeft = true;
            } else {
                canDragLeft = false;
            }
            if (Utilities.roundFloat(y, 1) == Utilities.roundFloat(vec.y, 1) && Utilities.isFloatInRange(vec.x, x, x + width)) {
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.VerticalResize);
                canDragUp = true;
            } else {
                canDragUp = false;
            }
            if (Utilities.roundFloat(y+height, 1) == Utilities.roundFloat(vec.y, 1) && Utilities.isFloatInRange(vec.x, x, x + width)) {
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.VerticalResize);
                canDragDown = true;
            } else {
                canDragDown = false;
            }
            if (Utilities.isFloatInRange(vec.x,x+0.1f,x+width-0.1f)
                    && (Utilities.isFloatInRange(vec.y,y+0.1f,y+height-0.1f))) {
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Hand);
                canDragObject = true;
            } else {
                canDragObject = false;
            }
            if (!canDragLeft && !canDragRight && !canDragDown && !canDragUp && !canDragObject) {
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
            }
        }
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
