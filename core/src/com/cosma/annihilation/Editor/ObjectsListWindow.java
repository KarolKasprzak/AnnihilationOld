package com.cosma.annihilation.Editor;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.cosma.annihilation.Annihilation;
import com.cosma.annihilation.Editor.CosmaMap.CosmaEditorObject.MapObject;
import com.cosma.annihilation.Editor.CosmaMap.CosmaEditorObject.RectangleObject;
import com.cosma.annihilation.Editor.CosmaMap.ObjectMapLayer;
import com.cosma.annihilation.Screens.MapEditor;
import com.cosma.annihilation.Utils.OLDAssetDescriptors;
import com.cosma.annihilation.Utils.Utilities;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.util.adapter.AbstractListAdapter;
import com.kotcrab.vis.ui.util.adapter.ArrayAdapter;
import com.kotcrab.vis.ui.util.adapter.ListSelectionAdapter;
import com.kotcrab.vis.ui.widget.*;

public class ObjectsListWindow extends VisWindow implements InputProcessor {

    private MapEditor mapEditor;
    private VisTable layersTable;
    private ListView<MapObject> view;
    private OrthographicCamera camera;
    private RectangleObject selectedObject;
    private Body selectedBody;
    private final ObjectAdapter adapter;
    private boolean canDragRight, canDragLeft, canDragUp, canDragDown, canDragObject, canRotateObject,
            isLeftButtonPressed, isRightButtonPressed;

    @Override
    protected void close() {
        super.close();
        mapEditor.objectPanel.setObjectListWindowOpen(false);
        mapEditor.getInputMultiplexer().removeProcessor(this);
//        if (!adapter.getSelection().isEmpty()) {
//            adapter.getSelectionManager().deselectAll();
//        }
    }

    public ObjectsListWindow(final MapEditor mapEditor, OrthographicCamera camera) {
        super("Map layers");
        this.mapEditor = mapEditor;
        this.camera = camera;
        TableUtils.setSpacingDefaults(this);
        columnDefaults(0).left();
        addCloseButton();
        adapter = new ObjectAdapter(mapEditor.layersPanel.getSelectedLayer(ObjectMapLayer.class).getObjects().getObjects(), mapEditor,this);
        view = new ListView<MapObject>(adapter);
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
                Array<Body> bodies = new Array<Body>();
                selectedObject = (RectangleObject) item;
                mapEditor.getWorld().getBodies(bodies);
                for (Body body : bodies) {
                    if (body.getUserData().equals(item.getName())) {
                        selectedBody = body;
                    }
                }
            }

            @Override
            public void deselected(MapObject item, VisTable view) {
                item.setHighlighted(false);
                System.out.println("hjhj");
                selectedBody = null;
                selectedObject = null;
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
        if (canDragRight && isLeftButtonPressed) {
            float width = vec.x - selectedObject.getX();
            if (width < 0.2f) width = 0.2f;
            selectedObject.setWidth(width);
        }
        if (canDragLeft && isLeftButtonPressed) {
            amountX = vec.x - deltaVec.x;
            startX = deltaVec.x;
            if (selectedObject.getWidth() - amountX < 0.2f) amountX = 0;
            selectedObject.setX(selectedObject.getX() + amountX);
            selectedObject.setWidth(selectedObject.getWidth() - amountX);
        }
        if (canDragDown && isLeftButtonPressed) {
            float height = vec.y - selectedObject.getY();
            if (height < 0.2f) height = 0.2f;
            selectedObject.setHeight(height);
        }
        if (canDragUp && isLeftButtonPressed) {
            amountY = vec.y - deltaVec.y;
            startY = deltaVec.y;
            if (selectedObject.getHeight() - amountY < 0.2f) amountY = 0;
            selectedObject.setY(selectedObject.getY() + amountY);
            selectedObject.setHeight(selectedObject.getHeight() - amountY);
        }
        if (canDragObject && isLeftButtonPressed) {
            amountX = vec.x - deltaVec.x;
            amountY = vec.y - deltaVec.y;
            selectedObject.setX(selectedObject.getX() + amountX);
            selectedObject.setY(selectedObject.getY() + amountY);
        }
        if (canRotateObject && isRightButtonPressed) {
            amountY = vec.y - deltaVec.y;
            selectedObject.setRotation(selectedObject.getRotation() + amountY * 7);
        }
        if (selectedBody != null) {
            float x = selectedObject.getX();
            float y = selectedObject.getY();
            float width = selectedObject.getWidth();
            float height = selectedObject.getHeight();
            ((PolygonShape) selectedBody.getFixtureList().first().getShape()).setAsBox(width / 2, height / 2);
            selectedBody.setTransform(width / 2 + x, height / 2 + y, selectedObject.getRotation() * MathUtils.degreesToRadians);
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
            if (Utilities.roundFloat(y + height, 1) == Utilities.roundFloat(vec.y, 1) && Utilities.isFloatInRange(vec.x, x, x + width)) {
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.VerticalResize);
                canDragDown = true;
            } else {
                canDragDown = false;
            }
            if (Utilities.isFloatInRange(vec.x, x + 0.1f, x + width - 0.1f)
                    && (Utilities.isFloatInRange(vec.y, y + 0.1f, y + height - 0.1f))) {
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Hand);
                canDragObject = true;
                canRotateObject = true;
            } else {
                canDragObject = false;
                canRotateObject = false;
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
        private ObjectsListWindow objectsListWindow;



        private ObjectAdapter(Array<MapObject> array, MapEditor mapEditor, ObjectsListWindow objectsListWindow) {
            super(array);
            setSelectionMode(SelectionMode.SINGLE);
            this.mapEditor = mapEditor;
            this.objectsListWindow = objectsListWindow;
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
            VisLabel label = new VisLabel(item.getName());
            final VisImageButton bodySetting = new VisImageButton(new TextureRegionDrawable(Annihilation.getAssets().get("gfx/atlas/editor_icon.atlas", TextureAtlas.class).findRegion("settings_")));
            bodySetting.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {

                        BodyFilterWindow window= new BodyFilterWindow(objectsListWindow.selectedBody);
                        objectsListWindow.getStage().addActor(window);

                }
            });
            VisTextButton delete = new VisTextButton("x");
            delete.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    mapEditor.layersPanel.getSelectedLayer(ObjectMapLayer.class).getObjects().remove(item.getName());
                    Array<Body> bodies = new Array<Body>();
                    mapEditor.getWorld().getBodies(bodies);
                    for (Body body : bodies) {
                        if (body.getUserData().equals(item.getName())) {
                            mapEditor.getWorld().destroyBody(body);
                            objectsListWindow.selectedObject = null;
                            objectsListWindow.selectedBody = null;
                        }
                    }
                    view.rebuildView();
                }
            });
            VisTable table = new VisTable();
            table.center();
            table.add(label).fill().expandX();
            table.add(bodySetting).size(29);
            table.add(delete);
            return table;
        }
    }
}
