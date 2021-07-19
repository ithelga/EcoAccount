package com.ecoaccount.ldutils.animations;

import com.badlogic.gdx.math.MathUtils;

/**
 * Created by Lion's DaMajectim on 04.01.2018
 * Updated on 17.12.2018
 */

public class AnimAlpha {

    public static float A;
    public float a;

    private static boolean F;
    private boolean f;
    private float max, min;

    public AnimAlpha() {
        this(0, 1);
    }
    public AnimAlpha(float max) {
        this(0, max);
    }
    public AnimAlpha(float min, float max) {
        a = MathUtils.random(min, max);
        this.min = min;
        this.max = max;
    }

    public void update(float s) {
        a += f ? s:-s;
        if (a >= max || a <= min) {
            f = !f;
            a = a >= max ? max:min;
        }
    }

    public static void updateStatic(float s) {
        A += F ? s:-s;
        if (A >= 1 || A <= 0) {
            F = !F;
            A = A >= 1 ? 1:0;
        }
    }
}
