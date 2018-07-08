package com.cosma.annihilation.World;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.maps.*;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.*;
import com.cosma.annihilation.Utils.BodyID;
import com.cosma.annihilation.Utils.Constants;

class WorldLoader {
    private  Engine engine;
    private  World world;
    private  TiledMap gameMap;
     WorldLoader(Engine engine,World world,TiledMap gameMap){
        this.engine = engine;
        this.world = world;
        this.gameMap = gameMap;
    }
    void loadMap(){

                    MapLayers layers = gameMap.getLayers();
                    MapLayer layer = layers.get("object");
                    MapObject mobject = layer.getObjects().get("spawn");

                    System.out.println(mobject.getProperties().get("x"));

                    for(MapObject mo : layer.getObjects()){
                        if("spawnBox".equals(mo.getName())){
                            float[] dimension = getDimension(mo);
                            Body body = createBoxBody(dimension[0],dimension[1],1,1,true,0);
                        }
                        if("ladder".equals(mo.getName())){
                            float[] dimension = getDimension(mo);
                            Body body = createLadder(dimension[0],dimension[1],dimension[2],dimension[3]);
                        }
                        if("ground".equals(mo.getName())) {
                            float[] dimension = getDimension(mo);
                            Body body = createBoxBody(dimension[0], dimension[1], dimension[2], dimension[3], false, 0);
                        }
                    }
                }

        private Body createBoxBody(float x, float y, float width, float height,boolean dynamic,int selectType) {
                BodyDef bodyDef = new BodyDef();
                if (dynamic) {
                    bodyDef.type = BodyDef.BodyType.DynamicBody;
                } else {
                    bodyDef.type = BodyDef.BodyType.StaticBody;
                }
                bodyDef.position.set((width / 2) + x, (height / 2) + y);
                Body boxBody = world.createBody(bodyDef);
                PolygonShape shape = new PolygonShape();
                shape.setAsBox(width / 2, height / 2);
                //Fixture
                FixtureDef fixtureDef = new FixtureDef();
                fixtureDef.shape = shape;
                fixtureDef.density = 1f;

                switch (selectType) {
                    case 0:
                        boxBody.createFixture(fixtureDef);
                        break;

                    case 1:
                        fixtureDef.isSensor = true;
                        boxBody.createFixture(fixtureDef);
                        break;

                    case 2:
                        fixtureDef.isSensor = true;
                        boxBody.createFixture(fixtureDef).setUserData(BodyID.LADDER);
                        break;

                }
                shape.dispose();
                return boxBody;
        }

        private Body createLadder(float x, float y, float width, float height){
                BodyDef bodyDef = new BodyDef();
                bodyDef.type = BodyDef.BodyType.KinematicBody;
                bodyDef.position.set((width / 2)+x ,(height / 2)+y);
                Body ladderBody = world.createBody(bodyDef);
                PolygonShape shape = new PolygonShape();
                shape.setAsBox(width/2, height/2);
                //Fixture
                FixtureDef fixtureDef = new FixtureDef();
                fixtureDef.shape = shape;
                fixtureDef.density = 1f;
                fixtureDef.isSensor = true;
                fixtureDef.filter.categoryBits = Constants.NOT_COLIDED;
                ladderBody.createFixture(fixtureDef).setUserData(BodyID.LADDER);
                shape.dispose();
                return ladderBody;

        }


        private float[] getDimension(MapObject mapObject){
            MapProperties properties = mapObject.getProperties();
            String[] dimension = {"x","y","width","height"};
            float[] values = new float[dimension.length];

            for(int i=0; i < dimension.length; i++){
                if(! properties.containsKey(dimension[i])){
                    System.out.println("properties error");
                    return null;
                }
                values[i] =  properties.get(dimension[i], float.class) / Constants.PPM;
            }
            return values;
        }

}
