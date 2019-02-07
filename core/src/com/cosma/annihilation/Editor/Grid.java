package com.cosma.annihilation.Editor;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Grid {

    private int mapWidth, mapHeight, gridSize;
    private ShapeRenderer renderer;

    public Grid(int mapWidth, int mapHeight, int gridSize, ShapeRenderer renderer) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.gridSize = gridSize/32;

        this.renderer = renderer;


    }

    public void render(){
        int startX =0;
        int startY =0;
        renderer.line(startX,startY,startX, gridSize);

        startX = startX + gridSize;
      for(int x = 0; x < mapWidth; x++){
//          renderer.line(startX,startY,startX + gridSize,startY);

          renderer.line(startX,startY,startX, gridSize);

          startX = startX + gridSize;
     }
    }
}
