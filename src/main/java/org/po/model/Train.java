package org.po.model;

public abstract class Train  extends Thread  {

    public String number;
    public String operator;
    public double speed;
    public int train_id;

    public int getCurrent_station_id() {
        return current_station_id;
    }

    public void setCurrent_station_id(int current_station_id) {
        this.current_station_id = current_station_id;
    }

    public int getCurrent_destination_id() {
        return current_destination_id;
    }

    public void setCurrent_destination_id(int current_destination_id) {
        this.current_destination_id = current_destination_id;
    }

    private int current_station_id;
    private int current_destination_id;

    public Position getPosition() {
        return position;
    }

    public Position position;

    public Train(String number, String operator, double speed){
        this.number = number;
        this.operator = operator;
        this.speed = speed;
    }

    public int getTrain_id() {
        return train_id;
    }

    public void setTrain_id(int train_id) {
        this.train_id = train_id;
    }

    public abstract void addListener(Listener listener);

    public abstract void removeListener(Listener listener);

    public abstract double getNeighborProgress();
    public abstract void setNeighborProgress(double progress);

    public abstract String getOperator();

    public abstract String getTrainName();

    public abstract Station getCurrentStation();

    public abstract void setCurrentStation(Station station);

    public abstract Neighbor getNextNeighbor();

    public abstract void setNextNeighbor(Neighbor neighbor);

    public abstract void startTrain();

    public abstract void stopTrain();

    public abstract String getTrainData();

    //co autor miał na myśli:
//    nadaj kurs i ustaw startowe dane (np magazyn na płaszowie aby pociąg nie pojawiał się znikąd)
    public abstract void initialize(Boolean isRunning, Station currentStation,Neighbor current_connection, double connectionProgress, int train_id);

}
