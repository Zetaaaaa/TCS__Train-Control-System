package org.po.model;

public abstract class Train {

    public String number;
    public String operator;
    public double speed;

    public Train(String number, String operator, double speed) {
        this.number = number;
        this.operator = operator;
        this.speed = speed;
    }

    public abstract void start();

    public abstract void stop();

    public abstract String getTrainData();

    //co autor miał na myśli:
//    nadaj kurs i ustaw startowe dane (np magazyn na płaszowie aby pociąg nie pojawiał się znikąd)
    public abstract void initialize(Boolean isRunning, Station currentStation,Neighbor current_connection, double connectionProgress);

}
