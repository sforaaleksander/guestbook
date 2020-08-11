package com.codecool.guestbook;

import java.util.Date;

public class Note {
    private final int id;
    private String name;
    private String message;
    private String date;

    public Note(int id, String name, String message, String date) {
        this.id = id;
        this.name = name;
        this.message = message;
        this.date = date;
    }

    public int getId() {
        return id;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
