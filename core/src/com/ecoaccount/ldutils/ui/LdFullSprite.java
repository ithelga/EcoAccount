package com.ecoaccount.ldutils.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Lion's DaMajectim on 19.06.2020
 * Updated on 18.11.2020
 */

public class LdFullSprite {

    public static final int centerBottom = 0;
    public static final int center = 1;

    private TextureRegion texture;
    private float x, y, oX, oY, width, height, scale;
    private int align;

    public LdFullSprite(TextureRegion texture) {
        this(new TextureRegion(texture), 0, 0, 0, 0);
    }
    public LdFullSprite(Texture texture, float x, float y, float width, float height) {
        this(new TextureRegion(texture), x, y, width, height);
    }
    public LdFullSprite(TextureRegion texture, float x, float y, float width, float height) {
        this.texture = texture;
        this.texture.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        this.height = texture.getRegionHeight();
        this.width = texture.getRegionWidth();
        align = centerBottom;
        update(x, y, width, height);
    }

    public void update(float x, float y, float width, float height) {
        if (align == centerBottom) {
            oX = x + (width - this.width) / 2;
            oY = y;
        }
        else if (align == center) {
            oX = x + (width - this.width) / 2;
            oY = y + (height - this.height) / 2;
        }
        float scaleX = width / this.width;
        float scaleY = height / this.height;
        scale = scaleX > scaleY ? scaleX : scaleY;
    }

    public void setAlign(int align) {
        this.align = align;
    }

    public void setPosition(float x, float y) { this.x = x; this.y = y; }
    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }
    public TextureRegion getTexture() { return texture; }

    public void draw(SpriteBatch batch) {
        if (align == centerBottom) batch.draw(texture, x + oX, y + oY, width / 2, 0, width, height, scale, scale, 0);
        else if (align == center) batch.draw(texture, x + oX, y + oY, width / 2, height / 2, width, height, scale, scale, 0);
    }
}
