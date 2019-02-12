package com.cosma.annihilation.Editor;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class MapRender {

    private ShapeRenderer renderer;
    private int scale;
    private GameMap gameMap;
    public MapRender (ShapeRenderer renderer,GameMap gameMap) {

        this.gameMap = gameMap;
        this.scale = gameMap.getTileSize();
        this.renderer = renderer;
    }

    public void renderMap() {
        if(gameMap.getLayers().getCount()>0) {
            for(MapLayer mapLayer: gameMap.getLayers()){
                if (mapLayer.isLayerVisible()) {
                    for (int x = 0; x < gameMap.getWidth(); x++) {
                        for (int y = 0; y < gameMap.getHeight(); y++) {
                            if(mapLayer.getColor() != null){
                                renderer.setColor(mapLayer.getColor());
                            }

                            renderer.line(x, y, x + gameMap.getTileSize() / scale, y);
                            renderer.line(x, y, x, y + gameMap.getTileSize() / scale);


                        }
                    }
                }
            }
        }
    }
}
