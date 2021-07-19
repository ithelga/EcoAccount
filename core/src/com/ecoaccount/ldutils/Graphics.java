package com.ecoaccount.ldutils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

/**
 * Created by Lion's DaMajectim on 24.12.2018
 * Updated on 27.08.2020
 */

public class Graphics {

    private static final Color[] colors = {Color.BLACK, Color.WHITE, Color.LIGHT_GRAY, Color.GRAY,
            Color.DARK_GRAY, Color.BLUE, Color.NAVY, Color.ROYAL, Color.SLATE, Color.SKY, Color.CYAN,
            Color.TEAL, Color.GREEN, Color.CHARTREUSE, Color.LIME, Color.FOREST, Color.OLIVE, Color.YELLOW,
            Color.GOLD, Color.GOLDENROD, Color.ORANGE, Color.BROWN, Color.TAN, Color.FIREBRICK, Color.RED,
            Color.SCARLET, Color.CORAL, Color.SALMON, Color.PINK, Color.MAGENTA, Color.PURPLE, Color.VIOLET, Color.MAROON
    };

    public static Texture getColorTexture(String hex) {
        return getColorTexture(Color.valueOf(hex));
    }

    public static TextureRegion getEmptyTextureRegion() {
        return new TextureRegion(getEmptyTexture());
    }

    public static Texture getEmptyTexture() {
        return getColorTexture(new Color(0, 0, 0, 0));
    }

    public static Texture getColorTexture(Color color) {
        Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pix.setColor(color);
        pix.fill();
        Texture texture = new Texture(pix);
        pix.dispose();
        return texture;
    }

    public static Color randomDefaultColor() {
        return colors[MathUtils.random(0, colors.length - 1)];
    }
}
