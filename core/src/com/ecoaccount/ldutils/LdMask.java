package com.ecoaccount.ldutils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Lion's DaMajectim on 22.02.2019
 * Updated on 07.07.2020
 */

public class LdMask {

    private static Texture empty;
    private static SpriteBatch batch;
    private static ShapeRenderer renderer;
    private static OrthographicCamera camera;

    public static void init(OrthographicCamera orthographicCamera, Texture emptyTexture) {
        renderer = new ShapeRenderer();
        camera = orthographicCamera;
        empty = emptyTexture;
    }

    public static void begin(SpriteBatch batch) {
        batch.end();
        LdMask.batch = batch;

        Gdx.gl.glClearDepthf(1f);
        Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glDepthFunc(GL20.GL_LESS);
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDepthMask(true);
        Gdx.gl.glColorMask(false, false, false, false);

        renderer.setProjectionMatrix(batch.getProjectionMatrix());
        renderer.begin(ShapeRenderer.ShapeType.Filled);
    }

    public static void setRect(Rectangle rect) {
        renderer.rect(rect.x, rect.y, rect.width, rect.height);
    }
    public static void setRect(float x, float y, float w, float h) {
        renderer.rect(x, y, w, h);
    }
    public static void setRoundedRect(float x, float y, float w, float h, float r) {
        float d = r * 2;
        renderer.rect(x + r, y + r, w - d, h - d);
        renderer.rect(x + r, y, w - d, r);
        renderer.rect(x + w - r, y + r, r, h - d);
        renderer.rect(x + r, y + h - r, w - d, r);
        renderer.rect(x, y + r, r, h - d);
        renderer.arc(x + r, y + r, r, 180f, 90f);
        renderer.arc(x + w - r, y + r, r, 270f, 90f);
        renderer.arc(x + w - r, y + h - r, r, 0f, 90f);
        renderer.arc(x + r, y + h - r, r, 90f, 90f);
    }
    public static void setEllipse(float x, float y, float w, float h) {
        renderer.ellipse(x, y, w, h);
    }

    public static void setTriangle(float x1, float y1, float x2, float y2, float x3, float y3) {
        renderer.triangle(x1, y1, x2, y2, x3, y3);
    }

    public static void setPolygon(float... vertices) {
        renderer.polygon(vertices);
    }

    public static void setCircle(float x, float y, float size) {
        size /= 2;
        renderer.circle(x + size, y + size, size);
    }

    public static void flush() {
        renderer.end();

        Gdx.gl.glColorMask(true, true, true, true);
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDepthFunc(GL20.GL_EQUAL);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
    }

    public static void end() {
        batch.draw(empty, 0, 0, 0, 0); //Drawing empty, because last draw not masked...
        Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
    }
}
