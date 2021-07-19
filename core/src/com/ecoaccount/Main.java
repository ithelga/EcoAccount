package com.ecoaccount;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.ecoaccount.ldutils.Graphics;
import com.ecoaccount.ldutils.LdDelta;
import com.ecoaccount.ldutils.LdMask;
import com.ecoaccount.ldutils.ui.LdText;
import com.ecoaccount.pages.AccountPage;
import com.ecoaccount.pages.HistoryPage;
import com.ecoaccount.pages.HomePage;
import com.ecoaccount.pages.ListPage;
import com.ecoaccount.pages.LoginPage;
import com.ecoaccount.pages.RatingPage;
import com.ecoaccount.pages.RegistrationPage;
import com.ecoaccount.pages.ScanPage;
import com.ecoaccount.panels.MenuPanel;

public class Main extends ApplicationAdapter implements InputProcessor {

    public static int WIDTH, HEIGHT, HALF_WIDHT, HALF_HEIGHT;
    public static float AR;

    public static Color clBackground = Color.valueOf("E9EBEA");
    public static Color clDark = Color.valueOf("6DAB92");
    public static Color clLight = Color.valueOf("98D1BA");

    private SpriteBatch batch;
    private OrthographicCamera camera;

    public Pages page;
    public Stage stage;
    public Texture whiteTex;
    public NinePatch panelGray, panelGreen;


    public String userFirstName, userLastName;
    public int userId, userScore, userMoney;
    public LoginPage loginPage;
    public RegistrationPage registrationPage;
    public HomePage homePage;
    public ScanPage scanPage;
    public AccountPage accountPage;
    public HistoryPage historyPage;
    public RatingPage ratingPage;
    public ListPage listPage;
    public BitmapFont inputFont;
    public TextField.TextFieldStyle inputStyle, areaStyle;
    public Server server;


    public MenuPanel menuPanel;
    public Platform platform;

    public enum Pages {Login, Registration, Home, History, Account, Scan, Rating, List}

    public Main(Platform p) {
        this.platform = p;
    }

    @Override
    public void create() {
        server = new Server(this);

        HEIGHT = 1920;
        WIDTH = HEIGHT * Gdx.graphics.getWidth() / Gdx.graphics.getHeight();
        if (WIDTH < 1080) {
            WIDTH = 1080;
            HEIGHT = WIDTH * Gdx.graphics.getHeight() / Gdx.graphics.getWidth();
        }
        HALF_WIDHT = WIDTH / 2;
        HALF_HEIGHT = HEIGHT / 2;
        AR = (float) WIDTH / Gdx.graphics.getWidth();

        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        stage = new Stage(new StretchViewport(WIDTH, HEIGHT, camera));
        Gdx.input.setInputProcessor(new InputMultiplexer(stage, this));


        whiteTex = Graphics.getColorTexture(Color.WHITE);

        LdMask.init(camera, Graphics.getEmptyTexture());
        LdText.init(batch, 2);
        LdText.add("fonts/reg48.fnt", 48, 57);
        LdText.add("fonts/reg110.fnt", 110, 130);
        LdText.add("fonts/med48.fnt", 48, 57, LdText.F2);
        LdText.add("fonts/med110.fnt", 110, 130, LdText.F2);
        inputFont = new BitmapFont(Gdx.files.internal("fonts/reg48.fnt"));
        inputFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        inputStyle = new TextField.TextFieldStyle(inputFont, Color.WHITE,
                new TextureRegionDrawable(whiteTex),
                new TextureRegionDrawable(Graphics.getColorTexture(clDark)),
                new TextureRegionDrawable(new Texture("images/input_field.png")));
        inputStyle.messageFontColor = Color.WHITE;
        inputStyle.cursor.setMinWidth(5);
        inputStyle.background.setLeftWidth(75);
        inputStyle.background.setRightWidth(75);

        areaStyle = new TextField.TextFieldStyle(inputFont, clDark,
                new TextureRegionDrawable(Graphics.getColorTexture(clDark)),
                new TextureRegionDrawable(Graphics.getColorTexture(clLight)),
                new TextureRegionDrawable(new Texture("images/text_area.png")));
        areaStyle.messageFontColor = clLight;
        areaStyle.cursor.setMinWidth(5);
        areaStyle.background.setLeftWidth(75);
        areaStyle.background.setRightWidth(75);
        areaStyle.background.setTopHeight(75);
        areaStyle.background.setBottomHeight(60);


        loginPage = new LoginPage(this);
        registrationPage = new RegistrationPage(this);
        homePage = new HomePage(this);
        scanPage = new ScanPage(this);
        accountPage = new AccountPage(this);
        historyPage = new HistoryPage(this);
        ratingPage = new RatingPage(this);
        listPage = new ListPage(this);
        loginPage.show();

        menuPanel = new MenuPanel(this);
        panelGreen = new NinePatch(new Texture("images/nine_start.png"), 160, 160, 0, 160);
        panelGray = new NinePatch(new Texture("images/nine_field.png"), 160, 160, 0, 160);
    }

    @Override
    public void render() {
        float delta = LdDelta.getDelta();
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) backPressed();

        Gdx.gl.glClearColor(clBackground.r, clBackground.g, clBackground.b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        if (page == Pages.Login) loginPage.draw(batch);
        else if (page == Pages.Registration) registrationPage.draw(batch);
        else if (page == Pages.Home) homePage.draw(batch);
        else if (page == Pages.Scan) scanPage.draw(batch);
        else if (page == Pages.Account) accountPage.draw(batch);
        else if (page == Pages.History) historyPage.draw(batch, delta);
        else if (page == Pages.Rating) ratingPage.draw(batch);
        else if (page == Pages.List) listPage.draw(batch, delta);

        if (page == Pages.Home || page == Pages.Scan || page == Pages.Account ||
                page == Pages.History || page == Pages.Rating || page == Pages.List)
            menuPanel.draw(batch);

        if (page == Pages.Scan) scanPage.drawDialog(batch);
        if (page == Pages.List) listPage.drawDialog(batch);


        batch.end();
    }

    public int getUserMoney() {
        return userScore * 10;
    }

    public void backPressed() {
        if (page == Pages.Login) return;
        else if (page == Pages.Registration || page == Pages.Home) loginPage.show();
        else {
            if (scanPage.isScan) {
                scanPage.isScan = false;
                platform.hideScanner();
            }
            homePage.show();
        }

    }

    @Override
    public void dispose() {
        batch.dispose();
    }


    @Override
    public boolean touchDown(int sX, int sY, int pointer, int button) {
        sX *= AR;
        sY = (int) (HEIGHT - sY * AR);
        if (page == Pages.List) listPage.touchDown(sX, sY);
        else if (page == Pages.History) historyPage.touchDown(sX, sY);
        return false;
    }

    @Override
    public boolean touchUp(int sX, int sY, int pointer, int button) {
        sX *= AR;
        sY = (int) (HEIGHT - sY * AR);
        if (page == Pages.List) listPage.touchUp(sX, sY);
        else if (page == Pages.History) historyPage.touchUp(sX, sY);
        return false;
    }

    @Override
    public boolean touchDragged(int sX, int sY, int pointer) {
        sX *= AR;
        sY = (int) (HEIGHT - sY * AR);
        if (page == Pages.List) listPage.touchDragged(sX, sY);
        else if (page == Pages.History) historyPage.touchDragged(sX, sY);
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        if (page == Pages.List) listPage.scroll(amount);
        else if (page == Pages.History) historyPage.scroll(amount);
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }


}
