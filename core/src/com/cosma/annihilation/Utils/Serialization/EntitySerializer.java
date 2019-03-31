package com.cosma.annihilation.Utils.Serialization;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.cosma.annihilation.Components.ActionComponent;
import com.cosma.annihilation.Components.BodyComponent;
import com.cosma.annihilation.Components.HealthComponent;
import com.cosma.annihilation.Components.TagComponent;
import com.cosma.annihilation.Utils.Enums.CollisionID;
import com.cosma.annihilation.Utils.Enums.EntityAction;


public class EntitySerializer implements Json.Serializer<Entity> {
    private World world;

    public EntitySerializer(World world) {
        this.world = world;

    }

    @Override
    public void write(Json json, Entity object, Class knownType) {
        json.writeObjectStart();
        for(Component component: object.getComponents()){
            json.writeObjectStart(component.getClass().getSimpleName());
            if(component instanceof HealthComponent){
                json.writeValue("hp", ((HealthComponent) component).hp);
                json.writeValue("maxHP", ((HealthComponent) component).maxHP);
            }

            if(component instanceof TagComponent){
                json.writeValue("tag", ((TagComponent) component).tag);
            }

            if(component instanceof BodyComponent){
                Body body = ((BodyComponent) component).body;
                json.writeValue("bodyType",body.getType().name());
                json.writeValue("positionX",body.getPosition().x);
                json.writeValue("positionY",body.getPosition().y);
                json.writeValue("fixedRotation",body.isFixedRotation());
                json.writeValue("bullet",body.isBullet());

                json.writeArrayStart("Fixtures");
                for(Fixture fixture: body.getFixtureList()){
                    json.writeObjectStart();
                    json.writeValue("shapeType",fixture.getType().name());
                    if(fixture.getType().equals(Shape.Type.Polygon)){
                        PolygonShape shape = (PolygonShape) fixture.getShape();
                        json.writeArrayStart("polygon");
                        for(int i = 0; i<shape.getVertexCount(); i++){
                            Vector2 vector2 = new Vector2();
                            shape.getVertex(i,vector2);
                            json.writeValue(vector2.x);
                            json.writeValue(vector2.y);
                        }
                        json.writeArrayEnd();
                    }
                    if(fixture.getType().equals(Shape.Type.Circle)){
                        CircleShape shape = (CircleShape) fixture.getShape();
                        json.writeValue("radius",shape.getRadius());
                    }
                    json.writeValue("sensor",fixture.isSensor());
                    json.writeValue("destiny",fixture.getDensity());
                    json.writeValue("friction",fixture.getFriction());
                    json.writeValue("restitution",fixture.getRestitution());
                    json.writeObjectEnd();
                }
                json.writeArrayEnd();
            }
            json.writeObjectEnd();
        }
        json.writeObjectEnd();
    }

    @Override
    public Entity read(Json json, JsonValue jsonData, Class type) {
        Entity entity = new Entity();
        if (jsonData.hasChild("BodyComponent")) {
            BodyComponent bodyComponent = new BodyComponent();
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.valueOf(jsonData.get("BodyComponent").get("bodyType").asString());
            bodyDef.position.set(jsonData.get("BodyComponent").get("positionX").asFloat(), jsonData.get("BodyComponent").get("positionY").asFloat());
            bodyComponent.body = world.createBody(bodyDef);
            bodyComponent.body.setFixedRotation(jsonData.get("BodyComponent").get("bullet").asBoolean());
            bodyComponent.body.setBullet(jsonData.get("BodyComponent").get("bullet").asBoolean());
            bodyComponent.body.setUserData(entity);
            entity.add(bodyComponent);
            for (JsonValue value : jsonData.get("BodyComponent").get("Fixtures")) {
                FixtureDef fixtureDef = new FixtureDef();
                if (value.get("shapeType").asString().equals("Polygon")) {
                    PolygonShape shape = new PolygonShape();
                    shape.set(value.get("polygon").asFloatArray());
                    fixtureDef.shape = shape;
                }
                if (value.get("shapeType").asString().equals("Circle")) {
                    float r = value.get("radius").asFloat();
                    CircleShape shape = new CircleShape();
                    shape.setRadius(1);
                    fixtureDef.shape = shape;
                }
                fixtureDef.isSensor = value.get("sensor").asBoolean();
                fixtureDef.density = value.get("destiny").asFloat();
                fixtureDef.friction = value.get("friction").asFloat();
                fixtureDef.restitution = value.get("restitution").asFloat();
//                fixtureDef.filter.categoryBits = CollisionID.NO_SHADOW | CollisionID.JUMPABLE_OBJECT;
                bodyComponent.body.getUserData();
                bodyComponent.body.createFixture(fixtureDef);
            }
        }

        if (jsonData.hasChild("HealthComponent")) {
            HealthComponent healthComponent = new HealthComponent();
            healthComponent.hp = jsonData.get("HealthComponent").get("hp").asInt();
            healthComponent.maxHP = jsonData.get("HealthComponent").get("maxHP").asInt();
            entity.add(healthComponent);
        }

        if (jsonData.hasChild("TextureComponent")) {
        }

        if (jsonData.hasChild("TagComponent")) {
            TagComponent tagComponent = new TagComponent();
            tagComponent.tag = jsonData.get("TagComponent").get("tag").asString();
            entity.add(tagComponent);
        }

        if (jsonData.hasChild("ActionComponent")) {
            ActionComponent actionComponent = new ActionComponent();
            actionComponent.action = EntityAction.valueOf(jsonData.get("ActionComponent").get("action").asString());
            entity.add(actionComponent);
        }
        return entity;
    }
}
