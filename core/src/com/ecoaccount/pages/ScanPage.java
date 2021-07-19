package com.ecoaccount.pages;

import com.badlogic.gdx.Application;
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

import java.util.HashMap;
import java.util.Map;

import static com.ecoaccount.Main.AR;
import static com.ecoaccount.Main.HALF_HEIGHT;
import static com.ecoaccount.Main.HALF_WIDHT;
import static com.ecoaccount.Main.HEIGHT;
import static com.ecoaccount.Main.WIDTH;
import static com.ecoaccount.Main.clDark;
import static com.ecoaccount.Main.clLight;

/**
 * Created by Helga on
 */
public class ScanPage {
    private final String qrKey1 = "e2b704eb6c119a0b4abe224cbb2eb06f;1";

    private Main main;
    private TextureRegion icon;
    private Texture round;
    private LdButton butScan, butCancel, butOk, butTest;
    private Subpage subpage;
    private String title, sScan, sScanTitle;
    public boolean isScan, isScanResult;
    private LdButton[] butSetScans;
    private Map<String, String> textScan;
    public float s;
    private int num;

    public enum Subpage {Rubbish, Food, Event, Taxi}


    public ScanPage(Main m) {
        this.main = m;
        icon = new TextureRegion(new Texture("images/icon_scan_res.png"));
        textScan = new HashMap<>();
        round = new Texture("images/round.png");

        butScan = new LdButton(new TextureRegion(new Texture("images/but_long.png")), HALF_WIDHT - 360, 370, 720, 100, new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isScan = true;
                butScan.setVisible(false);
                butCancel.setVisible(true);
                butTest.setVisible(Gdx.app.getType() == Application.ApplicationType.Desktop);
                butOk.setVisible(false);
                for (LdButton but : butSetScans) {
                    but.setVisible(true);
                }
                main.platform.setScannerBounds((int) ((HALF_WIDHT - 250) * AR), (int) ((HEIGHT - (HALF_HEIGHT + 285 + 50)) * AR), (int) (500 * AR), (int) (500 * AR));
                main.platform.showScanner();

            }
        });
        butScan.addSprite(new TextureRegion(new Texture("images/icon_qr.png")), 600, 17, 70, 70);
        butScan.addText("СКАНИРОВАТЬ QR", 48, 0, 68, 600, Color.WHITE, Align.center, false);
        butScan.setLimPres(0.1f);


        butCancel = new LdButton(new TextureRegion(new Texture("images/but_short.png")), HALF_WIDHT - 190, 370, 380, 100, new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isScan = false;
                butScan.setVisible(true);
                butCancel.setVisible(false);
                butTest.setVisible(false);
                for (LdButton but : butSetScans) {
                    but.setVisible(false);
                }
                butOk.setVisible(false);
                main.platform.hideScanner();

            }
        });
        butCancel.addText("ОТМЕНА", 48, Color.WHITE);
        butCancel.setLimPres(0.1f);


        butSetScans = new LdButton[2];
        for (int i = 0; i < butSetScans.length; i++) {
            final int j = i;
            butSetScans[i] = new LdButton(new TextureRegion(new Texture("images/btn_f" + (i + 1) + "_dis.png")),
                    HALF_WIDHT - 460 + 820 * (i % 2), 550, 100, 100, new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    butSetScans[j].setState(butSetScans[j].getState() == LdButton.STATE_1 ? 0 : LdButton.STATE_1);
                    if (j == 0)
                        main.platform.setAutofocus(butSetScans[j].getState() == LdButton.STATE_1);
                    else main.platform.setFlashLight(butSetScans[j].getState() == LdButton.STATE_1);
                }
            });
            butSetScans[i].addSprite(new TextureRegion(new Texture("images/btn_f" + (i + 1) + ".png")), LdButton.STATE_1);
            butSetScans[i].setLimPres(0);
        }

        butTest = new LdButton(new TextureRegion(new Texture("images/btnTest.png")), HALF_WIDHT - 190, 980, 380, 100,
                new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        scan(qrKey1);
                    }
                });
        butTest.addText("Тест", 48, 0, 68, 380, Color.WHITE, Align.center, false);
        butTest.setLimPres(0.25f);

        butOk = new LdButton(new TextureRegion(new Texture("images/but_short.png")), HALF_WIDHT - 190, HALF_HEIGHT - 250, 380, 100,
                new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        butScan.setVisible(true);
                        isScanResult = false;
                    }
                });
        butOk.addText("ОК", 48, 0, 70, 380, Color.WHITE, Align.center, false);
        butOk.setLimPres(0.25f);

    }


    public void show(Subpage subpage) {
        this.subpage = subpage;
        main.page = Main.Pages.Scan;
        main.stage.clear();
        main.menuPanel.show();
        main.stage.addActor(butScan);
        main.stage.addActor(butCancel);
        main.stage.addActor(butTest);
        main.stage.addActor(butOk);
        isScan = false;
        butScan.setVisible(true);
        butCancel.setVisible(false);
        butTest.setVisible(false);
        butOk.setVisible(false);
        for (LdButton but : butSetScans) {
            main.stage.addActor(but);
            but.setVisible(false);
        }

        sScan = "";
        sScanTitle = "";

        if (subpage == Subpage.Rubbish) {
            title = "Мусор";
            num = 0;
        } else if (subpage == Subpage.Food) {
            title = "Еда с собой";
            num = 1;
        } else if (subpage == Subpage.Event) {
            title = "Мероприятия";
            num = 2;
        } else if (subpage == Subpage.Taxi) {
            title = "Такси";
            num = 3;
        }
    }

    public void draw(SpriteBatch batch) {
        batch.draw(main.whiteTex, 0, 0, WIDTH, HEIGHT);
        main.panelGray.draw(batch, 0, 250, WIDTH, HEIGHT - 250);
        s = isScan ? 1.31f : 1;
        batch.draw(icon, HALF_WIDHT - 375, HALF_HEIGHT - 290, 375, 375, 750, 750, s, s, 0);
        LdText.draw(title, 110, 0, HEIGHT - 255, WIDTH, clDark, Align.center, false);
        if (isScan) batch.draw(round, HALF_WIDHT - 332, HALF_HEIGHT - 247, 665, 665);
        butCancel.draw(batch);
        butScan.draw(batch);
        butTest.draw(batch);
        for (LdButton but : butSetScans) {
            but.draw(batch);
        }
        if (!isScan) {
            LdText.draw(main.homePage.pageScores[num] + "", 144, 0, HALF_HEIGHT + 140, WIDTH, Color.WHITE, LdText.F2, Align.center, false);
        }
    }

    public void drawDialog(SpriteBatch batch) {
        if (!isScanResult) return;

        batch.setColor(0, 0, 0, 0.5f);
        batch.draw(main.whiteTex, 0, 0, WIDTH, HEIGHT);
        batch.setColor(Color.WHITE);
        s = 1;
        isScan = false;
        for (LdButton but : butSetScans) {
            but.setVisible(false);
        }

        batch.draw(main.whiteTex, 0, butOk.getY() - 50, WIDTH, 800);
        butOk.draw(batch);
        LdText.draw(sScan, 48, 110, butOk.getY() + 490, WIDTH - 220, clDark, Align.left, true);
        LdText.draw(sScanTitle, 64, 110, butOk.getY() + 685, WIDTH - 220, clLight, Align.center, true);

    }

    public void scanned(String code) {
        scan(code);
//        System.out.println("CODE IN CORE: " + code);
    }

    private void scan(String code) {
        String[] data = code.split(";");
        if (data.length < 2) {
            sScan = "Передан неверный ключ";
            sScanTitle = "Ошибка регистрации \nЭко События";
            return;
        }
        String type = "";

        if (subpage == Subpage.Rubbish) type = "Мусор";
        else if (subpage == Subpage.Taxi) type = "Такси";
        else if (subpage == Subpage.Food) type = "Еда с собой";
        else if (subpage == Subpage.Event) type = "Мероприятие";
//        System.out.println(type);

        main.server.scan(main.userId, type, (data[0]), Integer.parseInt(data[1]), new Server.Response() {
            @Override
            public void result(Server.State state, String data) {
                sScan = "";
//                System.out.println("Server scan is " + state.name() + ", data: " + data);
                if (state == Server.State.Complete) {
                    isScanResult = true;
                    butOk.setVisible(true);
                    isScan = false;
                    butScan.setVisible(false);
                    butCancel.setVisible(false);
                    butTest.setVisible(false);
                    for (LdButton but : butSetScans) {
                        but.setVisible(false);
                    }

                    JsonValue json = main.server.parseData(data);
                    int s = json.getInt("state", 0);
                    if (s == 1) {
                        main.userScore = json.getInt("user_scores");
                        textScan.put("Категория:", json.getString("type"));
                        textScan.put("Описание:", json.getString("text"));
                        textScan.put("Адрес:", json.getString("locate"));
                        textScan.put("Кол-во:", json.getString("count"));
                        textScan.put("Баллы:", json.getString("scores"));
                        isScanResult = true;
                        sScanTitle = "Зарегистрировано Эко Событие";
                        for (Map.Entry<String, String> entry : textScan.entrySet()) {
                            sScan += entry.getKey() + " " + entry.getValue() + "\n";
                        }
                        main.homePage.getPageScores(json.getString("type"), num);

                    } else if (s == 2) {
                        sScan = "Пожалуйста, проверьте категорию и повторите попытку";
                        sScanTitle = "Ошибка регистрации \nЭко События";
                    } else {
                        sScan = "Передан неверный ключ";
                        sScanTitle = "Ошибка регистрации \nЭко События";
                    }

                } else {
                    sScan = "Передан неверный ключ";
                    sScanTitle = "Ошибка регистрации  \nЭко События";
                }
            }
        });
    }
}
