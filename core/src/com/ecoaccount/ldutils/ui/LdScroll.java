package com.ecoaccount.ldutils.ui;

import com.badlogic.gdx.utils.TimeUtils;

/**
 * Created by Lion's DaMajectim on 26.02.2020
 * Updated on 15.07.2020
 */

public class LdScroll {

    private static final float PULL_SPEED = 300;

    private boolean isScroll, isVertical, isEndless;
    private float sY, sX, imp, pullImp, down, downY, downX, width, height, missRatio, pullRatio;
    private long time;

    public float tempSize;

    public LdScroll() { this(true, 0); }
    public LdScroll(float height) { this(true, height); }
    public LdScroll(boolean isVertical) { this(isVertical, 0); }
    public LdScroll(boolean isVertical, float height) {
        this.isVertical = isVertical;
        this.height = height;
        missRatio = 1.1f;
        downY = -1;
        downX = -1;
    }

    public void update(float delta) {
        if (!isScroll) return;

        if (isVertical) {
            if (isEndless) {
                impUpdate(delta);
                if (!yPullUpdate(delta)) {
                    if (imp != 0) sY += imp * delta;
                }
                return;
            }

            if (imp < 0 && sY <= 0) imp = 0;
            if (imp > 0 && sY >= height) imp = 0;

            if (sY < 0) {
                if (sY + 1000 * delta > 0) sY = 0;
                else sY += 1000 * delta;
            }
            else if (sY > height) {
                if (sY - 1000 * delta < height) sY = height;
                else sY -= 1000 * delta;
            }
            else {
                impUpdate(delta);
                if (!yPullUpdate(delta)) {
                    if (imp != 0) {
                        if (sY + imp * delta <= 0) sY = 0;
                        else if (sY + imp * delta >= height) sY = height;
                        else sY += imp * delta;
                    }
                }
            }
        }
        else {
            if (isEndless) {
                impUpdate(delta);
                if (!xPullUpdate(delta)) {
                    if (imp != 0) sX += imp * delta;
                }
                return;
            }

            if (imp < 0 && sX <= 0) imp = 0;
            if (imp > 0 && sX >= width) imp = 0;

            if (sX < 0) {
                if (sX + 1000 * delta > 0) sX = 0;
                else sX += 1000 * delta;
            }
            else if (sX > width) {
                if (sX - 1000 * delta < width) sX = width;
                else sX -= 1000 * delta;
            }
            else {
                impUpdate(delta);
                if (!xPullUpdate(delta)) {
                    if (imp != 0) {
                        if (sX + imp * delta <= 0) sX = 0;
                        else if (sX + imp * delta >= width) sX = width;
                        else sX += imp * delta;
                    }
                }
            }
        }
    }

    private void impUpdate(float delta) {
        if (imp > 10) {
            imp -= 1500 * delta;
            if (imp < 0) imp = 0;
        }
        else if (imp < -10) {
            imp += 1500 * delta;
            if (imp > 0) imp = 0;
        }
        else imp = 0;

        if (pullRatio != 0) pullImp = (imp == 0 ? 0 : (PULL_SPEED * (imp < 0 ? -1 : 1)));
    }

    private boolean xPullUpdate(float delta) {
        if (pullRatio == 0 || imp < -PULL_SPEED || imp > PULL_SPEED) return false;
        float leftPull, rightPull, pull = sX % pullRatio;
        if (pull == 0) return true;
        if (pull > 0) {
            leftPull = sX - pull;
            rightPull = sX + pullRatio - pull;
            if (pullImp == 0) pullImp = PULL_SPEED * ((pull > pullRatio / 2) ? 1 : -1);
        }
        else {
            leftPull = sX - pullRatio - pull;
            rightPull = sX - pull;
            if (pullImp == 0) pullImp = PULL_SPEED * (((pull * -1) > pullRatio / 2) ? -1 : 1);
        }

        if (sX + pullImp * delta <= leftPull && pullImp < 0) {
            sX = leftPull;
            imp = 0;
        }
        else if (sX + pullImp * delta >= rightPull && pullImp > 0) {
            sX = rightPull;
            imp = 0;
        }
        else sX += pullImp * delta;
        return true;
    }

    private boolean yPullUpdate(float delta) {

        return false;
    }

    private void verScroll(float y) {
        if ((down + y <= height && down + y >= 0) || isEndless) sY = down + y; //Скролим
        else {
            float miss;
            if (down + y < 0) miss = - (down + y);
            else miss = height - (down + y);
            sY = down + y + miss / missRatio;
        }
    }

    private void horScroll(float x) {
        if ((down + x <= width && down + x >= 0) || isEndless) sX = down + x; //Скролим
        else {
            float miss;
            if (down + x < 0) miss = - (down + x);
            else miss = width - (down + x);
            sX = down + x + miss / missRatio;
        }
    }

    public void touchDown(float x, float y) {
        isScroll = false;
        if (isVertical) {
            down = sY - y;
            downY = y;
        }
        else {
            down = sX - x;
            downX = x;
        }
        time = TimeUtils.millis() - 1;
    }

    public void touchDragged(float x, float y) {
        if (isVertical) { if (downY != -1) verScroll(y); }
        else { if (downX != -1) horScroll(x); }
    }

    public void touchUp(float x, float y) {
        if (isVertical) {
            if (downY != -1) {
                isScroll = true;
                imp = ((y - downY) * 1000) / (TimeUtils.millis() - time);
                impUpdate(0);
                downY = -1;
            }
        }
        else {
            if (downX != -1) {
                isScroll = true;
                imp = ((x - downX) * 1000) / (TimeUtils.millis() - time);
                impUpdate(0);
                downX = -1;
            }
        }
    }
    public void scrolled(int amount) {
        isScroll = true;
        imp = amount * 2000;
    }

    public void stop() { imp = 0; }
    public void toBegin() { sY = 0; sX = 0; }
    public void toEnd() { sY = height; sX = width; }
    public void set(float pos) { sY = pos; sX = pos; }
    public void removeNegativeSize() { if (height < 0) height = 0; if (width < 0) width = 0; }
    public void setMissRatio(float ratio) {
        if (ratio < 1) ratio = 1;
        missRatio = ratio;
    }
    public void setPullRatio(float ratio) {
        if (ratio < 0) ratio *= -1;
        pullRatio = ratio;
    }
    public void setSize(float size) {
        if (isVertical) height = size;
        else width = size;
    }
    public void addSize(float addSize) {
        if (isVertical) height += addSize;
        else width += addSize;
    }
    public void addImp(float imp) {
        this.imp += imp;
        isScroll = true;
    }
    public void setEndless(boolean endless) { isEndless = endless; }
    public boolean isBegin() {
        if (isVertical) return sY == 0;
        else return sX == 0;
    }
    public boolean isEnd() {
        if (isVertical) return sY == height;
        else return sX == width;
    }
    public boolean isStop() { return imp == 0 && pullImp == 0; }
    public float getSize() {
        if (isVertical) return height;
        else return width;
    }
    public float get() {
        if (isVertical) return sY;
        else return sX;
    }
}
