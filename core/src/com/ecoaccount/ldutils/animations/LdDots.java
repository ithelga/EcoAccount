package com.ecoaccount.ldutils.animations;

/**
 * Created by Lion's DaMajectim on 27.04.2020
 * Updated on 27.04.2020
 */

public class LdDots {

    private float t;

    public LdDots() {

    }

    public String getDots(float delta) {
        t += delta;
        if (t > 4) t = 0;

        if (t < 1) return "";
        else if (t < 2) return ".";
        else if (t < 3) return "..";
        else return "...";
    }
}
