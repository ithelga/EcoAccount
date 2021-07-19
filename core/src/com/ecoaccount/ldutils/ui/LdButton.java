package com.ecoaccount.ldutils.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.ecoaccount.ldutils.LdDelta;
import com.ecoaccount.ldutils.LdMask;

/**
 * Created by Lion's DaMajectim on 28.03.2018
 * Updated on 12.06.2021
 */

public class LdButton extends Actor {

    public static final int STATE_1 = 1;
    public static final int STATE_2 = 2;
    public static final int STATE_3 = 3;
    public static final int STATE_4 = 4;
    public static final int STATE_5 = 5;

    public static float LIM_PRES = 0.5f;

    private Color color, pressColor;
    private Sprite sprite;
    private NinePatch ninePatch;
    private Array<Element> elements;
    private int state;
    private float aPres, limPres;
    private boolean isActive, isMoveCancel, isTextureVisible, isPress;

    public class Element {

        public static final int FONT_1 = 0;
        public static final int FONT_2 = 1;
        public static final int FONT_CACHE = 2;

        private Color color, pressColor;
        private String text;
        private NinePatch ninePatch;
        private TextureRegion texture;
        private boolean scaled, isFront, isBack, isShadow, isMask, flipX, flipY, isCache, textWrap, visible = true;
        private float x, y, a, s, width, height, size, mX, mY, mW, mH;
        private int ste, fontState, textAlign, fontType;

        private boolean isDeprecatedText;

        public float scale;

        @Deprecated
        private Element(String text, float scale, float x, float y, float width, float height, Color color, boolean scaled, int state) {
            isDeprecatedText = true;
            this.text = text;
            this.scaled = scaled;
            this.scale = scale;
            this.x = x;
            if (scaled) {
                this.y = height / 2;
                this.height = LdText.textHeight(text, scale, (int) width, true) / 2;
            } else {
                this.y = height - (height - LdText.textHeight(text, scale, (int) width, true)) / 2;
                this.height = height;
            }
            this.width = width;
            this.color = color;
            ste = state;
        }

        @Deprecated
        private Element(String text, float scale, float x, float y, float width, Color color, int state) {
            isDeprecatedText = true;
            this.text = text;
            this.scale = scale;
            this.x = x;
            this.y = y;
            this.width = width;
            this.color = color;
            ste = state;
        }


        private Element(String text, float size, float x, float y, float width, float height, Color color, int type, int align, boolean wrap, boolean scaled, int state) {
            this.text = text;
            this.size = size;
            this.scaled = scaled;
            this.x = x;
            this.width = width;
            this.color = color;
            textAlign = align;
            textWrap = wrap;
            fontType = type;
            if (scaled) {
                this.y = height / 2;
                this.height = LdText.getHeight(text, size, (int) width, true, fontType) / 2;
            } else {
                this.y = height - (height - LdText.getHeight(text, size, (int) width, true, fontType)) / 2;
                this.height = height;
            }
            ste = state;
        }

        private Element(String text, float size, float x, float y, float width, Color color, int type, int align, boolean wrap, int state) {
            this.text = text;
            this.size = size;
            this.x = x;
            this.y = y;
            this.width = width;
            this.color = color;
            textAlign = align;
            textWrap = wrap;
            fontType = type;
            ste = state;
        }

        private Element(TextureRegion texture, float x, float y, float width, float height, float rotation, int state) {
            this.scale = 1;
            this.texture = texture;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            ste = state;
            s = rotation;
        }

        private Element(NinePatch ninePatch, float x, float y, float width, float height, int state) {
            this.scale = 1;
            this.ninePatch = ninePatch;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            ste = state;
            s = 0;
        }

        private void drawSimp(SpriteBatch batch, boolean isPress) {
            if (!isFront && !isBack) draw(batch, isPress);
        }

        private void drawBack(SpriteBatch batch, boolean isPress) {
            if (isBack) draw(batch, isPress);
        }

        private void drawFront(SpriteBatch batch, boolean isPress) {
            if (isFront) draw(batch, isPress);
        }

        private void draw(SpriteBatch batch, boolean isPress) {
            if ((state != ste && ste != 0) || !visible) return;
            if (text != null) drawText();
            else if (texture != null) drawTexture(batch, isPress);
            else if (ninePatch != null) drawNinePatch(batch, isPress);
        }

