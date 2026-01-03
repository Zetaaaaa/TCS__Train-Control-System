package org.po.model;

import java.util.ArrayList;

public class Station {
    private String city;
    private ArrayList<Neighbor> connections;

    public Station(String city, ArrayList<Neighbor> connections) {
        this.city = city;
        this.connections = connections;
    }
}
