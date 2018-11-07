package com.cosma.annihilation.Utils;

import com.badlogic.gdx.graphics.Texture;
import com.cosma.annihilation.Utils.Enums.BodyID;
import net.dermetfan.gdx.graphics.g2d.Box2DSprite;

public class FixtureUserDate {
    public Box2DSprite texture;
    public BodyID bodyID;

    public FixtureUserDate(Box2DSprite texture, BodyID bodyID) {
        this.texture = texture;
        this.bodyID = bodyID;
    }

    public Box2DSprite getTexture() {
        return texture;
    }

    public void setTexture(Box2DSprite texture) {
        this.texture = texture;
    }

    public BodyID getBodyID() {
        return bodyID;
    }

    public void setBodyID(BodyID bodyID) {
        this.bodyID = bodyID;
    }
}
