package com.cosma.annihilation.Editor;

import com.badlogic.gdx.graphics.Color;

public class MapLayer {
    private int width;
    private int height;
    private Cell[][] cells;
    private String name = "";
    private boolean visible = true;;
    private Color color;

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public MapLayer(int width, int height, String name) {
        this.width = width;
        this.height = height;
        this.cells = new Cell[width][height];
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isLayerVisible(){
        return visible;
    }

    public Cell getCell (int x, int y) {
        if (x < 0 || x >= width) return null;
        if (y < 0 || y >= height) return null;
        return cells[x][y];
    }
    public void setCell (int x, int y, Cell cell) {
        if (x < 0 || x >= width) return;
        if (y < 0 || y >= height) return;
        cells[x][y] = cell;
    }
}