        private void drawText() {
            if (isDeprecatedText) {
                if (fontState == FONT_1) {
                    if (scaled)
                        LdText.drawText(text, scale * getSpriteScale(), sprite.getX() + x, sprite.getY() + y + height * getSpriteScale(), width, (isPress && pressColor != null) ? pressColor : color);
                    else
                        LdText.drawText(text, scale, sprite.getX() + x, sprite.getY() + y, width, (isPress && pressColor != null) ? pressColor : color);
                } else if (fontState == FONT_2) {
                    if (scaled)
                        LdText.drawText2(text, scale * getSpriteScale(), sprite.getX() + x, sprite.getY() + y + height * getSpriteScale(), width, (isPress && pressColor != null) ? pressColor : color);
                    else
                        LdText.drawText2(text, scale, sprite.getX() + x, sprite.getY() + y, width, (isPress && pressColor != null) ? pressColor : color);
                } else if (fontState == FONT_CACHE) {
                    if (scaled)
                        LdText.drawTextCache(text, scale * getSpriteScale(), sprite.getX() + x, sprite.getY() + y + height * getSpriteScale(), width);
                    else
                        LdText.drawTextCache(text, scale, sprite.getX() + x, sprite.getY() + y, width);
                }
            } else {
                if (isCache) {
                    if (scaled)
                        LdText.drawCache(text, size * getSpriteScale(), sprite.getX() + x, sprite.getY() + y + height * getSpriteScale(), width, fontType, textAlign, textWrap);
                    else
                        LdText.drawCache(text, size, sprite.getX() + x, sprite.getY() + y, width, fontType, textAlign, textWrap);
                } else {
                    if (scaled)
                        LdText.draw(text, size * getSpriteScale(), sprite.getX() + x, sprite.getY() + y + height * getSpriteScale(), width, (isPress && pressColor != null) ? pressColor : color, fontType, textAlign, textWrap);
                    else
                        LdText.draw(text, size, sprite.getX() + x, sprite.getY() + y, width, (isPress && pressColor != null) ? pressColor : color, fontType, textAlign, textWrap);
                }
            }
        }

        private void drawTexture(SpriteBatch batch, boolean isPress) {
            if (s != 0) a += s * LdDelta.getDelta();
            if (isPress && pressColor != null) batch.setColor(pressColor);
            else if (color != null) batch.setColor(color);

            if (isMask) {
                LdMask.begin(batch);
                LdMask.setRect(sprite.getX() + x + mX, sprite.getY() + y + mY, mW, mH);
                LdMask.flush();
            }
            if (flipX || flipY) texture.flip(flipX, flipY);
            batch.draw(texture, sprite.getX() + x, sprite.getY() + y, width / 2, height / 2, width, height, scale, scale, a);
            if (flipX || flipY) texture.flip(flipX, flipY);
            if (isMask) LdMask.end();
            if (color != null || (isPress && pressColor != null)) batch.setColor(Color.WHITE);

            if (isShadow && aPres > 0) {
                batch.setColor(0, 0, 0, aPres);
                if (flipX || flipY) texture.flip(flipX, flipY);
                batch.draw(texture, sprite.getX() + x, sprite.getY() + y, width / 2, height / 2, width, height, scale, scale, a);
                if (flipX || flipY) texture.flip(flipX, flipY);
                batch.setColor(1, 1, 1, 1);
            }
        }

        private void drawNinePatch(SpriteBatch batch, boolean isPress) {
            if (s != 0) a += s * LdDelta.getDelta();
            if (isPress && pressColor != null) batch.setColor(pressColor);
            else if (color != null) batch.setColor(color);

            if (isMask) {
                LdMask.begin(batch);
                LdMask.setRect(sprite.getX() + x + mX, sprite.getY() + y + mY, mW, mH);
                LdMask.flush();
            }
            ninePatch.draw(batch, sprite.getX() + x, sprite.getY() + y, width / 2, height / 2, width, height, scale, scale, a);
            if (isMask) LdMask.end();
            if (color != null || (isPress && pressColor != null)) batch.setColor(Color.WHITE);

            if (isShadow && aPres > 0) {
                batch.setColor(0, 0, 0, aPres);
                ninePatch.draw(batch, sprite.getX() + x, sprite.getY() + y, width / 2, height / 2, width, height, scale, scale, a);
                batch.setColor(1, 1, 1, 1);
            }
        }

