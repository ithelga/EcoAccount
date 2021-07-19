package com.ecoaccount.boards;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.JsonValue;
import com.ecoaccount.Server;
import com.ecoaccount.ldutils.ui.LdButton;
import com.ecoaccount.ldutils.ui.LdText;
import com.ecoaccount.pages.HistoryPage;
import com.ecoaccount.pages.ListPage;

import static com.ecoaccount.Main.HALF_WIDHT;
import static com.ecoaccount.Main.clDark;
import static com.ecoaccount.Main.clLight;

/**
 * Created by Helga on
 */
public class ListBoard {
    private ListPage listPage;
    private boolean done;
    private float x, h;
    private int id;
    private String text;
    public LdButton butCheck;


    public ListBoard(ListPage l, JsonValue json) {
        this.listPage = l;
        id = json.getInt("id");
        done = json.getInt("done", 0) == 1;
        text = json.getString("text");
        h = LdText.getHeight(text, 48, 775, true) + 75;
        x = HALF_WIDHT - 470;
        butCheck = new LdButton(new TextureRegion(new Texture("images/but_check1.png")), x + 845, 0, 60, 60, new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                done = !done;
                changeDone();
            }
        });
        butCheck.addSprite(new TextureRegion(new Texture("images/but_check2.png")), LdButton.STATE_1);
        listPage.main.stage.addActor(butCheck);
    }

    public void draw(SpriteBatch batch, float y) {
        butCheck.setNewY(y + h - 85);
        if (done) {
            butCheck.setState(LdButton.STATE_1);
            batch.setColor(clLight);
        } else butCheck.setState(0);
        listPage.field.draw(batch, x, y, 940, h);
        batch.setColor(Color.WHITE);
        butCheck.draw(batch);
        LdText.draw(text, 48, x + 35, y + h - 37, 775, done ? Color.WHITE : clDark, Align.left, true);
    }


    private void changeDone() {
//        System.out.println(done);
        listPage.main.server.changeDone(id, done ? 1 : 0, (state, data) -> {
//            System.out.println("Server changeDone is " + state.name() + ", data: " + data);
            if (state == Server.State.Complete) {
                JsonValue json = listPage.main.server.parseData(data);
                int s = json.getInt("state", 0);
                if (s == 1) {
                    listPage.notes();
                }
            }
        });
    }

    public float getHeight() {
        return h;
    }
}
