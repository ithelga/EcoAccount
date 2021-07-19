package com.ecoaccount.pages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.JsonValue;
import com.ecoaccount.Main;
import com.ecoaccount.Server;
import com.ecoaccount.ldutils.ui.LdButton;
import com.ecoaccount.ldutils.ui.LdText;

import static com.ecoaccount.Main.HALF_HEIGHT;
import static com.ecoaccount.Main.HALF_WIDHT;
import static com.ecoaccount.Main.HEIGHT;
import static com.ecoaccount.Main.WIDTH;
import static com.ecoaccount.Main.clDark;

/**
 * Created by Helga on
 */
public class HomePage {

    private Main main;
    private Texture icon;
    private LdButton[] homeButs;
    public int[] pageScores;


    public HomePage(Main m) {
        this.main = m;

        float cY = HEIGHT - 450 - ((HEIGHT - 250) - 1430) / 2;
        homeButs = new LdButton[6];
        for (int i = 0; i < homeButs.length; i++) {
            final int j = i;
            homeButs[i] = new LdButton(new TextureRegion(new Texture("images/main_but.png")),
                    HALF_WIDHT - 470 + 490 * (i % 2), cY - 490 * (i / 2), 450, 450, new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    menuButClick(j);

                }
            });
            homeButs[i].addSprite(new TextureRegion(new Texture("images/icon_home" + (i + 1) + ".png")),
                    100, 130, 250, 260);

            homeButs[i].setPressColor(clDark);
            homeButs[i].getFirst().setColor(clDark);
            homeButs[i].getFirst().setPressColor(Color.WHITE);
            homeButs[i].setLimPres(0f);

        }
        homeButs[0].addText("Мусор", 48, 0, 100, 450, clDark, LdText.F2, Align.center, false).setPressColor(Color.WHITE);
        homeButs[1].addText("Еда с собой", 48, 0, 100, 450, clDark, LdText.F2, Align.center, false).setPressColor(Color.WHITE);
        homeButs[2].addText("Мероприятия", 48, 0, 100, 450, clDark, LdText.F2, Align.center, false).setPressColor(Color.WHITE);
        homeButs[3].addText("ЭкоТакси", 48, 0, 100, 450, clDark, LdText.F2, Align.center, false).setPressColor(Color.WHITE);
        homeButs[4].addText("Заметки", 48, 0, 100, 450, clDark, LdText.F2, Align.center, false).setPressColor(Color.WHITE);
        homeButs[5].addText("Рейтинг", 48, 0, 100, 450, clDark, LdText.F2, Align.center, false).setPressColor(Color.WHITE);
        pageScores = new int[]{0, 0, 0, 0};
    }

    private void menuButClick(int i) {
        if (i == 0) {
            getPageScores("Мусор", 0);
            main.scanPage.show(ScanPage.Subpage.Rubbish);
        } else if (i == 1) {
            getPageScores("Еда с собой", 1);
            main.scanPage.show(ScanPage.Subpage.Food);
        } else if (i == 2) {
            getPageScores("Мероприятие", 2);
            main.scanPage.show(ScanPage.Subpage.Event);
        } else if (i == 3) {
            getPageScores("Такси", 3);
            main.scanPage.show(ScanPage.Subpage.Taxi);
        } else if (i == 4) main.listPage.show();
        else if (i == 5) {
            main.ratingPage.show();
        }

    }


    public void show() {
        main.page = Main.Pages.Home;
        main.stage.clear();
        main.menuPanel.show();
        for (LdButton but : homeButs) {
            main.stage.addActor(but);
        }
    }

    public void draw(SpriteBatch batch) {
        batch.draw(main.whiteTex, 0, 0, WIDTH, HEIGHT);
        main.panelGray.draw(batch, 0, 250, WIDTH, HEIGHT - 250);
        for (LdButton but : homeButs) {
            but.draw(batch);
        }
    }

    public void getPageScores(String type, int i) {
        main.server.scores(main.userId, type, new Server.Response() {
                    @Override
                    public void result(Server.State state, String data) {
//                        System.out.println("Server scores is " + state.name() + ", data: " + data);
                        if (state == Server.State.Complete) {
                            JsonValue json = main.server.parseData(data);
                            int s = json.getInt("state", 0);
                            if (s == 1) pageScores[i] = json.getInt("scores");
                            else pageScores[i] = 0;
                        } else pageScores[i] = 0;
                    }
                }
        );

    }
}
