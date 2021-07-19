package com.ecoaccount.ldutils.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

/**
 * Created by Lion's DaMajectim on 28.03.2018
 * Updated on 31.05.2021
 */

public class LdText {

    public static final int F1 = 0;
    public static final int F2 = 1;
    public static final int F3 = 2;

    private static BitmapFont[][] fonts;
    private static float[][] fontSizes;

//    public static final int oCenter = 9999;
    public static BitmapFont oldFont, oldFont2;

    private static Affine2 affine;
    private static Matrix4 oldMatrix, transformMatrix;
    private static SpriteBatch batch;
    private static GlyphLayout bounds;
    private static float nowScale, nowScale2;

    public static void init(SpriteBatch spriteBatch) {
        init(spriteBatch, 1);
    }
    public static void init(SpriteBatch spriteBatch, int countTypes) {
        batch = spriteBatch;
        bounds = new GlyphLayout();
        affine = new Affine2();
        oldMatrix = new Matrix4();
        transformMatrix = new Matrix4();
        fonts = new BitmapFont[countTypes][0];
        fontSizes = new float[countTypes][0];
    }

    public static void add(String filePath, float size, float lineHeight) {
        add(filePath, size, lineHeight, F1);
    }
    public static void add(String filePath, float size, float lineHeight, int type) {
        BitmapFont font = new BitmapFont(Gdx.files.internal(filePath));
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        add(font, size, lineHeight, type);
    }
    public static void add(BitmapFont font, float size, float lineHeight) {
        add(font, size, lineHeight, F1);
    }
    public static void add(BitmapFont font, float size, float lineHeight, int type) {
        font.getData().setLineHeight(lineHeight);

        BitmapFont[] tempFont = fonts[type];
        fonts[type] = new BitmapFont[tempFont.length + 1];
        System.arraycopy(tempFont, 0, fonts[type], 0, tempFont.length);
        fonts[type][fonts[type].length - 1] = font;

        float[] tempSize = fontSizes[type];
        fontSizes[type] = new float[tempSize.length + 1];
        System.arraycopy(tempSize, 0, fontSizes[type], 0, tempSize.length);
        fontSizes[type][fontSizes[type].length - 1] = size;
    }

    @Deprecated
    public static void init(BitmapFont bitmapFont, SpriteBatch spriteBatch) {
        init(spriteBatch);
        oldFont = bitmapFont;
        setFontScale(1f);
    }

    @Deprecated
    public static void init(BitmapFont bitmapFont, BitmapFont bitmapFont2, SpriteBatch spriteBatch) {
        init(bitmapFont, spriteBatch);
        oldFont2 = bitmapFont2;
        setFontScale2(1f);
    }

    public static String numSpace(Object num) {
        String s = num.toString();
        for (int i = s.length(), j = 0; i > 0; i--) {
            if (s.charAt(i - 1) == '-') continue;
            j++;
            if (j == 4) {
                s = s.substring(0, i) + " " + s.substring(i);
                j = 0;
                i++;
            }
        }
        return s;
    }

    public static String floatNumSpace(Object num) { return floatNumSpace(num, 2); }
    public static String floatNumSpace(Object num, int count) {
        String s = num.toString();
        if (!s.contains(".")) s += ".00";
        String[] arr = s.split("\\.");
        if (arr[1].length() > count) arr[1] = arr[1].substring(0, count);
        while (arr[1].length() < count) arr[1] += "0";
        return numSpace(arr[0]) + "." + arr[1];
    }

    public static String zeros(Object num) { return zeros(num, 2); }
    public static String zeros(Object num, int outLen) {
        String s = num.toString();
        while (s.length() < outLen) s = "0" + s;
        return s;
    }

    public static String cropText(String text, float size, int maxWidth) {
        return cropText(text, size, maxWidth, F1);
    }
    public static String cropText(String text, float size, int maxWidth, int type) {
        String cropText = text;
        while (getWidth(cropText, size, type) > maxWidth) {
            text = text.substring(0, text.length() - 1);
            cropText = text.trim() + "...";
        }
        return cropText;
    }

    public static String format(String text, Object... objects) {
        for(Object object : objects) text = text.replaceFirst("@", object.toString());
        return text;
    }

    @Deprecated
    public static Vector2 getElementsPos(int allWidth, int width1, int width2, int space) {
        int x1, x2;
        x1 = (allWidth - (width1 + width2 + space)) / 2;
        x2 = x1 + width1 + space;
        return new Vector2(x1, x2);
    }

