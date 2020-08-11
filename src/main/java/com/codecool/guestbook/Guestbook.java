package com.codecool.guestbook;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Guestbook implements HttpHandler {
    private List<Note> notes;

    public Guestbook() {
        this.notes = new ArrayList<>();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        String method = httpExchange.getRequestMethod();

        String response = "";

        if(method.equals("POST")){
            System.out.println("posting");
            postNote(httpExchange);
        }
        getNotes(httpExchange);
        System.out.println(notes.size());
    }

    private void getNotes(HttpExchange httpExchange) throws IOException {
        String response;
        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/template.twig");

        JtwigModel model = JtwigModel.newModel();

        response = template.render(model);

        model.with("notes", notes);

        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private void postNote(HttpExchange httpExchange) throws IOException {
        InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);
        String formData = br.readLine();

        System.out.println(formData);
        Map<String, String> inputs = parseFormData(formData);

        System.out.println(inputs.get("name"));
        System.out.println(inputs.get("message"));

        notes.add(new Note(inputs.get("name"), inputs.get("message")));

    }

    private static Map<String, String> parseFormData(String formData) throws UnsupportedEncodingException {
        Map<String, String> map = new HashMap<>();
        String[] pairs = formData.split("&");
        for(String pair : pairs){
            String[] keyValue = pair.split("=");
            // We have to decode the value because it's urlencoded. see: https://en.wikipedia.org/wiki/POST_(HTTP)#Use_for_submitting_web_forms
            String key = new URLDecoder().decode(keyValue[0], "UTF-8");
            String value = new URLDecoder().decode(keyValue[1], "UTF-8");
//            if (key.length() != 0 && value.length() != 0){
            map.put(key, value);
//            }
        }
        return map;
    }
}