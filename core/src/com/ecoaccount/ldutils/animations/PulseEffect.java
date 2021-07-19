package com.ecoaccount.ldutils.animations;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Lion's DaMajectim on 06.03.2019
 * Updated on 31.01.2020
 */

public class PulseEffect {

    private Color cl;
    private TextureRegion texture;
    private boolean loop;
    private float time;
    private float x, y, w, h;
    private int n;

    public PulseEffect(TextureRegion texture, int n, float x, float y, float w, float h, boolean loop) {
        this.texture = texture;
        this.loop = loop;
        this.n = n;
        setX(x);
        setY(y);
        this.w = w;
        this.h = h;
        if (!loop) time = 10;
        cl = Color.WHITE;
    }

    public void update(float speed) {
        time += speed;
        if (loop && time > 1) time -= 1;
    }

    public void draw(SpriteBatch batch) {
        for(int i = 0; i < n; i++) {
            float s = time - (1f / n) * i;
            if (loop && s < 0) s += 1;
            else if (!loop && (s < 0 || s > 1)) continue;
            batch.setColor(cl.r, cl.g, cl.b, s <= 0.8f ? 1:(1 - (s - 0.8f) * 5));
            batch.draw(texture, x - w * s / 2, y - h * s / 2, w * s, h * s);
        }
        batch.setColor(Color.WHITE);
    }

    public void draw(SpriteBatch batch, float speed) {
        update(speed);
        draw(batch);
    }

    public void setColor(Color color) {
        cl = color;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setPosition(float x, float y) {
        setX(x);
        setY(y);
    }

    public float getWidth() {return w;}
    public float getHeight() {return h;}

    public void reset() {
        time = 0;
    }

    public boolean isFinished() {
        return !loop && ((time - (1f / n) * (n - 1)) > 1);
    }

    public void end() {
        time = 10;
    }
}
