package com.cosma.annihilation.Utils.Serialization;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.cosma.annihilation.Annihilation;
import com.cosma.annihilation.Components.*;
import com.cosma.annihilation.Gui.Inventory.InventoryItemLocation;
import com.cosma.annihilation.Utils.Enums.BodyID;
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

            if(component instanceof TextureComponent){
                json.writeValue("patch", Annihilation.getAssets().getAssetFileName(((TextureComponent) component).texture));
            }

            if(component instanceof PlayerComponent){

            }

            if(component instanceof AnimationComponent){
                //TODO
            }

            if(component instanceof PlayerInventoryComponent){
                //TODO
            }

            if(component instanceof TagComponent){
                json.writeValue("tag", ((TagComponent) component).tag);
            }

            if(component instanceof ContainerComponent){
                json.writeValue("name", ((ContainerComponent) component).name);
                json.writeArrayStart("itemList");
                for(InventoryItemLocation location: ((ContainerComponent) component).itemLocations){
                    json.writeObjectStart();
                    json.writeValue("tableIndex",location.getTableIndex());
                    json.writeValue("itemID",location.getItemID());
                    json.writeValue("itemsAmount",location.getItemsAmount());
                    json.writeObjectEnd();
                }
                json.writeArrayEnd();

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
                    json.writeValue("categoryBits",fixture.getFilterData().categoryBits);
                    if(fixture.getUserData() != null){
                        json.writeValue("hasUserDate", true);
                        if(fixture.getUserData() instanceof BodyID){
                            json.writeValue("userDate", fixture.getUserData());
                        }
                    }else json.writeValue("hasUserDate", false);

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
//            bodyDef.position.set(jsonData.get("BodyComponent").get("positionX").asFloat(), jsonData.get("BodyComponent").get("positionY").asFloat());
            bodyComponent.body = world.createBody(bodyDef);
            bodyComponent.body.setFixedRotation(jsonData.get("BodyComponent").get("bullet").asBoolean());
            bodyComponent.body.setBullet(jsonData.get("BodyComponent").get("bullet").asBoolean());
            bodyComponent.body.setUserData(entity);
            bodyComponent.body.setTransform(new Vector2(jsonData.get("BodyComponent").get("positionX").asFloat(), jsonData.get("BodyComponent").get("positionY").asFloat()),0);
            entity.add(bodyComponent);
            for (JsonValue value : jsonData.get("BodyComponent").get("Fixtures")) {
                FixtureDef fixtureDef = new FixtureDef();

                if(value.has("shapeX") && value.has("shapeY")){
                    PolygonShape shape = new PolygonShape();
                    shape.setAsBox(value.get("shapeW").asFloat()/2,value.get("shapeH").asFloat()/2,
                            new Vector2(value.get("shapeX").asFloat(),value.get("shapeY").asFloat()),value.get("shapeA").asFloat());
                    fixtureDef.shape = shape;
                }

                if(value.has("polygon")){
                    if (value.get("shapeType").asString().equals("Polygon")) {
                        PolygonShape shape = new PolygonShape();
                        shape.set(value.get("polygon").asFloatArray());
                        fixtureDef.shape = shape;
                    }
                }

                if (value.get("shapeType").asString().equals("Circle")) {
                    CircleShape shape = new CircleShape();
                    shape.setRadius(value.get("radius").asFloat());
                    fixtureDef.shape = shape;
                }
                fixtureDef.isSensor = value.get("sensor").asBoolean();
                fixtureDef.density = value.get("destiny").asFloat();
                fixtureDef.friction = value.get("friction").asFloat();
                fixtureDef.restitution = value.get("restitution").asFloat();
                fixtureDef.filter.categoryBits = CollisionID.NO_SHADOW | CollisionID.JUMPABLE_OBJECT;
                if (value.get("hasUserDate").asBoolean()){
                    bodyComponent.body.createFixture(fixtureDef).setUserData(BodyID.valueOf(value.get("userDate").asString()));
                }else bodyComponent.body.createFixture(fixtureDef);
                fixtureDef.shape.dispose();
            }
        }

        if (jsonData.hasChild("HealthComponent")) {
            HealthComponent healthComponent = new HealthComponent();
            healthComponent.hp = jsonData.get("HealthComponent").get("hp").asInt();
            healthComponent.maxHP = jsonData.get("HealthComponent").get("maxHP").asInt();
            entity.add(healthComponent);
        }

        if (jsonData.hasChild("TextureComponent")) {
            //TODO
            TextureComponent textureComponent = new TextureComponent();
            textureComponent.texture = Annihilation.getAssets().get(jsonData.get("TextureComponent").get("patch").asString());
            entity.add(textureComponent);
        }

        if (jsonData.hasChild("TagComponent")) {
            TagComponent tagComponent = new TagComponent();
            tagComponent.tag = jsonData.get("TagComponent").get("tag").asString();
            entity.add(tagComponent);
        }

        if (jsonData.hasChild("ContainerComponent")) {
            ContainerComponent containerComponent = new ContainerComponent();
            containerComponent.name = jsonData.get("ContainerComponent").get("name").asString();
            containerComponent.itemLocations = new Array<>();
            for(JsonValue value : jsonData.get("ContainerComponent").get("itemList")){
                InventoryItemLocation location = new InventoryItemLocation();
                location.setTableIndex(value.get("tableIndex").asInt());
                location.setItemID(value.get("itemID").asString());
                location.setItemsAmount(value.get("itemsAmount").asInt());
                containerComponent.itemLocations.add(location);
            }
            entity.add(containerComponent);
        }

        if (jsonData.hasChild("ActionComponent")) {
            ActionComponent actionComponent = new ActionComponent();
            actionComponent.action = EntityAction.valueOf(jsonData.get("ActionComponent").get("action").asString());
            entity.add(actionComponent);
        }

        if(jsonData.hasChild("PlayerComponent")){
            PlayerComponent playerComponent = new PlayerComponent();
            entity.add(playerComponent);
        }

        if(jsonData.hasChild("AnimationComponent")){
            AnimationComponent animationComponent = new AnimationComponent();
            entity.add(animationComponent);
        }

        if(jsonData.hasChild("PlayerInventoryComponent")){
            PlayerInventoryComponent playerInventoryComponent = new PlayerInventoryComponent();
            entity.add(playerInventoryComponent);

        }

        return entity;
    }

}
