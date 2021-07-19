package com.ecoaccount.ldutils.animations;

import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Lion's DaMajectim on 25.07.2019
 * Updated on 25.10.2019
 */

public class Follow {

    private Rectangle path;
    private float t, x, y;

    public Follow(float x, float y, float width, float height) {
        this(new Rectangle(x, y, width, height));
    }
    public Follow(Rectangle path) {
        this.path = path;
        t = 0;
    }

    public void update(float delta) {
        t += delta;
        if (t > 1) t -= 1;

        if (t <= 0.25f) {
            x = path.x;
            y = path.y + path.height * t * 4;
        }
        else if (t <= 0.5f) {
            x = path.x + path.width * (t - 0.25f) * 4;
            y = path.y + path.height;
        }
        else if (t <= 0.75f) {
            x = path.x + path.width;
            y = path.y + path.height * (1 - (t - 0.5f) * 4);
        }
        else {
            x = path.x + path.width * (1 - (t - 0.75f) * 4);
            y = path.y;
        }
    }

    public void setX(float x) {path.x = x;}
    public void setY(float y) {path.y = y;}

    public float getX() {return x;}
    public float getY() {return y;}
}
