package com.ecoaccount.pages;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.JsonValue;
import com.ecoaccount.Main;
import com.ecoaccount.Server;
import com.ecoaccount.ldutils.ui.LdButton;
import com.ecoaccount.ldutils.ui.LdLinkButton;
import com.ecoaccount.ldutils.ui.LdText;

import static com.ecoaccount.Main.HALF_HEIGHT;
import static com.ecoaccount.Main.HALF_WIDHT;
import static com.ecoaccount.Main.HEIGHT;
import static com.ecoaccount.Main.WIDTH;
import static com.ecoaccount.Main.clDark;

/**
 * Created by Helga on
 */
public class LoginPage {

    private Main main;
    private float panY;
    private Texture icon;
    private LdButton butLogin;
    private LdLinkButton linkRegister;
    public Color clNorm = Color.valueOf("BBB5B5");
    public Color clPress = Color.valueOf("ABABAB");
    private TextField inpLogin, inpPassword;
    private String errorMessage;


    public LoginPage(Main m) {
        this.main = m;
        panY = HALF_HEIGHT - 475;
        icon = new Texture("images/icon_logo360.png");

        butLogin = new LdButton(new TextureRegion(new Texture("images/but_short.png")), HALF_WIDHT - 190, panY - 190, 380, 100, new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                auth();

            }
        });
        butLogin.addText("ВОЙТИ", 48, Color.WHITE);
        butLogin.setLimPres(0.1f);

        linkRegister = new LdLinkButton("Создать аккаунт", 48, clNorm, clPress, WIDTH / 2, panY - 320, new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.registrationPage.show();
            }
        });


        inpLogin = new TextField("", main.inputStyle);
        inpLogin.setMessageText("ЛОГИН");
        inpLogin.setBounds(HALF_WIDHT - 325, panY + 300, 650, 100);
        inpLogin.setMaxLength(30);

        inpPassword = new TextField("", main.inputStyle);
        inpPassword.setMessageText("ПАРОЛЬ");
        inpPassword.setBounds(HALF_WIDHT - 325, panY + 150, 650, 100);
        inpPassword.setMaxLength(30);
        inpPassword.setPasswordMode(true);
        inpPassword.setPasswordCharacter('•');
    }


    public void show() {
        main.page = Main.Pages.Login;
        main.stage.clear();
        main.stage.addActor(butLogin);
        main.stage.addActor(linkRegister);
        main.stage.addActor(inpLogin);
        main.stage.addActor(inpPassword);
        errorMessage = "";
        inpLogin.setText("");
        inpPassword.setText("");
    }

    public void draw(SpriteBatch batch) {

        main.panelGreen.draw(batch, 0, panY, WIDTH, HEIGHT - panY);
        batch.draw(icon, HALF_WIDHT - 150, panY + 725, 300, 360);

        LdText.draw("ЭКО КАБИНЕТ", 110, 0, panY + 615, WIDTH, Color.WHITE, Align.center, false);
        if (errorMessage != null) {
            LdText.draw(errorMessage, 36, 0, panY + 480, WIDTH, clDark, Align.center, false);
        }

        butLogin.draw(batch);
        linkRegister.draw(batch);
        inpLogin.draw(batch, 1);
        inpPassword.draw(batch, 1);
    }

    private void auth() {
        errorMessage = "Авторизация...";
        String login = inpLogin.getText();
        String password = inpPassword.getText();

        if (login.length() < 3 || password.length() < 3) {
            errorMessage = "Неверный логин или пароль";
            return;
        }

        main.server.auth(login, password, new Server.Response() {
            @Override
            public void result(Server.State state, String data) {
//                System.out.println("Server test is " + state.name() + ", data: " + data);
                if (state == Server.State.Complete) {
                    JsonValue json = main.server.parseData(data);
                    int s = json.getInt("state", 0);
                    if (s == 1) {
                        main.userId = json.getInt("id");
                        main.userFirstName = json.getString("first_name");
                        main.userLastName = json.getString("last_name");
                        main.userScore = json.getInt("scores");
                        main.homePage.show();

                        main.homePage.getPageScores("Мусор", 0);
                        main.homePage.getPageScores("Еда с собой", 1);
                        main.homePage.getPageScores("Мероприятие", 2);
                        main.homePage.getPageScores("Такси", 3);

                    } else if (s == 2) {
                        errorMessage = "Неверный логин или пароль";
                    } else {
                        errorMessage = "Ошибка авторизации";
                    }
                } else {
                    errorMessage = "Ошибка авторизации";
                }
            }
        });
    }
}