        public Element setFlip(boolean flipX, boolean flipY) {
            this.flipX = flipX;
            this.flipY = flipY;
            return this;
        }

        @Deprecated
        public Element setFontState(int fontState) {
            this.fontState = fontState;
            return this;
        }

        public Element setDrawCache(boolean isCache) {
            this.isCache = isCache;
            return this;
        }

        public Element setFontType(int type) {
            fontType = type;
            return this;
        }

        public Element setColor(Color color) {
            this.color = color;
            return this;
        }

        public Element setPressColor(Color pressColor) {
            this.pressColor = pressColor;
            return this;
        }

        public void setVisible(boolean visible) {
            this.visible = visible;
        }

        public Element setText(String text) {
            this.text = text;
            if (height > 0) {
                if (scaled)
                    this.height = LdText.getHeight(text, size, (int) width, true, fontType) / 2;
                else
                    this.y = height - (height - LdText.getHeight(text, size, (int) width, true, fontType)) / 2;
            }
            return this;
        }

        public Element setBack(boolean isBack) {
            this.isBack = isBack;
            return this;
        }

        public Element setFront(boolean isFront, boolean isShadow) {
            this.isFront = isFront;
            this.isShadow = isShadow;
            return this;
        }

        public void setRectMask(float x, float y, float width, float height) {
            isMask = true;
            mX = x;
            mY = y;
            mW = width;
            mH = height;
        }

        public void setTexture(TextureRegion texture) {
            this.texture = texture;
        }

        public Color getColor() {
            return color;
        }

        public String getText() {
            return text;
        }

        public TextureRegion getTexture() {
            return texture;
        }

        @Override
        public String toString() {
            return "Element (" + x + ", " + y + ", " + width + ", " + height + ")";
        }
    }

    public LdButton(TextureRegion image, float x, float y, float width, float height) {
        sprite = new Sprite(image);
        setBounds(x, y, width, height);
        sprite.setBounds(x, y, width, height);
        sprite.setOriginCenter();

        limPres = LIM_PRES;
        elements = new Array<>();
        isActive = true;
        isTextureVisible = true;
    }

    public LdButton(TextureRegion image, float x, float y, float width, float height, EventListener listener) {
        this(image, x, y, width, height);
        addListener(listener);
    }

    public void setTexture(TextureRegion texture) {
        sprite.setRegion(texture);
        ninePatch = null;
    }

    public void setNinePath(TextureRegion texture, int left, int right, int top, int bottom) {
        ninePatch = new NinePatch(texture, left, right, top, bottom);
    }

    public void setNinePath(NinePatch patch) {
        ninePatch = patch;
    }

    public void deactiveNinePatch() {
        ninePatch = null;
    }

