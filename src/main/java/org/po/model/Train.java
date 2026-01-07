package org.po.model;

public abstract class Train  extends Thread  {

    public String number;
    public String operator;
    public double speed;

    public Train(String number, String operator, double speed){
        this.number = number;
        this.operator = operator;
        this.speed = speed;
    }


    public abstract void addListener(Listener listener);

    public abstract void removeListener(Listener listener);

    public abstract double getNeighborProgress();
    public abstract void setNeighborProgress(double progress);

    public abstract String getOperator();

    public abstract String getTrainName();

    public abstract Station getCurrentStation();

    public abstract Neighbor getNextNeighbor();

    public abstract void startTrain();

    public abstract void stopTrain();

    public abstract String getTrainData();

    //co autor miał na myśli:
//    nadaj kurs i ustaw startowe dane (np magazyn na płaszowie aby pociąg nie pojawiał się znikąd)
    public abstract void initialize(Boolean isRunning, Station currentStation,Neighbor current_connection, double connectionProgress);

}
