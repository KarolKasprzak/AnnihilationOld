package com.cosma.annihilation.World;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.maps.*;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.*;
import com.cosma.annihilation.Components.BodyComponent;
import com.cosma.annihilation.Components.PlayerComponent;
import com.cosma.annihilation.Entities.PlayerEntity;
import com.cosma.annihilation.Utils.BodyID;
import com.cosma.annihilation.Utils.CollisionID;
import com.cosma.annihilation.Utils.Constants;

class WorldLoader {
    private  Engine engine;
    private  World world;
    private  TiledMap gameMap;
     WorldLoader(Engine engine,World world,TiledMap gameMap){
        this.engine = engine;
        this.world = world;
        this.gameMap = gameMap;
        createEntitys();
        loadMap();
    }
    void loadMap(){

                    MapLayers layers = gameMap.getLayers();
                    MapLayer layer = layers.get("object");
                    for(MapObject mo : layer.getObjects()){
                        if("spawn".equals(mo.getName())){
                            float[] dimension = getDimension(mo);
                            Entity player = engine.getEntitiesFor(Family.all(PlayerComponent.class).get()).first();
                            Body playerBody =  player.getComponent(BodyComponent.class).body;
                            playerBody.setTransform(dimension[0],dimension[1],0);
                        }
                        if("ladder".equals(mo.getName())){
                            float[] dimension = getDimension(mo);
                            Body body = createLadder(dimension[0],dimension[1],dimension[2],dimension[3]);
                        }
                        if("ground".equals(mo.getName())) {
                            float[] dimension = getDimension(mo);
                            Body body = createBoxBody(dimension[0], dimension[1], dimension[2], dimension[3], false, 0);
                        }
                        if("descent".equals(mo.getName())) {
                            float[] dimension = getDimension(mo);
                            Body body = createBoxBody(dimension[0], dimension[1], dimension[2], dimension[3], false, 1);
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
                        boxBody.createFixture(fixtureDef).setUserData(BodyID.GROUND);
                        fixtureDef.filter.categoryBits = CollisionID.CATEGORY_GROUND;

                        break;

                    case 1:
                        fixtureDef.isSensor = true;
                        boxBody.createFixture(fixtureDef).setUserData(BodyID.DESCENT);
                        fixtureDef.filter.categoryBits = CollisionID.CATEGORY_LADDER;
                        fixtureDef.filter.maskBits = CollisionID.CATEGORY_PLAYER_SENSOR;

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
                PolygonShape fshape = new PolygonShape();
                fshape.setAsBox(width/2, height/2);
                FixtureDef fixtureDef = new FixtureDef();
                fixtureDef.shape = fshape;
                fixtureDef.density = 1f;
                fixtureDef.isSensor = true;
                fixtureDef.filter.categoryBits = CollisionID.CATEGORY_LADDER;
                fixtureDef.filter.maskBits = CollisionID.CATEGORY_PLAYER_SENSOR;
                ladderBody.createFixture(fixtureDef).setUserData(BodyID.LADDER);
                shape.dispose();
                return ladderBody;

        }

        private void createEntitys(){
            PlayerEntity playerEntity = new PlayerEntity(engine,world);
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
