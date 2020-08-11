package com.codecool.guestbook;

import java.util.Date;

public class Note {
    private final String name;
    private final String message;
    private final String date;

    public Note(String name, String message, String date) {
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
