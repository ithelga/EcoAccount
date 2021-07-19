package com.ecoaccount.ldutils;

import com.badlogic.gdx.Gdx;

/**
 * Created by Lion's DaMajectim on 17.02.2018
 * Updated on 01.20.2020
 */

public class LdDelta {

    private static final float FPS_MIN = 10;
    private static final float FPS_MIN_DELTA = 1f / FPS_MIN;

    public static float getDelta() {
        float delta = Gdx.graphics.getDeltaTime();
        if (delta > FPS_MIN_DELTA) delta = FPS_MIN_DELTA;
        return delta;
    }
}