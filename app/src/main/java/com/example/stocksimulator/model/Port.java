package com.example.stocksimulator.model;

import com.example.stocksimulator.handler.ConnectionHandler;

public class Port {

    private static Port port = new Port();
    private ConnectionHandler client;

    private Port() {}

    public static Port getInstance() {
        return port;
    }

    public void setClient(ConnectionHandler client) {
        this.client = client;
    }

    public ConnectionHandler getClient() {
        return client;
    }
}
