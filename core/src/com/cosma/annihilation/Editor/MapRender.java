package com.cosma.annihilation.Editor;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class MapRender {

    private ShapeRenderer renderer;
    private int scale;
    private Map map;
    private int startX =0;
    private int startY =0;
    public MapRender (ShapeRenderer renderer,Map map) {

        this.map = map;
        this.scale = map.getTileSize();
        this.renderer = renderer;
        int startX =0;
        int startY =0;
    }

    public void renderMap() {
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {

                renderer.line(x, y, x + map.getTileSize() / scale, y);
                renderer.line(x, y, x, y + map.getTileSize() / scale);


            }
        }
    }

}
