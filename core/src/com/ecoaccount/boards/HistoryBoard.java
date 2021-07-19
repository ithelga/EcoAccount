package com.ecoaccount.boards;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.JsonValue;
import com.ecoaccount.ldutils.ui.LdText;
import com.ecoaccount.pages.HistoryPage;

import static com.ecoaccount.Main.HALF_WIDHT;
import static com.ecoaccount.Main.WIDTH;
import static com.ecoaccount.Main.clDark;

/**
 * Created by Helga on
 */
public class HistoryBoard {
    private HistoryPage historyPage;
    private String text, type, locate, data;
    private int count, iconType;
    private float x;
    public String si;


    public HistoryBoard(HistoryPage h, JsonValue json) {
        this.historyPage = h;
        data = json.getString("data");

        String[] arrayData = data.split(" ");
        String[] arrayTime = arrayData[1].split(":");
        String[] arrayDataTime = arrayData[0].split("-");
        data = arrayDataTime[2] + "." + arrayDataTime[1] + "." + arrayDataTime[0] + " " + arrayTime[0] + ":" + arrayTime[1];

        count = json.getInt("count");
        text = json.getString("text");
        type = json.getString("type");
        locate = json.getString("locate");

        if (type.equals("Мусор")) {
            si = "кг";
            iconType = 0;
        } else {
            si = "шт";
            if (type.equals("Еда с собой")) iconType = 1;
            else if (type.equals("Мероприятие")) iconType = 2;
            else if (type.equals("Такси")) iconType = 3;
        }

        x = HALF_WIDHT - 470;
    }

    public void draw(SpriteBatch batch, float y) {
        batch.draw(historyPage.field, x, y, 940, 200);
        LdText.draw(text, 40, x + 30, y + 80, 750, clDark, LdText.F2, Align.left, false);
        LdText.draw(locate, 40, x + 30, y + 155, 630, clDark, Align.left, false);
        LdText.draw(data, 40, x + 670, y + 155, 240, clDark, Align.right, false);
        LdText.draw(count + si, 40, x + 835, y + 80, 75, clDark, Align.right, false);
        batch.draw(historyPage.icons[iconType], x + 775, y + 40, 45, 50);
    }

}
