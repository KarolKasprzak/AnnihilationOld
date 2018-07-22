package com.cosma.annihilation.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.cosma.annihilation.Utils.Constants;

public class TileMapRender extends IteratingSystem {

    private OrthographicCamera camera;
    private TiledMapRenderer tiledMapRenderer;

        public TileMapRender(OrthographicCamera camera,TiledMap tiledMap) {
            super(Family.all().get(),Constants.TILE_MAP_RENDER);
            this.camera = camera;
            this.tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap,1f/32f);
        }


    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

    }


    @Override
    protected void processEntity(Entity entity, float deltaTime) {

    }
}
