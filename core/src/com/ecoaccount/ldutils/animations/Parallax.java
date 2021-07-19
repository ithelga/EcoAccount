package com.ecoaccount.ldutils.animations;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

/**
 * Created by Lion's DaMajectim on 15.12.2018
 * Updated on 26.12.2019
 */


public class Parallax {

    private TextureRegion texture;
    private boolean firstFlip, flipped;
    private float x, y, w, h, mX, oX;
    private int count;

    public Parallax(TextureRegion texture, float y, float w, float h, float mX, float width, boolean flipped) {
        this.texture = texture;
        this.y = y;
        this.w = w;
        this.h = h;
        this.mX = mX;
        this.flipped = flipped;

        x = -(w - mX);
        count = MathUtils.ceil(width / (w - mX)) + 1;
    }

    public void draw(SpriteBatch batch, float delta, float speed) {
        x += speed * delta;

        if (x >= 0) {
            x -= (w - mX);
            if (flipped) firstFlip = !firstFlip;
        }
        else if (x <= -(w - mX)) {
            x += (w - mX);
            if (flipped) firstFlip = !firstFlip;
        }

        for(int i = 0; i < count; i++) {
            if (flipped) texture.flip((i % 2 == 0 && firstFlip) || (i % 2 != 0 && !firstFlip), false);
            batch.draw(texture, x + oX + (w - mX) * i, y, w, h);
            if (flipped) texture.flip((i % 2 == 0 && firstFlip) || (i % 2 != 0 && !firstFlip), false);
        }
    }

    public void setOX(float oX) { this.oX = oX; }
    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }
    public void setPos(float x, float y) { setX(x); setY(y); }

    public TextureRegion getTexture() {
        return texture;
    }
}
