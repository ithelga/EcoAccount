package com.ecoaccount.ldutils.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.ecoaccount.ldutils.Graphics;

/**
 * Created by Lion's DaMajectim on 10.07.2018
 * Updated on 30.05.2021
 */

public class LdLinkButton extends Actor {

    private Color clNorm, clPres;
    private String text;
    private Texture txNorm, txPres;
    private float x, y, size, lineHeight;
    private int fontType;

    public LdLinkButton(String text, float size, Color clNorm, Color clPres, float x, float y) {
        this.size = size;
        this.clNorm = clNorm;
        this.clPres = clPres;
        this.x = x;
        this.y = y;
        fontType = LdText.F1;
        txNorm = Graphics.getColorTexture(clNorm);
        txPres = Graphics.getColorTexture(clPres);
        setText(text == null ? "":text);
        setLineHeight(4);
    }

    public LdLinkButton(String text, float size, Color clNorm, Color clPres, float x, float y, EventListener listener) {
        this(text, size, clNorm, clPres, x, y);
        addListener(listener);
    }

    public void setText(String text) {
        this.text = text;
        changeBounds();
    }

    public void setFontType(int type) {
        fontType = type;
    }

    public void setLineHeight(float height) {
        lineHeight = height;
    }

    public void setNewX(float x) {
        this.x = x;
        changeBounds();
    }

    public void setNewY(float y) {
        this.y = y;
        changeBounds();
    }

    public void setNewPosition(float x, float y) {
        this.x = x;
        this.y = y;
        changeBounds();
    }

    private void changeBounds() {
        int width = LdText.getWidth(text, size, fontType) + 30;
        int height = LdText.getHeight(text, size, width, true, fontType) + 30;
        setBounds(x - width / 2f, y - 10, width, height);
    }

    public void draw(SpriteBatch batch) {
        if (!isVisible()) return;
        boolean isPressed = ((ClickListener)this.getListeners().first()).isVisualPressed();
        LdText.draw(text, size, getX(), getTop() - 10, getWidth(), isPressed ? clPres : clNorm, fontType, Align.center, false);
        batch.draw(isPressed ? txPres:txNorm, getX() + 15, getY() + 10, getWidth() - 30, lineHeight);
    }
}
