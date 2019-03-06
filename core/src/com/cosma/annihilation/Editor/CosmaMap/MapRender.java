package com.cosma.annihilation.Editor.CosmaMap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.cosma.annihilation.Annihilation;
import com.cosma.annihilation.Editor.CosmaMap.CosmaEditorLights.MapLight;
import com.cosma.annihilation.Editor.CosmaMap.CosmaEditorLights.MapPointLight;
import com.cosma.annihilation.Editor.CosmaMap.CosmaEditorObject.RectangleObject;
import com.cosma.annihilation.Utils.GfxAssetDescriptors;

public class MapRender {

    private ShapeRenderer renderer;
    private int scale;
    private GameMap gameMap;
    private SpriteBatch batch;
    private TextureAtlas iconPack;

    public MapRender (ShapeRenderer renderer, GameMap gameMap,SpriteBatch batch) {
        this.batch = batch;
        this.gameMap = gameMap;
        this.scale = gameMap.getTileSize();
        this.renderer = renderer;
        iconPack = Annihilation.getAssets().get(GfxAssetDescriptors.editor_icons);
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

            for(LightsMapLayer mapLayer: gameMap.getLayers().getByType(LightsMapLayer.class)){
                if (mapLayer.isLayerVisible()) {
                    for(MapLight light: mapLayer.geLights()){
                        if(light instanceof MapPointLight){
                            TextureAtlas.AtlasRegion texture = iconPack.findRegion("point_light");
                            batch.begin();
                            batch.draw(texture,light.getX(),light.getY(),texture.getRegionWidth()/gameMap.getTileSize(),texture.getRegionHeight()/gameMap.getTileSize());
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
                        renderer.rect(object.getX(),object.getY(),object.getWidth()/2,object.getHeight()/2,object.getWidth(),object.getHeight(),1,1,object.getRotation());
                        renderer.end();
                    }
                }
            }
        }
    }
}
