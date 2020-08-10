package com.codecool;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {

    public static void main( String[] args ) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(9000), 0);

        // set routes
        server.createContext("/hello", new Guestbook());
        server.setExecutor(null); // creates a default executor

        // start listening
        server.start();
    }
}
