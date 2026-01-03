package org.po.model;

public class Connection {
    Station station ;
    String destination;

    public Connection(Station station, String destination) {
        this.station = station;
        this.destination = destination;
    }

}
