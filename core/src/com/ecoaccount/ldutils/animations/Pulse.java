package com.ecoaccount.ldutils.animations;

/**
 * Created by Lion's DaMajectim on 11.02.2019
 * Updated on 24.02.2019
 */

public class Pulse {
    public float s;
    public void update(float delta) { update(delta * 0.5f, 1.15f, true); }
    public void update(float delta, boolean active) { update(delta * 0.5f, 1.15f, active); }
    public void update(float speed, float max) { update(speed, max, true); }
    public void update(float speed, float max, boolean active) {
        if (s > 1) s -= speed;
        else s = active ? max : 1;
    }

    public void updateReverse(float speed, float max) {
        if (s < max) s += speed;
        else s = 1;
    }
}