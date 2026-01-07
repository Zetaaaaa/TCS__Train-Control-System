package org.po.model;

public class PassengerTrain extends Train {


    private String number;
    private String operator;
    private Boolean running = false;

    public String getOperator() {
        return operator;
    }

    private Neighbor current_connection;
    private double connection_progress;
    private Station current_station;
    private int passengerCapacity;
    private boolean hasDiningCart;
    private boolean requiresMaintenance;
    private Position position = new Position(0,0);
    public Position getPosition() {
        return this.position;
    }

    public double getNeighborProgress() {
        return connection_progress;
    }

    public String getName() {
        return this.number;
    }

    public Station getCurrentStation() {
        return this.current_station;
    }

    public Neighbor getNextNeighbor() {
        return this.current_connection;
    }


    public PassengerTrain(PassengerTrainBuilder builder){
        super(builder.number, builder.operator, builder.speed);

        this.running = builder.running;
        this.current_connection = builder.current_connection;
        this.connection_progress = builder.connection_progress;
        this.current_station = builder.current_station;
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
    public void start() {
        if(!this.running){
            this.running = true;
        }

    }

    @Override
    public void stop() {
        if(this.running){
            this.running = false;
        }
    }

    @Override
    public String getTrainData() {
        return "Number: " + super.number +"\n" +
                "Operator: " + super.operator + "\n"+
                "Capacity: " +passengerCapacity+"People \n"+
                "Current connection: " + current_connection + "\n"+
                "Has dining cart: " + hasDiningCart + "\n"+
                "Requires maintenance: " + requiresMaintenance +"\n";
    }

    @Override
    public void initialize(Boolean isRunning, Station currentStation, Neighbor current_connection, double connectionProgress) {
        this.running = isRunning;
        this.getPosition().setX(currentStation.getPosition().getX());
        this.getPosition().setY(currentStation.getPosition().getY());
        this.current_station = currentStation;
        this.connection_progress = connectionProgress;
        this.current_connection = current_connection;
    }


}
