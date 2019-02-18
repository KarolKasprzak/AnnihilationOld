package com.cosma.annihilation.Editor;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.cosma.annihilation.Annihilation;

public class Tile implements Json.Serializable {
    private TextureRegion textureRegion;
    private String atlasRegionName;
    private String atlasPath;

    public TextureRegion getTextureRegion() {
        return textureRegion;
    }

    public void setTextureRegion(TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
    }

    public void setTextureRegion(String region, String path) {
        this.textureRegion = Annihilation.getAssets().get(path,TextureAtlas.class).findRegion(region);
        this.atlasPath = path;
        this.atlasRegionName = region;
    }

    public void setAtlasRegionName(String atlasRegionName) {
        this.atlasRegionName = atlasRegionName;
    }

    public void setAtlasPath(String atlasPath) {
        this.atlasPath = atlasPath;
    }

    public Tile(){

    }

    @Override
    public void write(Json json) {
        json.writeValue("atlasRegionName", atlasRegionName);
        json.writeValue("atlasPath",atlasPath);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        atlasRegionName = jsonData.getString("atlasRegionName");
        atlasPath = jsonData.getString("atlasPath");
        textureRegion = Annihilation.getAssets().get(atlasPath,TextureAtlas.class).findRegion(atlasRegionName);
    }
}
