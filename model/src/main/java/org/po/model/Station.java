package org.po.model;

import java.util.ArrayList;

public class Station {
    private String city;
    private ArrayList<Connection> connections;

    public Station(String city, ArrayList<Connection> connections) {
        this.city = city;
        this.connections = connections;
    }
}
