package com.codecool.guestbook;

import java.util.Date;

public class Note {
    private final String name;
    private final String message;
//    private final Date date;

    public Note(String name, String message) {
        this.name = name;
        this.message = message;
//        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }
//
//    public Date getDate() {
//        return date;
//    }
}
