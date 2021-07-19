package com.ecoaccount.ldutils.animations;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Lion's DaMajectim on 04.01.2019
 * Updated on 04.01.2019
 */

public class FrameAnimation extends Animation {

    public float time, x, y, s;

    public FrameAnimation(float frameDuration, TextureRegion... frames) {
        super(frameDuration, frames);
    }

    public TextureRegion getFrame() {
        return (TextureRegion) getKeyFrame(time);
    }

    public boolean isFinished() {
        return isAnimationFinished(time);
    }
}