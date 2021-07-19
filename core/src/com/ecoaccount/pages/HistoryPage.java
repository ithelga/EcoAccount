package com.ecoaccount.pages;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.ecoaccount.Main;
import com.ecoaccount.Server;
import com.ecoaccount.boards.HistoryBoard;
import com.ecoaccount.ldutils.LdMask;
import com.ecoaccount.ldutils.ui.LdButton;
import com.ecoaccount.ldutils.ui.LdScroll;
import com.ecoaccount.ldutils.ui.LdText;

import static com.ecoaccount.Main.HALF_HEIGHT;
import static com.ecoaccount.Main.HALF_WIDHT;
import static com.ecoaccount.Main.HEIGHT;
import static com.ecoaccount.Main.WIDTH;
import static com.ecoaccount.Main.clDark;

/**
 * Created by Helga on
 */
public class HistoryPage {

    private Main main;
    private Array<HistoryBoard> historyBoards;
    public Texture field;
    public Texture[] icons;
    private LdScroll scroll;


    public HistoryPage(Main m) {
        this.main = m;
        historyBoards = new Array<>();
        field = new Texture("images/field_rating.png");
        icons = new Texture[]{new Texture("images/icon_rub45.png"),
                new Texture("images/icon_food45.png"),
                new Texture("images/icon_ev45.png"),
                new Texture("images/icon_car45.png")};
        scroll = new LdScroll();
        scroll.setMissRatio(1.5f);
    }


    public void show() {
        main.page = Main.Pages.History;
        main.stage.clear();
        main.menuPanel.show();
        history();


    }

    public void draw(SpriteBatch batch, float delta) {
        scroll.update(delta);
        batch.draw(main.whiteTex, 0, 0, WIDTH, HEIGHT);
        main.panelGray.draw(batch, 0, 250, WIDTH, HEIGHT - 250);
        LdText.draw("История", 110, 0, HEIGHT - 255, WIDTH, clDark, Align.center, false);

        LdMask.begin(batch);
        LdMask.setRect(0, 330 , WIDTH, HEIGHT - 760);
        LdMask.flush();

        float y = HEIGHT - 630;
        for (HistoryBoard board : historyBoards) {
            board.draw(batch, y + scroll.get());
            y -= 240;
        }
        LdMask.end();

    }

    public void history() {

        main.server.history(main.userId, new Server.Response() {
            @Override
            public void result(Server.State state, String data) {
//                System.out.println("Server test is " + state.name() + ", data: " + data);
                if (state == Server.State.Complete) {
                    JsonValue json = main.server.parseData(data);
                    int s = json.getInt("state", 0);
                    if (s == 1) {
                        historyBoards.clear();
                        scroll.setSize(0);
                        scroll.toBegin();
                        scroll.stop();
                        JsonValue jsonActions = json.get("actions");
                        if (jsonActions.size > 0) {
                            for (JsonValue val = jsonActions.child; val != null; val = val.next) {
                                historyBoards.add(new HistoryBoard(HistoryPage.this, val));
                                scroll.addSize(240);
                            }
                            scroll.addSize(-(HEIGHT - 760));
                            scroll.removeNegativeSize();
                        }
                    }
                }
            }
        });
    }

    public void touchDown(float x, float y) {
        scroll.touchDown(x, y);
    }

    public void touchDragged(float x, float y) {
        scroll.touchDragged(x, y);

    }

    public void touchUp(float x, float y) {
        scroll.touchUp(x, y);

    }

    public void scroll(int amount) {
        scroll.scrolled(amount);
    }
}
