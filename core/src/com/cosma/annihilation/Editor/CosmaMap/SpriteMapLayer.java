package com.cosma.annihilation.Editor.CosmaMap;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.cosma.annihilation.Annihilation;

public class SpriteMapLayer extends MapLayer implements Json.Serializable{

    private Array<Sprite> spriteArray = new Array<>();

    public SpriteMapLayer(int width, int height, String name) {
        super(width, height, name);
    }

    public SpriteMapLayer() {
    }

    public Array<Sprite> getSpriteArray() {
        return spriteArray;
    }

    @Override
    public void write(Json json) {
        json.writeValue("name", this.getName());
        json.writeValue("height", this.getHeight());
        json.writeValue("width", this.getWidth());
        json.writeArrayStart("sprites");
        for(Sprite sprite: spriteArray){
            json.writeObjectStart();
            json.writeValue("position",sprite.getX()+","+sprite.getY()+","+sprite.getAngle());
            json.writeValue("texture",sprite.getTextureDate());
            json.writeObjectEnd();
        }
        json.writeArrayEnd();
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        this.setName(jsonData.get("name").asString());
        this.setHeight(jsonData.get("height").asInt());
        this.setWidth(jsonData.get("width").asInt());
        for(JsonValue value: jsonData.get("sprites")){
            Sprite sprite = new Sprite();
            String texture = value.get("texture").asString();
            String position = value.get("position").asString();
            sprite.setTextureRegion(texture.split(",")[1],texture.split(",")[0]);
            sprite.setSpritePosition(Float.parseFloat(position.split(",")[0]),Float.parseFloat(position.split(",")[1]),Float.parseFloat(position.split(",")[2]));

        }
    }
}
