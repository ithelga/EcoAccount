package com.ecoaccount.pages;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.JsonValue;
import com.ecoaccount.Main;
import com.ecoaccount.Server;
import com.ecoaccount.ldutils.ui.LdText;

import static com.ecoaccount.Main.HALF_HEIGHT;
import static com.ecoaccount.Main.HALF_WIDHT;
import static com.ecoaccount.Main.HEIGHT;
import static com.ecoaccount.Main.WIDTH;
import static com.ecoaccount.Main.clDark;

/**
 * Created by Helga on
 */
public class RatingPage {

    private Main main;
    private Texture field, iconStar, round, iconRubbish, iconFood, iconTaxi, iconTree;
    private Color[] colors;
    private Color clGrey;
    private String[] fieldTitles;
    public int[][] rValue;

    public RatingPage(Main m) {
        this.main = m;
        field = new Texture("images/field_rating.png");
        iconStar = new Texture("images/icon_star.png");
        round = new Texture("images/icon_round.png");
        iconRubbish = new Texture("images/icon_rubbish.png");
        iconFood = new Texture("images/icon_food.png");
        iconTaxi = new Texture("images/icon_taxi.png");
        iconTree = new Texture("images/icon_trees.png");
        colors = new Color[]{Color.valueOf("98D1BA"), Color.valueOf("FDDF25"), Color.valueOf("D1D4D6"), Color.valueOf("FAA681")};
        clGrey = Color.valueOf("F7F6F6");
        fieldTitles = new String[]{"Вы:", "Первый:", "Средний:", "Последний:"};
        rValue = new int[4][5];

    }

    public void show() {
        main.page = Main.Pages.Rating;
        main.stage.clear();
        main.menuPanel.show();
        getRating();
    }

    public void draw(SpriteBatch batch) {
        batch.draw(main.whiteTex, 0, 0, WIDTH, HEIGHT);
        main.panelGray.draw(batch, 0, 250, WIDTH, HEIGHT - 250);
        LdText.draw("Рейтинг", 110, 0, HEIGHT - 255, WIDTH, clDark, Align.center, false);

        float x = HALF_WIDHT - 470, y = HEIGHT - 630;
        for (int i = 0; i < 4; i++) {
            batch.draw(field, x, y, 940, 200);
            LdText.draw(fieldTitles[i], 48, x + 130, y + 120, 336, colors[i], LdText.F2, Align.center, false);

            batch.setColor(colors[i]);
            batch.draw(iconStar, x + 30, y + 60, 80, 80);

            batch.draw(iconRubbish, x + 695, y + 115, 70, 75);
            batch.draw(iconFood, x + 695, y + 10, 70, 75);
            batch.draw(iconTaxi, x + 840, y + 115, 70, 75);
            batch.draw(iconTree, x + 840, y + 10, 70, 75);
            batch.draw(round, x + 482, y + 34, 125, 125);

            batch.setColor(clGrey);
            batch.draw(round, x + 470, y + 41, 125, 125);

            LdText.draw(rValue[i][4] + "", 48, x + 470, y + 117, 125, colors[i], LdText.F2, Align.center, false);
            LdText.draw(rValue[i][0] + "", 48, x + 630, y + 168, 60, colors[i], LdText.F2, Align.right, false);
            LdText.draw(rValue[i][1] + "", 48, x + 630, y + 65, 60, colors[i], LdText.F2, Align.right, false);
            LdText.draw(rValue[i][2] + "", 48, x + 775, y + 168, 60, colors[i], LdText.F2, Align.right, false);
            LdText.draw(rValue[i][3] + "", 48, x + 775, y + 65, 60, colors[i], LdText.F2, Align.right, false);

            batch.setColor(Color.WHITE);
            y -= 240;

        }
    }

    private void getRating() {
        main.server.rating(main.userId, new Server.Response() {
            @Override
            public void result(Server.State state, String data) {
//                System.out.println("Server rating is " + state.name() + ", data: " + data);
                if (state == Server.State.Complete) {
                    JsonValue json = main.server.parseData(data);
                    int s = json.getInt("state", 0);
                    if (s == 1) {
                        JsonValue[] jsons = new JsonValue[]{
                                json.get("r_user"), json.get("r_max"),
                                json.get("r_av"), json.get("r_min")
                        };

                        for (int i = 0; i < rValue.length; i++) {
                            rValue[i][0] = jsons[i].getInt("Мусор", 0);
                            rValue[i][1] = jsons[i].getInt("Еда с собой", 0);
                            rValue[i][2] = jsons[i].getInt("Такси", 0);
                            rValue[i][3] = jsons[i].getInt("Мероприятие", 0);
                            rValue[i][4] = 0;

                            for (int j = 0; j < 4; j++) rValue[i][4] += rValue[i][j];
                        }
                    }
                }
            }
        });
    }
}
