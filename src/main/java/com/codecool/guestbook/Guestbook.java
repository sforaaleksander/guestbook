package com.codecool.guestbook;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Guestbook implements HttpHandler {
    private static List<Note> notes;

    public Guestbook() {
        notes = new ArrayList<>();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();

        if(method.equals("POST")){
            System.out.println("posting");
            postNote(httpExchange);
        }
        
        String requestURI = httpExchange.getRequestURI().toString();
        System.out.println(requestURI);

        if (requestURI.contains("delete")){
            System.out.println("now deleting");
            deleteNote(httpExchange);
        }
        if (requestURI.contains("update")){
            System.out.println("now updating");
            updateNote(httpExchange);
            return;
        }

        if(method.equals("POST") && requestURI.contains("update")) {
            saveUpdatedNote(httpExchange);
        }
            getNotes(httpExchange);
        System.out.println(notes.size());
    }

    private void saveUpdatedNote(HttpExchange httpExchange) {
        int noteId = getNoteId(httpExchange);
        Note note = notes.get(noteId);
    }

    private void updateNote(HttpExchange httpExchange) throws IOException {
        String response;
        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/update_template.twig");
        JtwigModel model = JtwigModel.newModel();
        int noteId = getNoteId(httpExchange);
        model.with("note", notes.get(noteId));
        response = template.render(model);
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private void deleteNote(HttpExchange httpExchange) throws IOException {
        int noteId = getNoteId(httpExchange);
        notes.remove(noteId);
        Headers responseHeaders = httpExchange.getResponseHeaders();
        responseHeaders.set("Location", "/guestbook");
        httpExchange.sendResponseHeaders(302,0);
    }

    private int getNoteId(HttpExchange httpExchange) {
        String[] elements = httpExchange.getRequestURI().toString().split("/");
        return Integer.parseInt(elements[elements.length-1]);
    }

    private void getNotes(HttpExchange httpExchange) throws IOException {
        String response;
        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/template.twig");
        JtwigModel model = JtwigModel.newModel();
        model.with("notes", notes);
        response = template.render(model);
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

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        String strDate= formatter.format(date);

        System.out.println(inputs.get("name"));
        System.out.println(inputs.get("message"));

        notes.add(new Note(notes.size(), inputs.get("name"), inputs.get("message"), strDate));
    }

    private static Map<String, String> parseFormData(String formData) throws UnsupportedEncodingException {
        Map<String, String> map = new HashMap<>();
        String[] pairs = formData.split("&");
        for(String pair : pairs){
            String[] keyValue = pair.split("=");
            // We have to decode the value because it's urlencoded. see: https://en.wikipedia.org/wiki/POST_(HTTP)#Use_for_submitting_web_forms
            String key = URLDecoder.decode(keyValue[0], "UTF-8");
            String value = URLDecoder.decode(keyValue[1], "UTF-8");
//            if (key.length() != 0 && value.length() != 0){
            map.put(key, value);
//            }
        }
        return map;
    }
}