    public static int[] getElementsX(int allWidth, int width1, int width2, int space) {
        int[] arr = new int[2];
        arr[0] = (allWidth - (width1 + width2 + space)) / 2;
        arr[1] = arr[0] + width1 + space;
        return arr;
    }

    public static void draw(String text, float size, float x, float y, Color color) {
        draw(text, size, x, y, 0, color, F1, Align.left, false);
    }
    public static void draw(String text, float size, float x, float y, float w, Color color) {
        draw(text, size, x, y, w, color, F1, Align.left, false);
    }
    public static void draw(String text, float size, float x, float y, float w, Color color, int type) {
        draw(text, size, x, y, w, color, type, Align.left, false);
    }
    public static void draw(String text, float size, float x, float y, float w, Color color, int type, int align) {
        draw(text, size, x, y, w, color, type, align, false);
    }
    public static void draw(String text, float size, float x, float y, float w, Color color, int align, boolean wrap) {
        draw(text, size, x, y, w, color, F1, align, wrap);
    }
    public static void draw(String text, float size, float x, float y, float w, Color color, int type, int align, boolean wrap) {
        if (text == null || color == null || size == 0 || text.isEmpty()) return;
        int fontId = getBestFont(size, type);
        setFontSize(size, type, fontId);
        if (fonts[type][fontId].getColor() != color) fonts[type][fontId].setColor(color);
        fonts[type][fontId].draw(batch, text, x, y, w, align, wrap);
    }

    public static void drawCache(String text, float size, float x, float y, float w) {
        drawCache(text, size, x, y, w, F1, Align.left, false);
    }
    public static void drawCache(String text, float size, float x, float y, float w, int align, boolean wrap) {
        drawCache(text, size, x, y, w, F1, align, wrap);
    }
    public static void drawCache(String text, float size, float x, float y, float w, int type, int align, boolean wrap) {
        if (text == null || size == 0 || text.isEmpty()) return;
        int fontId = getBestFont(size, type);
        setFontSize(size, type, fontId);
        fonts[type][fontId].getCache().clear();
        fonts[type][fontId].getData().markupEnabled = true;
        fonts[type][fontId].getCache().setText(text, x, y, w, align, wrap);
        fonts[type][fontId].getData().markupEnabled = false;
        fonts[type][fontId].getCache().draw(batch);
    }

    private static int getBestFont(float size, int type) {
        int id = 0;
        float minScale = size / fontSizes[type][0];
        if (minScale == 1f) return id;
        for (int i = 1; i < fontSizes[type].length; i++) {
            float scale = size / fontSizes[type][i];
            if (Math.abs(minScale - 1) > Math.abs(scale - 1)) {
                minScale = scale;
                id = i;
            }
        }
        return id;
    }

    private static void setFontSize(float size, int type, int id) {
        size /= fontSizes[type][id];
        if (fonts[type][id].getData().scaleX == size || size == 0) return;
        fonts[type][id].getData().setScale(size);
    }

    public static float getLineHeight(float size) {
        return getLineHeight(size, F1);
    }
    public static float getLineHeight(float size, int type) {
        return fonts[type][getBestFont(size, type)].getData().lineHeight;
    }
    public static void setLineHeight(float height, float size) {
        setLineHeight(height, size, F1);
    }
    public static void setLineHeight(float height, float size, int type) {
        fonts[type][getBestFont(size, type)].getData().setLineHeight(height);
    }

    public static int getWidth(String text, float size) {
        return getWidth(text, size, F1);
    }
    public static int getWidth(String text, float size, int type) {
        if (text == null || size == 0 || text.isEmpty()) return 0;
        int fontId = getBestFont(size, type);
        setFontSize(size, type, fontId);
        bounds.reset();
        bounds.setText(fonts[type][fontId], text);
        return (int) bounds.width;
    }

    public static int getHeight(String text, float size, int width, boolean wrap) {
        return getHeight(text, size, width, wrap, F1);
    }
    public static int getHeight(String text, float size, int width, boolean wrap, int type) {
        if (text == null || size == 0 || text.isEmpty()) return 0;
        int fontId = getBestFont(size, type);
        setFontSize(size, type, fontId);
        bounds.reset();
        bounds.setText(fonts[type][fontId], text, Color.WHITE, width, Align.center, wrap);
        return (int) bounds.height;
    }


