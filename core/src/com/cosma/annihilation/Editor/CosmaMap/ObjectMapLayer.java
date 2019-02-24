package com.cosma.annihilation.Editor.CosmaMap;

import com.cosma.annihilation.Editor.CosmaMap.CosmaEditorObject.MapObjects;
import com.cosma.annihilation.Editor.CosmaMap.CosmaEditorObject.RectangleObject;

public class ObjectMapLayer extends MapLayer {
    private int width;
    private int height;
    private MapObjects objects = new MapObjects();

    public ObjectMapLayer(int width, int height, String name) {
        super(width, height, name);
    }

    public ObjectMapLayer() {
    }

    public MapObjects getObjects() {
        return objects;
    }

    public void createBoxObject(float x, float y, float w, float h) {
        String name = "object" + (objects.getCount() + 1);
        RectangleObject rec = new RectangleObject(x,y,w,h,0);
        rec.setName(name);
        objects.add(rec);
    }
}
