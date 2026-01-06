package org.po.model;

import java.util.ArrayList;

public class Station implements Entity{
    private String city;
    private ArrayList<Neighbor> connections;

    public String getName() {
        return name;
    }
    public String getPosition() {
        return this.getCity();
    }

    public String getCity() {
        return city;
    }

    public ArrayList<Neighbor> getConnections() {
        return connections;
    }

    public Station(String city, ArrayList<Neighbor> connections) {
        this.city = city;
        this.connections = connections;
    }
}