    public void setTextureVisible(boolean visible) {
        isTextureVisible = visible;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setPressColor(Color pressColor) {
        this.pressColor = pressColor;
    }

    public void resetPress() {
        ((ClickListener) this.getListeners().first()).cancel();
        aPres = 0;
    }

    public void visualPress() {
        aPres = limPres;
    }

    @Deprecated
    public Element addTextElement(String text, float scale, float x, float y, float width, Color color, int state) {
        elements.add(new Element(text, scale, x, y, width, color, state));
        return elements.get(elements.size - 1);
    }

    @Deprecated
    public Element addTextElement(String text, float scale, float x, float y, float width, Color color) {
        elements.add(new Element(text, scale, x, y, width, color, 0));
        return elements.get(elements.size - 1);
    }

    @Deprecated
    public Element addTextElement(String text, float scale, Color color, int state) {
        elements.add(new Element(text, scale, 0, 0, sprite.getWidth(), sprite.getHeight(), color, false, state));
        return elements.get(elements.size - 1);
    }

    @Deprecated
    public Element addTextElement(String text, float scale, Color color) {
        elements.add(new Element(text, scale, 0, 0, sprite.getWidth(), sprite.getHeight(), color, false, 0));
        return elements.get(elements.size - 1);
    }

    @Deprecated
    public Element addTextElement(String text, float scale, boolean scaled, Color color, int state) {
        elements.add(new Element(text, scale, 0, 0, sprite.getWidth(), sprite.getHeight(), color, scaled, state));
        return elements.get(elements.size - 1);
    }

    @Deprecated
    public Element addTextElement(String text, float scale, boolean scaled, Color color) {
        elements.add(new Element(text, scale, 0, 0, sprite.getWidth(), sprite.getHeight(), color, scaled, 0));
        return elements.get(elements.size - 1);
    }

    public Element addText(String text, float size, float x, float y, Color color) {
        return addText(text, size, x, y, 0, color, LdText.F1, Align.left, false, 0);
    }

    public Element addText(String text, float size, float x, float y, float width, Color color) {
        return addText(text, size, x, y, width, color, LdText.F1, Align.left, false, 0);
    }

    public Element addText(String text, float size, float x, float y, float width, Color color, int state) {
        return addText(text, size, x, y, width, color, LdText.F1, Align.left, false, state);
    }

    public Element addText(String text, float size, float x, float y, float width, Color color, int align, boolean wrap) {
        return addText(text, size, x, y, width, color, LdText.F1, align, wrap, 0);
    }

    public Element addText(String text, float size, float x, float y, float width, Color color, int align, boolean wrap, int state) {
        return addText(text, size, x, y, width, color, LdText.F1, align, wrap, state);
    }

    public Element addText(String text, float size, float x, float y, float width, Color color, int type, int align, boolean wrap) {
        return addText(text, size, x, y, width, color, type, align, wrap, 0);
    }

    public Element addText(String text, float size, float x, float y, float width, Color color, int type, int align, boolean wrap, int state) {
        elements.add(new Element(text, size, x, y, width, color, type, align, wrap, state));
        return elements.get(elements.size - 1);
    }

    public Element addText(String text, float size, Color color) {
        return addText(text, size, color, false, 0);
    }

    public Element addText(String text, float size, Color color, int state) {
        return addText(text, size, color, false, state);
    }

    public Element addText(String text, float size, boolean scaled, Color color) {
        return addText(text, size, color, scaled, 0);
    }

    public Element addText(String text, float size, Color color, boolean scaled, int state) {
        elements.add(new Element(text, size, 0, 0, sprite.getWidth(), sprite.getHeight(), color, LdText.F1, Align.center, true, scaled, state));
        return elements.get(elements.size - 1);
    }


    public Element addSprite(TextureRegion texture) {
        elements.add(new Element(texture, 0, 0, sprite.getWidth(), sprite.getHeight(), 0, 0));
        return elements.get(elements.size - 1);
    }

    public Element addSprite(TextureRegion texture, int state) {
        elements.add(new Element(texture, 0, 0, sprite.getWidth(), sprite.getHeight(), 0, state));
        return elements.get(elements.size - 1);
    }

    public Element addSprite(TextureRegion texture, int x, int y, int width, int height) {
        elements.add(new Element(texture, x, y, width, height, 0, 0));
        return elements.get(elements.size - 1);
    }

    public Element addRotateSprite(TextureRegion texture, int x, int y, int width, int height, float rotation) {
        elements.add(new Element(texture, x, y, width, height, rotation, 0));
        return elements.get(elements.size - 1);
    }

    public Element addSprite(TextureRegion texture, int x, int y, int width, int height, int state) {
        elements.add(new Element(texture, x, y, width, height, 0, state));
        return elements.get(elements.size - 1);
    }

    public Element addRotateSprite(TextureRegion texture, int x, int y, int width, int height, float rotation, int state) {
        elements.add(new Element(texture, x, y, width, height, rotation, state));
        return elements.get(elements.size - 1);
    }

    public Element addNinePatch(NinePatch ninePatch, float x, float y, float width, float height, int state) {
        elements.add(new Element(ninePatch, x, y, width, height, state));
        return elements.get(elements.size - 1);
    }

    public Element addNinePatch(NinePatch ninePatch, float x, float y, float width, float height) {
        elements.add(new Element(ninePatch, x, y, width, height, 0));
        return elements.get(elements.size - 1);
    }

    public void setNewBounds(float x, float y, float width, float height) {
        setBounds(x, y, width, height);
        sprite.setBounds(x, y, width, height);
    }

    public void setNewPosition(float x, float y) {
        if (isMoveCancel && (Math.abs(getX() - x) > 10 || Math.abs(getY() - y) > 10))
            ((ClickListener) this.getListeners().first()).cancel();
        setPosition(x, y);
        sprite.setPosition(x, y);
    }

    public void setNewOrigin(float x, float y) {
        setOrigin(x, y);
        sprite.setOrigin(x, y);
    }

    public void setNewRotation(float angle) {
        setRotation(angle);
        sprite.setRotation(angle);
    }

    public void setNewX(float x) {
        if (isMoveCancel && Math.abs(getX() - x) > 10)
            ((ClickListener) this.getListeners().first()).cancel();
        setX(x);
        sprite.setX(x);
    }

    public void setNewY(float y) {
        if (isMoveCancel && Math.abs(getY() - y) > 10)
            ((ClickListener) this.getListeners().first()).cancel();
        setY(y);
        sprite.setY(y);
    }

    public void flipSprite(boolean flipX, boolean flipY) {
        sprite.flip(flipX, flipY);
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setLimPres(float lim) {
        limPres = lim;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }


    public Element getFirst() {
        return getElement(0);
    }

    public Element getSecond() {
        return getElement(1);
    }

    public Element getThird() {
        return getElement(2);
    }

    public Element getElement(int i) {
        return elements.get(i);
    }

    public void removeElement(int i) {
        elements.removeValue(getElement(i), false);
    }

    public void clearElements() {
        elements.clear();
    }

    public void setSpriteAngle(float angle) {
        sprite.setRotation(angle);
    }

    public float getSpriteAngle() {
        return sprite.getRotation();
    }

    public void setSpriteOriginCenter() {
        sprite.setOriginCenter();
    }

    public void setSpriteScale(float scale) {
        sprite.setScale(scale);
    }

    public float getSpriteScale() {
        return sprite.getScaleX();
    }

    public void setMoveCancel(boolean moveCancel) {
        isMoveCancel = moveCancel;
    }

    public void draw(SpriteBatch batch) {
        if (!isVisible()) return;
        isPress = false;
        if (this.getListeners().size > 0)
            isPress = ((ClickListener) this.getListeners().first()).isVisualPressed();
        if (isPress || this.getTouchable() == Touchable.disabled || !isActive) {
            if (isPress && aPres < limPres) aPres += LdDelta.getDelta() * 5;
            else aPres = limPres;
        } else {
            if (aPres > 0) aPres -= LdDelta.getDelta() * 5;
            else aPres = 0;
        }

        if (elements.size > 0) for (Element element : elements) element.drawBack(batch, isPress);

        if (ninePatch != null) {
            if (isPress && pressColor != null) batch.setColor(pressColor);
            else if (color != null) batch.setColor(color);
            ninePatch.draw(batch, sprite.getX(), sprite.getY(), sprite.getOriginX(), sprite.getOriginY(), sprite.getWidth(), sprite.getHeight(), sprite.getScaleX(), sprite.getScaleY(), sprite.getRotation());
            batch.setColor(Color.WHITE);
        } else if (isTextureVisible) {
            if (isPress && pressColor != null) sprite.setColor(pressColor);
            else if (color != null) sprite.setColor(color);
            else sprite.setColor(batch.getColor());
            sprite.draw(batch);
        }

        if (elements.size > 0) for (Element element : elements) element.drawSimp(batch, isPress);

        if (aPres > 0) {
            if (ninePatch != null) {
                batch.setColor(0, 0, 0, aPres);
                ninePatch.draw(batch, sprite.getX(), sprite.getY(), sprite.getOriginX(), sprite.getOriginY(), sprite.getWidth(), sprite.getHeight(), sprite.getScaleX(), sprite.getScaleY(), sprite.getRotation());
                batch.setColor(Color.WHITE);
            } else {
                sprite.setColor(0, 0, 0, aPres);
                sprite.draw(batch);
                sprite.setColor(Color.WHITE);
            }
        }

        if (elements.size > 0) for (Element element : elements) element.drawFront(batch, isPress);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
//        setNewX(getX());
//        setNewY(getY());
    }
}