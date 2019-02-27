package com.cosma.annihilation.Editor.CosmaMap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.cosma.annihilation.Editor.CosmaMap.CosmaEditorObject.RectangleObject;

import javax.rmi.ssl.SslRMIClientSocketFactory;

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

    public void renderGrid(){
        for (int x = 0; x < gameMap.getWidth(); x++) {
            for (int y = 0; y < gameMap.getHeight(); y++) {
                renderer.begin();
                renderer.setColor(Color.BLACK);
                renderer.rect(0,0,gameMap.getWidth(),gameMap.getHeight());
                renderer.setColor(0,0,0,0.2f);
                renderer.line(x, y, x + gameMap.getTileSize() / scale, y);
                renderer.line(x, y, x, y + gameMap.getTileSize() / scale);
                renderer.end();
            }
        }
    }

    public void renderMap() {
        if(gameMap.getLayers().getCount()>0) {
            for(TileMapLayer mapLayer: gameMap.getLayers().getByType(TileMapLayer.class)){
                if (mapLayer.isLayerVisible()) {
                    for (int x = 0; x < gameMap.getWidth(); x++) {
                        for (int y = 0; y < gameMap.getHeight(); y++) {
                            Tile tile = mapLayer.getTile(x,y);
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
            for(ObjectMapLayer layer: gameMap.getLayers().getByType(ObjectMapLayer.class)){
                if (layer.isLayerVisible()) {
                    for (RectangleObject object: layer.getObjects().getByType(RectangleObject.class)){
                        renderer.setColor(Color.WHITE);
                        if(object.isHighlighted()){
                            renderer.setColor(Color.ORANGE);
                        }
                        renderer.begin();
                        renderer.rect(object.x,object.y,object.w,object.h);
                        renderer.end();
                    }
                }
            }
        }
    }
}
