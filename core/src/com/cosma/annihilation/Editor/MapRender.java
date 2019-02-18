package com.cosma.annihilation.Editor;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class MapRender {

    private ShapeRenderer renderer;
    private int scale;
    private GameMap gameMap;
    private SpriteBatch batch;

    public MapRender (ShapeRenderer renderer, GameMap gameMap,SpriteBatch batch) {
        this.batch = batch;
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
                            Tile tile = mapLayer.getCell(x,y);
                            if (tile == null) {
                                continue;
                            }
                            if(tile.getTextureRegion() == null){
                                continue;
                            }
                            TextureRegion texture = tile.getTextureRegion();
                            batch.begin();
                            batch.draw(texture,x,y,texture.getRegionWidth()/gameMap.getTileSize(),texture.getRegionHeight()/gameMap.getTileSize());
                            batch.end();

                        }
                    }
                }
            }
        }
    }
}
