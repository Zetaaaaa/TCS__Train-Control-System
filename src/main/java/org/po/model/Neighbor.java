package org.po.model;

public class Neighbor {
    Station station ;
    Station destination;

    public Station getDestination() {
        return destination;
    }

    public Neighbor(Station station, Station destination) {
        this.station = station;
        this.destination = destination;
    }

}
