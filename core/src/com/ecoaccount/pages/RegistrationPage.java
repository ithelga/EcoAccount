package com.ecoaccount.pages;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
public class RegistrationPage {
    private Main main;
    private float panY;
    private LdButton butRegistration;
    private LdLinkButton linkLogin;
    private String errorMessage;
    private TextField inpLogin, inpPassword, inpInn, inpFirstName, inpLastName;


    public RegistrationPage(Main m) {
        this.main = m;
        panY = HALF_HEIGHT - 475;

        butRegistration = new LdButton(new TextureRegion(new Texture("images/but_long.png")), HALF_WIDHT - 360, panY - 190, 720, 100, new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                register();
            }
        });
        butRegistration.addText("ЗАРЕГИСТРИРОВАТЬСЯ", 48, Color.WHITE);
        butRegistration.setLimPres(0.1f);

        inpFirstName = new TextField("", main.inputStyle);
        inpFirstName.setMessageText("ИМЯ");
        inpFirstName.setBounds(HALF_WIDHT - 325, panY + 750, 650, 100);
        inpFirstName.setMaxLength(30);


        inpLastName = new TextField("", main.inputStyle);
        inpLastName.setMessageText("ФАМИЛИЯ");
        inpLastName.setBounds(HALF_WIDHT - 325, panY + 600, 650, 100);
        inpLastName.setMaxLength(30);

        inpInn = new TextField("", main.inputStyle);
        inpInn.setMessageText("ИНН");
        inpInn.setBounds(HALF_WIDHT - 325, panY + 450, 650, 100);
        inpInn.setMaxLength(12);

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

        linkLogin = new LdLinkButton("Войти в аккаунт", 48, main.loginPage.clNorm, main.loginPage.clPress, WIDTH / 2, panY - 320, new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.loginPage.show();
            }
        });
    }


    public void show() {
        main.page = Main.Pages.Registration;
        main.stage.clear();
        main.stage.addActor(butRegistration);
        main.stage.addActor(inpFirstName);
        main.stage.addActor(inpLastName);
        main.stage.addActor(inpLogin);
        main.stage.addActor(inpPassword);
        main.stage.addActor(inpInn);
        main.stage.addActor(linkLogin);
        errorMessage = "";
        inpFirstName.setText("");
        inpLastName.setText("");
        inpLogin.setText("");
        inpPassword.setText("");
        inpInn.setText("");


    }

    public void draw(SpriteBatch batch) {

        main.panelGreen.draw(batch, 0, panY, WIDTH, HEIGHT - panY);
        butRegistration.draw(batch);
        LdText.draw("РЕГИСТРАЦИЯ", 110, 0, panY + 1135, WIDTH, Color.WHITE, Align.center, false);
        if (errorMessage != null) {
            LdText.draw(errorMessage, 36, 0, panY + 950, WIDTH, clDark, Align.center, false);
        }

        inpFirstName.draw(batch, 1);
        inpLastName.draw(batch, 1);
        inpInn.draw(batch, 1);
        inpLogin.draw(batch, 1);
        inpPassword.draw(batch, 1);
        linkLogin.draw(batch);

    }


    private void register() {
        errorMessage = "Регистрация...";
        String login = inpLogin.getText();
        String password = inpPassword.getText();
        String inn = inpInn.getText();
        String firstName = inpFirstName.getText();
        String lastName = inpLastName.getText();

        if (login.length() < 3 || password.length() < 3 || inn.length() != 12
                || firstName.length() < 3 || lastName.length() < 3) {
            errorMessage = "Заполните все поля";
            return;
        }
        main.server.register(login, password, inn, firstName, lastName, new Server.Response() {
            @Override
            public void result(Server.State state, String data) {
//                System.out.println("Server register is " + state.name() + ", data: " + data);
                if (state == Server.State.Complete) {
                    JsonValue json = main.server.parseData(data);
                    int s = json.getInt("state", 0);
                    if (s == 1) {
                        main.loginPage.show();
                    } else if (s == 2) {
                        errorMessage = "Логин занят";
                    } else {
                        errorMessage = "Ошибка регистрации";
                    }
                } else {
                    errorMessage = "Ошибка регистрации";
                }
            }
        });
    }

}
