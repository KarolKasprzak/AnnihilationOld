package com.cosma.annihilation.World;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.physics.box2d.*;

public class WorldLoader {
    private  Engine engine;
    private  World world;
    private  TiledMap gameMap;
     WorldLoader(Engine engine,World world,TiledMap gameMap){
        this.engine = engine;
        this.world = world;
        this.gameMap = gameMap;
    }
    public void loadMap(){


        MapLayers layers = gameMap.getLayers();
        MapLayer layer = layers.get("object");
        MapObject object = layer.getObjects().get("playerPos");
        RectangleMapObject rectangleMapObject =(RectangleMapObject) layer.getObjects().get("playerPos");

        float x = rectangleMapObject.getRectangle().x;
        float y = rectangleMapObject.getRectangle().y;
        float width = rectangleMapObject.getRectangle().width;
        float height = rectangleMapObject.getRectangle().height;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x/16, y/16);
        Body body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/16, height/16);
        //Fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        body.createFixture(fixtureDef);

        }
}
