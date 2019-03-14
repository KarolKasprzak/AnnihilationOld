package com.cosma.annihilation.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.cosma.annihilation.Editor.CosmaMap.GameMap;
import com.cosma.annihilation.Editor.CosmaMap.Tile;
import com.cosma.annihilation.Editor.CosmaMap.TileMapLayer;
import com.cosma.annihilation.Utils.Constants;

public class TileMapRender extends IteratingSystem {

    private OrthographicCamera camera;
    private GameMap tiledMap;
    protected Batch batch;

    public TileMapRender(OrthographicCamera camera, GameMap tiledMap) {
        super(Family.all().get(), Constants.TILE_MAP_RENDER);
        this.batch = new SpriteBatch();
        this.camera = camera;
        this.tiledMap = tiledMap;
    }


    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        for (TileMapLayer mapLayer : tiledMap.getLayers().getByType(TileMapLayer.class)) {
            if (mapLayer.isLayerVisible()) {
                for (int x = 0; x < tiledMap.getWidth(); x++) {
                    for (int y = 0; y < tiledMap.getHeight(); y++) {
                        Tile tile = mapLayer.getTile(x, y);
                        if (tile == null) {
                            continue;
                        }
                        if (tile.getTextureRegion() == null) {
                            continue;
                        }
                        TextureRegion texture = tile.getTextureRegion();
                        batch.draw(texture, x, y, texture.getRegionWidth() / tiledMap.getTileSize(), texture.getRegionHeight() / tiledMap.getTileSize());
                    }
                }
            }
        }

        batch.end();
    }


    @Override
    protected void processEntity(Entity entity, float deltaTime) {

    }
}
