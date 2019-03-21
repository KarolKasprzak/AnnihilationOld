package com.cosma.annihilation.Editor.CosmaMap;

public class TileMapLayer extends MapLayer {
    private int width;
    private int height;
    private Tile[][] tiles;

    public TileMapLayer(int width, int height, String name) {
        super(width, height, name);
        this.height = height;
        this.width = width;
        this.tiles = new Tile[width][height];
    }

    public TileMapLayer() {

    }

    public void getTIleSIze(){
        System.out.println(tiles[0][0] == null);
        System.out.println(tiles[1][0] == null);
    }

    public Tile getTile(int x, int y) {
        if (x < 0 || x >= width) return null;
        if (y < 0 || y >= height) return null;
        return tiles[x][y];
    }
    public void setTile(int x, int y, Tile tile) {
        if (x < 0 || x >= width) return;
        if (y < 0 || y >= height) return;
        tiles[x][y] = tile;
    }
}
