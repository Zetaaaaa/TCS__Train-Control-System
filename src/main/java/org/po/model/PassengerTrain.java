package org.po.model;

import java.util.ArrayList;
import java.util.List;

public class PassengerTrain extends Train {


    private String number;
    private String operator;
    private Boolean running;

    private Neighbor currentConnection;
    private double connectionProgress;
    private Station currentStation;
    private int passengerCapacity;
    private boolean hasDiningCart;
    private boolean requiresMaintenance;
    private Position position = new Position(0,0);

    private List<Listener> listeners = new ArrayList<>();

    public Position getPosition() {
        return this.position;
    }



    public PassengerTrain(PassengerTrainBuilder builder){
        super(builder.number, builder.operator, builder.speed);
        this.running = builder.running;
        this.currentConnection = builder.current_connection;
        this.connectionProgress = builder.connection_progress;
        this.currentStation = builder.current_station;
        this.passengerCapacity = builder.passengerCapacity;
        this.hasDiningCart = builder.hasDiningCart;
        this.requiresMaintenance = builder.requiresMaintenance;
    }


    public static class PassengerTrainBuilder {

        private String number;
        private String operator;
        private double speed;
        private Boolean running = true;


        private Neighbor current_connection;
        private double connection_progress;
        private Station current_station;
        private int passengerCapacity;
        private Boolean requiresMaintenance = false;
        private Boolean hasDiningCart = false;

        public PassengerTrainBuilder(String number, String operator, double speed) {
            this.number = number;
            this.operator = operator;
            this.speed = speed;
        }

        public PassengerTrainBuilder setPassengerCapacity(int capacity) {
            this.passengerCapacity = capacity;
            return this;
        }

        public PassengerTrainBuilder setRequiresMaitenance(boolean requiresMaintenance) {
            this.requiresMaintenance = requiresMaintenance;
            return this;
        }

        public PassengerTrainBuilder setHasDiningCart(boolean hasDiningCart) {
            this.hasDiningCart = hasDiningCart;
            return this;
        }

        public PassengerTrain build() {
            if (number == null || operator == null){
                throw new IllegalArgumentException("PassengerTrain number or operator is null");
            }
            else {
                return new PassengerTrain(this);
            }

        }
    }


    @Override
    public void setCurrentStation(Station station){
        this.currentStation=station;
    }
    @Override
    public void setNextNeighbor(Neighbor neighbor){
        this.currentConnection = neighbor;
    }

    @Override
    public void setNeighborProgress(double progress){
        this.connectionProgress = progress;
    }

    @Override
    public String getTrainData() {
        return "Number: " + super.number +"\n" +
                "Operator: " + super.operator + "\n"+
                "Capacity: " +passengerCapacity+"People \n"+
                "Current connection: " + currentConnection + "\n"+
                "Has dining cart: " + hasDiningCart + "\n"+
                "Requires maintenance: " + requiresMaintenance +"\n";
    }

    @Override
    public void initialize(Boolean isRunning, Station currentStation, Neighbor current_connection, double connectionProgress, int train_id) {
//        this.running = isRunning;
        this.running = true;
        this.getPosition().setX(currentStation.getPosition().getX());
        this.getPosition().setY(currentStation.getPosition().getY());
        this.currentStation = currentStation;
        this.connectionProgress = connectionProgress;
        this.currentConnection = current_connection;
        this.train_id = train_id;
    }

    public String getOperator() {
        return operator;
    }

    public double getNeighborProgress() {
        return connectionProgress;
    }

    public String getTrainName() {
        return this.number;
    }

    public Station getCurrentStation() {
        return this.currentStation;
    }

    public Neighbor getNextNeighbor() {
        return this.currentConnection;
    }

    public void setPosition(Position position) {

    }

    @Override
    public void startTrain() {

    }

    public void stopTrain() {
        running = false;
        interrupt();
    }

    @Override
    public void run() {
        while (running && !isInterrupted()) {
            try {
                progress(); // sleep or wait
            } catch (InterruptedException e) {
                // Thread was interrupted -> exit gracefully
                running = false; // optional: ensure loop stops
                Thread.currentThread().interrupt(); // preserve interrupt status
                break; // exit loop
            }
        }

        // optional cleanup
        System.out.println("Train thread has been stopped");
    }

    public void progress() throws InterruptedException {
        //setPosition(new Position(getPosition().getX()+10,getPosition().getY()+10));
        Thread.sleep(50);
        notifyListeners();
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    private void notifyListeners() {
        for (Listener listener : listeners) {
            listener.update();
        }
    }
}