    @Deprecated
    public static void drawText(String text, float scale, float X, float Y, float W, Color color) {
        if (text == null || color == null || scale == 0) return;
        setFontScale(scale);
        if (oldFont.getColor() != color) oldFont.setColor(color);
        if (W == 0) oldFont.draw(batch, text, X, Y);
        else if (W == 1) oldFont.draw(batch, text, X, Y, W, Align.right, false);
        else oldFont.draw(batch, text, X, Y, W, Align.center, W % 2 == 1);
    }

    @Deprecated
    public static void drawText2(String text, float scale, float X, float Y, float W, Color color) {
        if (text == null || color == null || scale == 0) return;
        setFontScale2(scale);
        if (oldFont2.getColor() != color) oldFont2.setColor(color);
        if (W == 0) oldFont2.draw(batch, text, X, Y);
        else if (W == 1) oldFont2.draw(batch, text, X, Y, W, Align.right, false);
        else oldFont2.draw(batch, text, X, Y, W, Align.center, W % 2 == 1);
    }

    @Deprecated
    public static void drawText(String text, float scale, float X, float Y, float W, int align, boolean wrap, Color color) {
        if (text == null || color == null || scale == 0) return;
        setFontScale(scale);
        if (oldFont.getColor() != color) oldFont.setColor(color);
        oldFont.draw(batch, text, X, Y, W, align, wrap);
    }

    @Deprecated
    public static void drawText(String text, float scale, float x, float y, float w, float oX, float oY, float angle, int align, boolean wrap, Color color) {
        if (text == null || color == null || scale == 0) return;
        setFontScale(scale);
        if (oldFont.getColor() != color) oldFont.setColor(color);
//        if (oX == oCenter) oX = x + textWidth(text, scale) / 2;
//        if (oY == oCenter) {
//            int width = (int) w;
//            if (width == 0) width = textWidth(text, scale);
//            oY = y - textHeight(text, scale, width, wrap) / 2;
//        }
        oldMatrix.set(batch.getTransformMatrix());
        affine.setToTrnRotScl(oX, oY, angle, 1, 1);
        if (oX != 0 || oY != 0) affine.translate(-oX, -oY);
        transformMatrix.set(affine);
        batch.setTransformMatrix(transformMatrix);
        oldFont.draw(batch, text, x, y, w, align, wrap);
        batch.setTransformMatrix(oldMatrix);
    }

    @Deprecated
    public static void drawBlinkText(String text, float scale, float X, float Y, float W, Color color, float alpha) {
        drawText(text, scale, X, Y, W, new Color(color.r, color.g, color.b, alpha));
    }

    @Deprecated
    public static void drawTextCache(String text, float scale, float X, float Y, float W) {
        if (text == null || scale == 0) return;
        setFontScale(scale);
        oldFont.getCache().clear();
        oldFont.getData().markupEnabled = true;
        if (W == 0) oldFont.getCache().setText(text, X, Y);
        else if (W == 1) oldFont.getCache().setText(text, X, Y, W, Align.right, false);
        else oldFont.getCache().setText(text, X, Y, W, Align.center, W % 2 == 1);
        oldFont.getData().markupEnabled = false;
        oldFont.getCache().draw(batch);
    }

    @Deprecated
    public static void drawTextCache(String text, float scale, float X, float Y, float W, int align, boolean wrap) {
        if (text == null || scale == 0) return;
        setFontScale(scale);
        oldFont.getCache().clear();
        oldFont.getData().markupEnabled = true;
        oldFont.getCache().setText(text, X, Y, W, align, wrap);
        oldFont.getData().markupEnabled = false;
        oldFont.getCache().draw(batch);
    }
    @Deprecated
    public static int textWidth(String text, float scale) {
        if (text == null || scale == 0) return 0;
        bounds.reset();
        setFontScale(scale);
        bounds.setText(oldFont, text);
        return (int) bounds.width;
    }
    @Deprecated
    public static int textHeight(String text, float scale, int width, boolean wrap) {
        if (text == null || scale == 0) return 0;
        bounds.reset();
        setFontScale(scale);
        bounds.setText(oldFont, text, Color.WHITE, width, Align.center, wrap);
        return (int) bounds.height;
    }

    @Deprecated
    private static void setFontScale(float scale) {
        if (nowScale == scale || scale == 0) return;
        oldFont.getData().setScale(scale);
        nowScale = scale;
    }

    @Deprecated
    private static void setFontScale2(float scale) {
        if (nowScale2 == scale || scale == 0) return;
        oldFont2.getData().setScale(scale);
        nowScale2 = scale;
    }
}