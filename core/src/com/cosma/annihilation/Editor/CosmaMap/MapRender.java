package com.cosma.annihilation.Editor.CosmaMap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.cosma.annihilation.Annihilation;
import com.cosma.annihilation.Editor.CosmaMap.CosmaEditorLights.MapConeLight;
import com.cosma.annihilation.Editor.CosmaMap.CosmaEditorLights.MapLight;
import com.cosma.annihilation.Editor.CosmaMap.CosmaEditorLights.MapPointLight;
import com.cosma.annihilation.Editor.CosmaMap.CosmaEditorObject.RectangleObject;
import com.cosma.annihilation.Utils.OLDAssetDescriptors;

public class MapRender {

    private ShapeRenderer renderer;
    private int scale;
    private GameMap gameMap;
    private SpriteBatch batch;
    private TextureAtlas iconPack;

    public MapRender(ShapeRenderer renderer, GameMap gameMap, SpriteBatch batch) {
        this.batch = batch;
        this.gameMap = gameMap;
        this.scale = gameMap.getTileSize();
        this.renderer = renderer;
        iconPack = Annihilation.getAssets().get("gfx/atlas/editor_icon.atlas", TextureAtlas.class);
    }

    public void renderGrid() {
        renderer.begin();
        for (int x = 0; x < gameMap.getWidth(); x++) {
            for (int y = 0; y < gameMap.getHeight(); y++) {
                renderer.setColor(Color.BLACK);
                renderer.rect(0, 0, gameMap.getWidth(), gameMap.getHeight());
                renderer.setColor(0, 0, 0, 0.2f);
                renderer.line(x, y, x + gameMap.getTileSize() / scale, y);
                renderer.line(x, y, x, y + gameMap.getTileSize() / scale);
            }
        }
        renderer.end();
    }

    public void renderMap() {
        batch.begin();
        if (gameMap.getLayers().getCount() > 0) {
            for (TileMapLayer mapLayer : gameMap.getLayers().getByType(TileMapLayer.class)) {
                if (mapLayer.isLayerVisible()) {
                    for (int x = 0; x < gameMap.getWidth(); x++) {
                        for (int y = 0; y < gameMap.getHeight(); y++) {
                            if (mapLayer.getTile(x, y) == null) {
                                continue;
                            }
                            Tile tile = mapLayer.getTile(x, y);
                            if (tile.getTextureRegion() == null) {
                                continue;
                            }
                            TextureRegion texture = tile.getTextureRegion();
                            batch.draw(tile.getTextureRegion(), x, y, texture.getRegionWidth() / gameMap.getTileSize(), texture.getRegionHeight() / gameMap.getTileSize());
                        }
                    }
                }
            }

            for (LightsMapLayer mapLayer : gameMap.getLayers().getByType(LightsMapLayer.class)) {
                if (mapLayer.isLayerVisible()) {
                    for (MapLight light : mapLayer.getLights()) {
                        if (light instanceof MapPointLight) {
                            TextureAtlas.AtlasRegion texture = iconPack.findRegion("point_light");
                            if (light.isHighlighted()) {
                                texture = iconPack.findRegion("point_light_h");
                            }
                            batch.draw(texture, light.getX() - (texture.getRegionWidth() / gameMap.getTileSize()) / 4, light.getY() - (texture.getRegionHeight() / gameMap.getTileSize()) / 4, texture.getRegionWidth() / gameMap.getTileSize() / 2, texture.getRegionHeight() / gameMap.getTileSize() / 2);
                        }
                        if (light instanceof MapConeLight) {
                            TextureAtlas.AtlasRegion texture = iconPack.findRegion("cone_light");
                            if (light.isHighlighted()) {
                                texture = iconPack.findRegion("point_light_h");
                            }
                            batch.draw(texture, light.getX() - (texture.getRegionWidth() / gameMap.getTileSize()) / 4, light.getY() - (texture.getRegionHeight() / gameMap.getTileSize()) / 4, texture.getRegionWidth() / gameMap.getTileSize() / 2, texture.getRegionHeight() / gameMap.getTileSize() / 2);
                        }
                    }
                }
            }
        }
        batch.end();
        for (ObjectMapLayer layer : gameMap.getLayers().getByType(ObjectMapLayer.class)) {
            if (layer.isLayerVisible()) {
                renderer.begin();
                for (RectangleObject object : layer.getObjects().getByType(RectangleObject.class)) {
                    renderer.setColor(Color.WHITE);
                    if (object.isHighlighted()) {
                        renderer.setColor(Color.ORANGE);
                    }
                    renderer.rect(object.getX(), object.getY(), object.getWidth() / 2, object.getHeight() / 2, object.getWidth(), object.getHeight(), 1, 1, object.getRotation());
                }
                renderer.end();
            }
        }
    }
}
