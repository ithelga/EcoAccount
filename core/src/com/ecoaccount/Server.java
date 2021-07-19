package com.ecoaccount;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.net.HttpParametersUtils;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Maxim on 23.02.2020
 */
public class Server {

    public enum State {Complete, Failed, Canceled}

    public interface Response {
        void result(State state, String data);
    }

    private static final String API_TOKEN = "gjhsrgeaderk7jfge";
    private static final String API_URL = "https://ldm.im/eco/api/server";

    private Main main;

    public JsonReader jsonReader;

    public Server(Main main) {
        this.main = main;
        jsonReader = new JsonReader();
    }

    public JsonValue parseData(String data) {
        return jsonReader.parse(data);
    }

    private boolean isValidData(String data) {
        return data != null && !data.isEmpty() && !data.toLowerCase().startsWith("<!doctype") && !data.toLowerCase().contains("stack trace") && !data.toLowerCase().contains("<b>warning</b>");
    }

    private void listenResponse(Net.HttpRequest request, Response response) {

        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                String data = httpResponse.getResultAsString();
                Gdx.app.postRunnable(() -> {
//                    System.out.println(data);
                    if (isValidData(data)) response.result(State.Complete, data);
                    else response.result(State.Failed, "Data is null");
                });
            }

            @Override
            public void failed(Throwable t) {
                Gdx.app.postRunnable(() -> response.result(State.Failed, t.getMessage()));
            }

            @Override
            public void cancelled() {
                Gdx.app.postRunnable(() -> response.result(State.Canceled, ""));
            }
        });
    }

    private Net.HttpRequest createClientRequest(String action, JsonValue json) {
        json.addChild("app_token", new JsonValue(API_TOKEN));
//        json.addChild("auth_token", new JsonValue(app.user.getAuthToken()));
//        json.addChild("user_id", new JsonValue(Integer.toString(app.user.getId())));
        json.addChild("action", new JsonValue(action));

        String jsonString = json.toJson(JsonWriter.OutputType.json);
        Map<String, String> data = new HashMap<>();
        data.put("data", jsonString);
        Net.HttpRequest request = new Net.HttpRequest(Net.HttpMethods.POST);
        request.setUrl(API_URL);
        request.setContent(HttpParametersUtils.convertHttpParameters(data));
        request.setHeader("Content-Type", "application/x-www-form-urlencoded");
        return request;
    }


    public void auth(String login, String password, final Response response) {
        JsonValue json = new JsonValue(JsonValue.ValueType.object);
        json.addChild("login", new JsonValue(login));
        json.addChild("password", new JsonValue(password));
        listenResponse(createClientRequest("auth", json), response);
    }

    public void register(String login, String password, String inn, String firstName, String lastName, final Response response) {
        JsonValue json = new JsonValue(JsonValue.ValueType.object);
        json.addChild("login", new JsonValue(login));
        json.addChild("password", new JsonValue(password));
        json.addChild("inn", new JsonValue(inn));
        json.addChild("first_name", new JsonValue(firstName));
        json.addChild("last_name", new JsonValue(lastName));
        listenResponse(createClientRequest("register", json), response);
    }

    public void scan(int userId, String type, String qrKey, int count, final Response response) {
        JsonValue json = new JsonValue(JsonValue.ValueType.object);
        json.addChild("user_id", new JsonValue(userId));
        json.addChild("type", new JsonValue(type));
        json.addChild("qr_key", new JsonValue(qrKey));
        json.addChild("count", new JsonValue(count));
        listenResponse(createClientRequest("scan", json), response);
    }

    public void rating(int userId, final Response response) {
        JsonValue json = new JsonValue(JsonValue.ValueType.object);
        json.addChild("user_id", new JsonValue(userId));
        listenResponse(createClientRequest("rating", json), response);
    }

    public void scores(int userId, String type, final Response response) {
        JsonValue json = new JsonValue(JsonValue.ValueType.object);
        json.addChild("user_id", new JsonValue(userId));
        json.addChild("type", new JsonValue(type));
        listenResponse(createClientRequest("scores", json), response);
    }

    public void history(int userId, final Response response) {
        JsonValue json = new JsonValue(JsonValue.ValueType.object);
        json.addChild("user_id", new JsonValue(userId));
        listenResponse(createClientRequest("history", json), response);
    }

    public void notes(int userId, final Response response) {
        JsonValue json = new JsonValue(JsonValue.ValueType.object);
        json.addChild("user_id", new JsonValue(userId));
        listenResponse(createClientRequest("notes", json), response);
    }

    public void changeDone(int id, int done, final Response response) {
        JsonValue json = new JsonValue(JsonValue.ValueType.object);
        json.addChild("id", new JsonValue(id));
        json.addChild("done", new JsonValue(done));
        listenResponse(createClientRequest("change_done", json), response);
    }

    public void addNote(int userId, String text, final Response response) {
        JsonValue json = new JsonValue(JsonValue.ValueType.object);
        json.addChild("user_id", new JsonValue(userId));
        json.addChild("text", new JsonValue(text));
        listenResponse(createClientRequest("add_note", json), response);
    }
}