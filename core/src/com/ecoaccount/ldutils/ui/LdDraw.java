package com.ecoaccount.ldutils.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Lion's DaMajectim on 30.05.2020
 * Updated on 30.05.2020
 */

public class LdDraw {

    private static SpriteBatch batch;

    public static void init(SpriteBatch spriteBatch) {
        batch = spriteBatch;
    }

    public static void draw(Texture texture, float x, float y) {
        batch.draw(texture, x, y);
    }

    public static void draw(TextureRegion texture, float x, float y) {
        batch.draw(texture, x, y);
    }

    public static void draw(TextureRegion texture, Actor bounds) {
        batch.draw(texture, bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
    }

}
