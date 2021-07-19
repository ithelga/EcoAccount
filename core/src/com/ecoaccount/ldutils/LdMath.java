package com.ecoaccount.ldutils;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Lion's DaMajectim on 23.05.2018
 * Updated on 07.05.2020
 */

public class LdMath {

    public static Vector2[] pointsWay(Vector2[] values, float period) {
        float T = 0.02f;
        int n = 0;
        for (float i = 0; i < 1; i += T) {
            n++;
        }
        Vector2[] finalPoints = new Vector2[n];
        float[] X = new float[values.length];
        float[] Y = new float[values.length];
        for(int i = 0; i < values.length; i++) {
            X[i] = values[i].x;
            Y[i] = values[i].y;
        }
        for (float t = 0, i = 0; t < 1; t += T, i++) {
            float x = point(X, t);
            float y = point(Y, t);

            finalPoints[(int) i] = new Vector2((int)x, (int)y);
        }
        return finalPoints;
    }

    private static float point(float[] values, float t) {
        float res = 0;
        for (int i = 0; i < values.length; i++) res += values[i] * polynom(i, values.length - 1, t);
        return res;
    }

    private static float polynom(int k, int n, float t) {
        float res = 1;

        int toNinC = 2;
        int toNminusKinC = 2;
        int toKinC = 2;

        int toNminusKinT = 1;
        int toKinT = 1;

        float threshold = 1e9f;

        while (toNinC <= n || toNminusKinC <= n - k || toKinC <= k || toNminusKinT <= n - k || toKinT <= k) {
            if (res > threshold || toNinC > n) {
                if (toNminusKinC <= n - k) {
                    res /= toNminusKinC++;
                    continue;
                }
                if (toKinC <= k) {
                    res /= toKinC++;
                    continue;
                }
                if (toNminusKinT <= n - k) {
                    res *= (1 - t);
                    toNminusKinT++;
                    continue;
                }
                if (toKinT <= k) {
                    res *= t;
                    toKinT++;
                    continue;
                }
            }
            if (toNinC <= n) res *= toNinC++;
        }
        return res;
    }

    private Vector2[] ellipse (float x, float y, float width, float height, int segments) {
        Vector2[] finalPoints = new Vector2[segments];
        float angle = 2 * MathUtils.PI / segments;
        float cx = x + width / 2, cy = y + height / 2;
        for (int i = 0; i < segments; i += 2) {
            finalPoints[i] = new Vector2(cx + (width * 0.5f * MathUtils.cos(i * angle)), cy + (height * 0.5f * MathUtils.sin(i * angle)));
            finalPoints[i + 1] = new Vector2(cx + (width * 0.5f * MathUtils.cos((i + 1) * angle)), cy + (height * 0.5f * MathUtils.sin((i + 1) * angle)));
        }
        return finalPoints;
    }

    public static double rint1(double x) { return ((double)(int)Math.round(x * 10)) / 10; }
    public static double rint2(double x) { return ((double)(int)Math.round(x * 100)) / 100; }
    public static double rint3(double x) { return ((double)(int)Math.round(x * 1000)) / 1000; }

    public static float rint1(float x) { return ((float) Math.round(x * 10)) / 10; }
    public static float rint2(float x) { return ((float) Math.round(x * 100)) / 100; }
    public static float rint3(float x) { return ((float) Math.round(x * 1000)) / 1000; }

    public static int[] resortArray(int[] array) {
        for (int i = array.length - 1, buf, j; i > 0; i--) {
            j = MathUtils.random(i);
            buf = array[j];
            array[j] = array[i];
            array[i] = buf;
        }
        return array;
    }

    public static float[] resortArray(float[] array) {
        float buf;
        for (int i = array.length - 1, j; i > 0; i--) {
            j = MathUtils.random(i);
            buf = array[j];
            array[j] = array[i];
            array[i] = buf;
        }
        return array;
    }

    public static boolean pointInRect(float pX, float pY, float rX, float rY, float rW, float rH) {
        return pX >= rX && pX <= rX + rW && pY >= rY && pY <= rY + rH;
    }

    public static float distanceBetweenPoints(float pX1, float pY1, float pX2, float pY2) {
        return (float) Math.sqrt(Math.pow((pX1 - pX2), 2) + Math.pow((pY1 - pY2), 2));
    }

    public static boolean containsRectInCircle(Circle c, Rectangle r) {
        return c.contains(r.x, r.y) && c.contains(r.x + r.width, r.y) && c.contains(r.x, r.y + r.height) && c.contains(r.x + r.width, r.y + r.height);
    }

    public static boolean containsOneRectPointInCircle(Circle c, Rectangle r) {
        return c.contains(r.x, r.y) || c.contains(r.x + r.width, r.y) || c.contains(r.x, r.y + r.height) || c.contains(r.x + r.width, r.y + r.height);
    }

    public static boolean isIntNumber(String str) {
        if (str == null || str.isEmpty()) return false;
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) return false;
        }
        return true;
    }

    public static int getNod(int a, int b) {
        return b == 0 ? a : getNod(b, a % b);
    }
}
