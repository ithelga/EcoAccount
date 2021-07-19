package com.ecoaccount.ldutils;

/**
 * Created by Lion's DaMajectim on 30.10.2020
 * Updated on 30.10.2020
 */

public class Ld {

    public static boolean in(Object value, Object... objects) {
        for (Object object : objects) {
            if (value.equals(object)) return true;
        }
        return false;
    }

}
