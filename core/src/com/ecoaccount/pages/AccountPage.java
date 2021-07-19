package com.ecoaccount.pages;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.ecoaccount.Main;
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
public class AccountPage {

    private Main main;
    private LdButton[] payButs;
    private Texture texFIO;


    public AccountPage(Main m) {
        this.main = m;
        payButs = new LdButton[8];
        texFIO = new Texture("images/texFIO.png");


        for (int i = 0; i < payButs.length; i++) {
            final int j = i;
            payButs[i] = new LdButton(new TextureRegion(new Texture("images/but_pay" + (i + 1) + ".png")),
                    HALF_WIDHT - 470 + 490 * (i % 2),
                    HALF_HEIGHT - 140 - 140 * (i / 2), 450, 100, new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                }
            });
            payButs[i].setLimPres(0.25f);
        }
    }


    public void show() {
        main.page = Main.Pages.Account;
        main.stage.clear();
        main.menuPanel.show();
        for (LdButton but : payButs) {
            main.stage.addActor(but);
        }

    }

    public void draw(SpriteBatch batch) {
        batch.draw(main.whiteTex, 0, 0, WIDTH, HEIGHT);
        main.panelGray.draw(batch, 0, 250, WIDTH, HEIGHT - 250);

        for (LdButton but : payButs) {
            but.draw(batch);
        }


        batch.draw(texFIO, HALF_WIDHT - 470, HALF_HEIGHT + 320, 940, 100);
        batch.draw(texFIO, HALF_WIDHT - 470, HALF_HEIGHT + 470, 940, 100);

        LdText.draw("Способы вывода:", 48, 0, HALF_HEIGHT + 85, WIDTH, clDark, Align.center, false);
        LdText.draw("БАЛАНС: " + main.getUserMoney(), 48, 0, HALF_HEIGHT + 185, WIDTH, clDark, LdText.F2, Align.center, false);


        LdText.draw("Личный кабинет", 110, 0, HEIGHT - 255, WIDTH, clDark, Align.center, false);
        LdText.draw(main.userFirstName.toUpperCase(), 48, HALF_WIDHT - 370, HALF_HEIGHT + 540, WIDTH, Color.WHITE, Align.left, false);
        LdText.draw(main.userLastName.toUpperCase(), 48, HALF_WIDHT - 370, HALF_HEIGHT + 390, WIDTH, Color.WHITE, Align.left, false);

    }
}
