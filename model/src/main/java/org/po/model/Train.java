package org.po.model;

public abstract class Train {

    public String number;
    public String operator;

    public Train(String number, String operator) {
        this.number = number;
        this.operator = operator;
    }

    public abstract void start();

    public abstract void stop();

    public abstract String getTrainData();

    //co autor miał na myśli:
//    nadaj kurs i ustaw startowe dane (np magazyn na płaszowie aby pociąg nie pojawiał się znikąd)
    public abstract void initialize(Boolean isRunning, Station currentStation, double connectionProgress);

}
