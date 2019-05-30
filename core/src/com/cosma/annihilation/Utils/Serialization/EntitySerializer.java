package com.cosma.annihilation.Utils.Serialization;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.cosma.annihilation.Annihilation;
import com.cosma.annihilation.Components.*;
import com.cosma.annihilation.Gui.Inventory.InventoryItemLocation;
import com.cosma.annihilation.Items.InventoryItem;
import com.cosma.annihilation.Utils.AnimationFactory;
import com.cosma.annihilation.Utils.Enums.BodyID;
import com.cosma.annihilation.Utils.Enums.EntityAction;


public class EntitySerializer implements Json.Serializer<Entity> {
    private World world;
    private Engine engine;
    private AnimationFactory animationFactory;

    /** use in game **/
    EntitySerializer(World world, Engine engine) {
        this.world = world;
        this.engine = engine;
        animationFactory = new AnimationFactory();
    }
    /** use only in map editor **/
    public EntitySerializer(World world) {
        this.world = world;
        this.engine = null;
        animationFactory = new AnimationFactory();
    }

    @Override
    public void write(Json json, Entity object, Class knownType) {

        json.writeObjectStart();
        for (Component component : object.getComponents()) {
            json.writeObjectStart(component.getClass().getSimpleName());
            if (component instanceof HealthComponent) {
                json.writeValue("hp", ((HealthComponent) component).hp);
                json.writeValue("maxHP", ((HealthComponent) component).maxHP);
                json.writeObjectEnd();
                continue;
            }

//            if (component instanceof GateComponent) {
//                json.writeValue("targetMapPath", ((GateComponent) component).targetMapPath);
//                json.writeValue("positionX", ((GateComponent) component).playerPositionOnTargetMap.x);
//                json.writeValue("positionY", ((GateComponent) component).playerPositionOnTargetMap.y);
//                json.writeObjectEnd();
//                continue;
//            }

            if (component instanceof TextureComponent) {
                if (((TextureComponent) component).texture == null) {
                    json.writeObjectEnd();
                    continue;
                }
                json.writeValue("patch", Annihilation.getAssets().getAssetFileName(((TextureComponent) component).texture));
            }

            if (component instanceof AiComponent) {
                json.writeObjectEnd();
                continue;
            }

            if (component instanceof EnemyComponent) {
                json.writeObjectEnd();
                continue;
            }

            if (component instanceof ActionComponent) {
                json.writeValue("action", ((ActionComponent) component).action.name());
                json.writeObjectEnd();
                continue;
            }

            if (component instanceof PlayerComponent) {
                json.writeObjectEnd();
                continue;
            }

            if (component instanceof StateComponent) {
                json.writeObjectEnd();
                continue;
            }

            if (component instanceof AnimationComponent) {
                json.writeValue("id",((AnimationComponent) component).animationId.name());
                json.writeObjectEnd();
                continue;
                //TODO
            }

            if (component instanceof PlayerInventoryComponent) {
                json.writeObjectEnd();
                continue;
                //TODO
            }

            if (component instanceof PlayerStatsComponent) {
                json.writeObjectEnd();
                continue;
            }

            if (component instanceof SerializationComponent) {
                json.writeValue("tag", ((SerializationComponent) component).entityName);
                json.writeObjectEnd();
                continue;
            }

            if (component instanceof ContainerComponent) {
                json.writeValue("name", ((ContainerComponent) component).name);
                json.writeArrayStart("itemList");
                for (InventoryItemLocation location : ((ContainerComponent) component).itemLocations) {
                  GameEntitySerializer.saveItems(json,location);
                }
                json.writeArrayEnd();
                json.writeObjectEnd();
                continue;
            }

            if (component instanceof BodyComponent) {
                Body body = ((BodyComponent) component).body;
                json.writeValue("bodyType", body.getType().name());
                json.writeValue("positionX", body.getPosition().x);
                json.writeValue("positionY", body.getPosition().y);
                json.writeValue("fixedRotation", body.isFixedRotation());
                json.writeValue("bullet", body.isBullet());

                json.writeArrayStart("Fixtures");
                for (Fixture fixture : body.getFixtureList()) {
                    json.writeObjectStart();
                    json.writeValue("shapeType", fixture.getType().name());
                    if (fixture.getType().equals(Shape.Type.Polygon)) {
                        PolygonShape shape = (PolygonShape) fixture.getShape();
                        json.writeArrayStart("polygon");
                        for (int i = 0; i < shape.getVertexCount(); i++) {
                            Vector2 vector2 = new Vector2();
                            shape.getVertex(i, vector2);
                            json.writeValue(vector2.x);
                            json.writeValue(vector2.y);
                        }
                        json.writeArrayEnd();
                    }
                    if (fixture.getType().equals(Shape.Type.Circle)) {
                        CircleShape shape = (CircleShape) fixture.getShape();
                        json.writeValue("radius", shape.getRadius());
                    }
                    json.writeValue("sensor", fixture.isSensor());
                    json.writeValue("destiny", fixture.getDensity());
                    json.writeValue("friction", fixture.getFriction());
                    json.writeValue("restitution", fixture.getRestitution());
                    json.writeValue("categoryBits", fixture.getFilterData().categoryBits);
                    json.writeValue("maskBits", fixture.getFilterData().maskBits);
                    if (fixture.getUserData() != null) {
                        json.writeValue("hasUserDate", true);
                        if (fixture.getUserData() instanceof BodyID) {
                            json.writeValue("userDate", fixture.getUserData());
                        }
                    } else json.writeValue("hasUserDate", false);

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

        if (jsonData.has("BodyComponent")) {

            BodyComponent bodyComponent = new BodyComponent();
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.valueOf(jsonData.get("BodyComponent").get("bodyType").asString());
            bodyComponent.body = world.createBody(bodyDef);
            bodyComponent.body.setFixedRotation(jsonData.get("BodyComponent").get("fixedRotation").asBoolean());
            bodyComponent.body.setBullet(jsonData.get("BodyComponent").get("bullet").asBoolean());
            bodyComponent.body.setUserData(entity);
            bodyComponent.body.setTransform(new Vector2(jsonData.get("BodyComponent").get("positionX").asFloat(), jsonData.get("BodyComponent").get("positionY").asFloat()), 0);
            entity.add(bodyComponent);
            for (JsonValue value : jsonData.get("BodyComponent").get("Fixtures")) {
                FixtureDef fixtureDef = new FixtureDef();

                if (value.has("shapeX") && value.has("shapeY")) {
                    PolygonShape shape = new PolygonShape();
                    shape.setAsBox(value.get("shapeW").asFloat() / 2, value.get("shapeH").asFloat() / 2,
                            new Vector2(value.get("shapeX").asFloat(), value.get("shapeY").asFloat()), value.get("shapeA").asFloat());
                    fixtureDef.shape = shape;
                }

                if (value.has("polygon")) {
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
                if(value.has("categoryBits")){fixtureDef.filter.categoryBits = value.get("categoryBits").asShort();}
                if(value.has("maskBits")){fixtureDef.filter.maskBits = value.get("maskBits").asShort();}
                if (value.get("hasUserDate").asBoolean()) {
                    bodyComponent.body.createFixture(fixtureDef).setUserData(BodyID.valueOf(value.get("userDate").asString()));
                } else bodyComponent.body.createFixture(fixtureDef);
                fixtureDef.shape.dispose();
            }
        }

        if (jsonData.has("HealthComponent")) {
            HealthComponent healthComponent = new HealthComponent();
            healthComponent.hp = jsonData.get("HealthComponent").get("hp").asInt();
            healthComponent.maxHP = jsonData.get("HealthComponent").get("maxHP").asInt();
            entity.add(healthComponent);
        }

        if (jsonData.has("TextureComponent")) {
            //TODO
            TextureComponent textureComponent = new TextureComponent();
            if (jsonData.get("TextureComponent").has("patch")) {
                textureComponent.texture = Annihilation.getAssets().get(jsonData.get("TextureComponent").get("patch").asString());
            }
            entity.add(textureComponent);
        }

        if (jsonData.has("SerializationComponent")) {
            SerializationComponent serializationComponent = new SerializationComponent();
            serializationComponent.entityName = jsonData.get("SerializationComponent").get("entityName").asString();
            entity.add(serializationComponent);
        }

        if (jsonData.has("GateComponent")) {
            GateComponent gateComponent = new GateComponent();
//            gateComponent.targetMapPath = jsonData.get("GateComponent").get("targetMapPath").asString();
//            gateComponent.playerPositionOnTargetMap.set(jsonData.get("GateComponent").get("positionX").asFloat(),jsonData.get("GateComponent").get("positionY").asFloat());
            entity.add(gateComponent);
        }

        if (jsonData.has("StateComponent")) {
            StateComponent stateComponent = new StateComponent();
            entity.add(stateComponent);
        }

        if (jsonData.has("ContainerComponent")) {
            ContainerComponent containerComponent = new ContainerComponent();
            containerComponent.name = jsonData.get("ContainerComponent").get("name").asString();
            containerComponent.itemLocations = new Array<>();
            for (JsonValue value : jsonData.get("ContainerComponent").get("itemList")) {
                InventoryItemLocation location = new InventoryItemLocation();
                location.setTableIndex(value.get("tableIndex").asInt());
                location.setItemID(value.get("itemID").asString());
                location.setItemsAmount(value.get("itemsAmount").asInt());
                containerComponent.itemLocations.add(location);
            }
            entity.add(containerComponent);
        }

        if (jsonData.has("ActionComponent")) {
            ActionComponent actionComponent = new ActionComponent();
            actionComponent.action = EntityAction.valueOf(jsonData.get("ActionComponent").get("action").asString());
            entity.add(actionComponent);
        }

        if (jsonData.has("PlayerComponent")) {
            PlayerComponent playerComponent = new PlayerComponent();
            entity.add(playerComponent);
        }

        if (jsonData.has("AnimationComponent")) {
            AnimationComponent animationComponent = new AnimationComponent();
            animationComponent.animationId = AnimationFactory.AnimationId.valueOf(jsonData.get("AnimationComponent").getString("id"));
            animationComponent.animationMap = animationFactory.createAnimationMap(animationComponent.animationId);
            entity.add(animationComponent);
        }

        if (jsonData.has("PlayerComponent")) {
            PlayerComponent playerComponent = new PlayerComponent();
            entity.add(playerComponent);

        }

        if (jsonData.has("PlayerInventoryComponent")) {
            PlayerInventoryComponent playerInventoryComponent = new PlayerInventoryComponent();
            InventoryItemLocation item = new InventoryItemLocation(0,InventoryItem.ItemID.FIRE_AXE.name(),1);

            playerInventoryComponent.inventoryItem.add(item);
            entity.add(playerInventoryComponent);
        }

        if (jsonData.has("PlayerStatsComponent")) {
            PlayerStatsComponent playerStatsComponent = new PlayerStatsComponent();
            entity.add(playerStatsComponent);

        }

        if (jsonData.has("AiComponent")) {
            AiComponent aiComponent = new AiComponent();
            entity.add(aiComponent);
        }

        if (engine != null) {
            engine.addEntity(entity);
        }
        return entity;
    }

}
