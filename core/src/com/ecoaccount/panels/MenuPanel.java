package com.ecoaccount.panels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.ecoaccount.Main;
import com.ecoaccount.Platform;
import com.ecoaccount.ldutils.ui.LdButton;
import com.ecoaccount.ldutils.ui.LdText;

import static com.ecoaccount.Main.HALF_WIDHT;
import static com.ecoaccount.Main.HEIGHT;
import static com.ecoaccount.Main.WIDTH;
import static com.ecoaccount.Main.clDark;

/**
 * Created by Helga on
 */
public class MenuPanel {

    private Main main;
    private LdButton[] menuButs;
    private LdButton butHome;


    public MenuPanel(Main m) {
        this.main = m;
        menuButs = new LdButton[4];
        for (int i = 0; i < menuButs.length; i++) {
            final int j = i;
            menuButs[i] = new LdButton(new TextureRegion(new Texture("images/but_panel" + (i + 1) + "_dis.png")), HALF_WIDHT - 380 + 220 * i, 75, 100, 100,
                    new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            menuButClick(j);
                        }
                    });
            menuButs[i].addSprite(new TextureRegion(new Texture("images/but_panel" + (i + 1) + ".png")), LdButton.STATE_1);
            menuButs[i].setLimPres(0);
        }

        butHome = new LdButton(new TextureRegion(new Texture("images/but_home.png")), 70, HEIGHT - 120, 30, 55,
                new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        if (main.scanPage.isScan) {
                            main.scanPage.isScan = false;
                            main.platform.hideScanner();
                        }
                        main.homePage.show();
                    }
                });
        butHome.setWidth(215);
        butHome.addText("Домой", 48, 60, 48, 40, clDark, LdText.F2, Align.left, false);

    }

    private void menuButClick(int i) {
        for (int j = 0; j < menuButs.length; j++) {
            menuButs[j].setState(i == j ? LdButton.STATE_1 : 0);
        }
        if (main.scanPage.isScan) {
            main.scanPage.isScan = false;
            main.platform.hideScanner();
        }

        if (i == 0) main.homePage.show();
        else if (i == 1) main.historyPage.show();
        else if (i == 2) main.accountPage.show();
        else if (i == 3) Gdx.app.exit();
    }

    public void show() {
        for (LdButton but : menuButs) {
            main.stage.addActor(but);
        }
        if (main.page == Main.Pages.Scan || main.page == Main.Pages.List || main.page == Main.Pages.Rating) {
            main.stage.addActor(butHome);
        }


    }

    public void draw(SpriteBatch batch) {
        for (LdButton but : menuButs) {
            but.draw(batch);
        }

        if (main.page == Main.Pages.Scan || main.page == Main.Pages.List || main.page == Main.Pages.Rating)
            butHome.draw(batch);
        if (main.page != Main.Pages.Registration && main.page != Main.Pages.Login && main.page != Main.Pages.Home)
            LdText.draw("Счет: " + main.userScore, 48, 815, HEIGHT - 75, 195, clDark, LdText.F2, Align.right, false);
    }


}
