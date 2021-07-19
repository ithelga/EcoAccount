package com.ecoaccount.pages;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.ecoaccount.Main;
import com.ecoaccount.Server;
import com.ecoaccount.boards.HistoryBoard;
import com.ecoaccount.boards.ListBoard;
import com.ecoaccount.ldutils.LdMask;
import com.ecoaccount.ldutils.ui.LdButton;
import com.ecoaccount.ldutils.ui.LdScroll;
import com.ecoaccount.ldutils.ui.LdText;

import static com.ecoaccount.Main.HALF_HEIGHT;
import static com.ecoaccount.Main.HALF_WIDHT;
import static com.ecoaccount.Main.HEIGHT;
import static com.ecoaccount.Main.WIDTH;
import static com.ecoaccount.Main.clDark;
import static com.ecoaccount.Main.clLight;

/**
 * Created by Helga on
 */
public class ListPage {

    public Main main;
    public NinePatch field;
    private LdButton butAdd, butOk;
    private boolean isDialog;
    private Array<ListBoard> listBoards;
    private TextArea textArea;
    private LdScroll scroll;


    public ListPage(Main m) {
        this.main = m;
        field = new NinePatch(new Texture("images/nine_note.png"), 40, 40, 40, 40);
        listBoards = new Array<>();
        butAdd = new LdButton(new TextureRegion(new Texture("images/but_add.png")),
                HALF_WIDHT + 375, HEIGHT - 335, 60, 60, new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isDialog = true;
                butOk.setVisible(true);
                butAdd.setVisible(false);
                textArea.setVisible(true);
                for (ListBoard l : listBoards) {
                    l.butCheck.setVisible(false);
                }
            }
        });


        butOk = new LdButton(new TextureRegion(new Texture("images/but_short.png")), HALF_WIDHT - 190, HALF_HEIGHT - 250, 380, 100,
                new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        isDialog = false;
                        butOk.setVisible(false);
                        textArea.setVisible(false);
                        butAdd.setVisible(true);
                        for (ListBoard l : listBoards) {
                            l.butCheck.setVisible(true);
                        }
                        addNote();
                        textArea.getOnscreenKeyboard().show(false);
                        main.stage.setKeyboardFocus(null);


                    }
                });
        butOk.addText("ОК", 48, 0, 70, 380, Color.WHITE, Align.center, false);
        butOk.setLimPres(0.25f);

        textArea = new TextArea("", main.areaStyle);
        textArea.setAlignment(Align.center);
//        textArea.setOnlyFontChars(true);
        textArea.setBounds(HALF_WIDHT - 465, butOk.getY() + 150, 930, 385);
        textArea.setMaxLength(1000);

        scroll = new LdScroll();
        scroll.setMissRatio(1.5f);
    }

    public void show() {
        main.page = Main.Pages.List;
        main.stage.clear();
        main.menuPanel.show();
        main.stage.addActor(butAdd);
        main.stage.addActor(butOk);
        main.stage.addActor(textArea);
        textArea.setVisible(false);
        butOk.setVisible(false);
        butAdd.setVisible(true);
        for (ListBoard l : listBoards) {
            l.butCheck.setVisible(true);
        }
        notes();
    }

    public void draw(SpriteBatch batch, float delta) {
        scroll.update(delta);
        batch.draw(main.whiteTex, 0, 0, WIDTH, HEIGHT);
        main.panelGray.draw(batch, 0, 250, WIDTH, HEIGHT - 250);
        LdText.draw("Заметки", 110, 0, HEIGHT - 255, WIDTH, clDark, Align.center, false);
        butAdd.draw(batch);
        butAdd.setLimPres(0.25f);

        LdMask.begin(batch);
        LdMask.setRect(0, 330, WIDTH, HEIGHT - 760);
        LdMask.flush();

        float y = HEIGHT - 390;
        for (ListBoard board : listBoards) {
            y -= board.getHeight() + 40;
            board.draw(batch, y + scroll.get());
        }
        LdMask.end();
    }

    public void drawDialog(SpriteBatch batch) {
        if (!isDialog) return;
        batch.setColor(0, 0, 0, 0.5f);
        batch.draw(main.whiteTex, 0, 0, WIDTH, HEIGHT);
        batch.setColor(Color.WHITE);
        batch.draw(main.whiteTex, 0, butOk.getY() - 50, WIDTH, 800);
        textArea.draw(batch, 1);
        butOk.draw(batch);
        LdText.draw("Новая заметка", 64, 110, butOk.getY() + 685, WIDTH - 220, clLight, Align.center, true);
    }


    public void notes() {
        main.server.notes(main.userId, new Server.Response() {
            @Override
            public void result(Server.State state, String data) {
//                System.out.println("Server notes is " + state.name() + ", data: " + data);
                if (state == Server.State.Complete) {
                    JsonValue json = main.server.parseData(data);
                    int s = json.getInt("state", 0);
                    if (s == 1) {
                        listBoards.clear();
                        scroll.setSize(0);
                        scroll.toBegin();
                        scroll.stop();
                        JsonValue jsonActions = json.get("actions");
                        if (jsonActions.size > 0) {
                            for (JsonValue val = jsonActions.child; val != null; val = val.next) {
                                listBoards.add(new ListBoard(ListPage.this, val));
                                scroll.addSize(listBoards.peek().getHeight() + 40);
                            }
                            scroll.addSize(-(HEIGHT - 760));
                            scroll.removeNegativeSize();
                        }
                    }
                }
            }
        });
    }

    private void addNote() {

        main.server.addNote(main.userId, textArea.getText(), new Server.Response() {
            @Override
            public void result(Server.State state, String data) {
//                System.out.println("Server addNote is " + state.name() + ", data: " + data);
                if (state == Server.State.Complete) {
                    JsonValue json = main.server.parseData(data);
                    int s = json.getInt("state", 0);
                    if (s == 1) {
                        textArea.setText("");
                        notes();
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
