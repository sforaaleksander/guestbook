package com.codecool.guestbook;

import java.util.Date;

public class Note {
    private int id;
    private final String name;
    private final String message;
    private final String date;

    public Note(int id, String name, String message, String date) {
        this.id = id;
        this.name = name;
        this.message = message;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }
}
