package org.po.model;

import javafx.geometry.Pos;

import java.util.ArrayList;

public class Station extends Entity{
    private String city;
    private ArrayList<Neighbor> connections;

    public String getName() {
        return name;
    }
    public Position getPosition() {
        return this.position;
    }

    public String getCity() {
        return city;
    }

    public ArrayList<Neighbor> getConnections() {
        return connections;
    }

    public void addConnection(Station _neighbor) {
        this.connections.add(new Neighbor(this, _neighbor));
    }

    public Station(String name, Position position,String city, ArrayList<Neighbor> connections) {
        this.name = name;
        this.position = position;
        this.city = city;
        this.connections = connections;
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
