package org.po.model;

public class Neighbor {
    Station station;
    Station destination;

    public Station getDestination() {
        return destination;
    }

    public Station getSource() {
        return station;
    }

    public Neighbor(Station station, Station destination) {
        this.station = station;
        this.destination = destination;
    }

    public double getDistance() {
        double dx = destination.getPosition().getX() - station.getPosition().getX();
        double dy = destination.getPosition().getY() - station.getPosition().getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public String toString() {
        return destination.getName();
    }
}
