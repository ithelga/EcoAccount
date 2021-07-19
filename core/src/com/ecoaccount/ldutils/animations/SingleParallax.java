package com.ecoaccount.ldutils.animations;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Lion's DaMajectim on 16.12.2018
 * Updated on 05.05.2019
 */

public class SingleParallax {

    private TextureRegion texture;
    private float x, y, rX;
    private int w, h, regX;

    public SingleParallax(TextureRegion texture, float x, float y, int w, int h) {
        this.texture = new TextureRegion(texture);
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        rX = 0;
        regX = texture.getRegionX();
    }

    public void updateDraw(SpriteBatch batch, float speed) {
        update(speed);
        draw(batch);
    }

    public void update(float speed) {
        rX += speed;
        if (rX >= w) rX -= w;
        else if (rX < 0) rX += w;
    }

    public void draw(SpriteBatch batch) {
        texture.setRegionX(regX);
        texture.setRegionWidth((w - (int)rX));
        batch.draw(texture, x + rX, y, w - rX + 1, h);

        texture.setRegionX(regX + (w - (int)rX));
        texture.setRegionWidth((int) rX);
        batch.draw(texture, x, y, rX + 1, h);
    }

    public void flip(boolean x, boolean y) { texture.flip(x, y); }
    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }
    public void setPos(float x, float y) { setX(x); setY(y); }
    public float getX() { return x; }
    public float getY() { return y; }
}
