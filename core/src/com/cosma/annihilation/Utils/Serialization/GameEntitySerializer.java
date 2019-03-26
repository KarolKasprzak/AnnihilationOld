package com.cosma.annihilation.Utils.Serialization;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.cosma.annihilation.Components.ActionComponent;
import com.cosma.annihilation.Components.BodyComponent;
import com.cosma.annihilation.Components.HealthComponent;
import com.cosma.annihilation.Components.TagComponent;
import com.cosma.annihilation.Utils.Enums.EntityAction;


public class GameEntitySerializer implements Json.Serializer<Entity> {
    private World world;
    public GameEntitySerializer(World world){
        this.world = world;

    }

    @Override
    public void write(Json json, Entity object, Class knownType) {

    }

    @Override
    public Entity read(Json json, JsonValue jsonData, Class type) {
        Entity entity = new Entity();

        if (jsonData.hasChild("BodyComponent")) {
            BodyComponent bodyComponent = new BodyComponent();
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.valueOf(jsonData.get("BodyComponent").get("bodyType").asString());
            bodyDef.position.set(jsonData.get("BodyComponent").get("positionX").asFloat(),jsonData.get("BodyComponent").get("positionY").asFloat());
            bodyComponent.body = world.createBody(bodyDef);
            bodyComponent.body.setUserData(entity);
            entity.add(bodyComponent);
            for(JsonValue value: jsonData.get("BodyComponent").get("Fixtures")){
                System.out.println(value.get("shapeType"));
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
            actionComponent.action = EntityAction.valueOf( jsonData.get("ActionComponent").get("action").asString());
            entity.add(actionComponent);
        }
        return entity;
    }
}
