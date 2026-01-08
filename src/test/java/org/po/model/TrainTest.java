package org.po.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class TrainTest {
    Train trainPassenger = TrainFactory.getTrain("passeNGER","EIP123","IC",190,450,true,false);

    @org.junit.Test
    public void getTrain_id() {
        Station station1 = new Station("test",new Position(0,0),"testowe",null);
        Station station2 = new Station("test",new Position(0,0),"testowe",null);
        trainPassenger.initialize(true,station1,new Neighbor(station1,station2),0,1);
        assertEquals(1, trainPassenger.getTrain_id());
    }

    @Test
    public void getCurrentStation() {
        Station station1 = new Station("test",new Position(0,0),"testowe",null);
        Station station2 = new Station("test",new Position(0,0),"testowe",null);
        trainPassenger.initialize(true,station1,new Neighbor(station1,station2),0,1);
        assertEquals(station1, trainPassenger.getCurrentStation());
    }

    @Test
    public void getNextNeighborDestination() {
        Station station1 = new Station("test",new Position(0,0),"testowe",null);
        Station station2 = new Station("test",new Position(0,0),"testowe",null);
        trainPassenger.initialize(true,station1,new Neighbor(station1,station2),0,1);
        assertEquals(station2, trainPassenger.getNextNeighbor().getDestination());
    }
}