package org.po.model;

public class CargoTrain extends Train {

    //super.number zastępuje
    private String number;
    private String operator;
    private Boolean running = false;

    private Neighbor currentConnection;
    private double connectionProgress;
    private Station currentStation;
    private int cargoCapacity;
    private boolean hasDiningCart;
    private boolean requiresMaintenance;
    private Position position = new Position(0,0);

    private CargoTrain(CargoTrainBuilder builder) {
        super(builder.number, builder.operator, builder.speed);
        this.currentConnection = builder.currentConnection;
        this.connectionProgress = builder.connectionProgress;
        this.currentStation = builder.currentStation;
        this.cargoCapacity = builder.cargoCapacity;
        this.hasDiningCart = builder.hasDiningCart;
        this.requiresMaintenance = builder.requiresMaintenance;
    }

    public static class CargoTrainBuilder {

        //required
        private String number;
        private String operator;
        private Double speed;
        private int cargoCapacity;

        private Boolean requiresMaintenance = false;
        private Boolean hasDiningCart = false;

        //optional
        private Neighbor currentConnection;
        private double connectionProgress;
        private Station currentStation;

        public CargoTrainBuilder(String number, String operator, double speed) {
            this.number = number;
            this.operator = operator;
            this.speed = speed;
        }

        public CargoTrainBuilder setCargoCapacity(int cargoCapacity) {
            this.cargoCapacity = cargoCapacity;
            return this;
        }

        public CargoTrainBuilder setRequiresMaitenance(boolean requiresMaintenance) {
            this.requiresMaintenance = requiresMaintenance;
            return this;
        }

        public CargoTrainBuilder setHasDiningCart(boolean hasDiningCart) {
            this.hasDiningCart = hasDiningCart;
            return this;
        }

        public CargoTrain build(){
            if(number == null || operator == null){
                throw new IllegalArgumentException("Cargotrain number or operator is null");
            }
            else{
                return new CargoTrain(this);
            }

        }
    }

    @Override
    public void stopTrain() {
        if(this.running){
            this.running = false;
        }
    }
    @Override
    public void setNeighborProgress(double progress){
        this.connectionProgress = progress;
    }
    @Override
    public String getTrainData() {
        //zamiast super.cos mozna wywalic konstruktor nadrzedny i zainicjalizować w tej klasie
        return "Number: " + super.number +"\n" +
                "Operator: " + super.operator+  "\n"+
                "Capacity: " + cargoCapacity + " kgs \n" +
                "Current connection: " + currentConnection + "\n"+
                "Has dining cart: " + hasDiningCart +"\n"+
                "Requires maintenance: "+ requiresMaintenance+ "\n";
    }

    @Override
    public void initialize(Boolean isRunning, Station currentStation,Neighbor current_connection, double connectionProgress) {

    }

    public String getOperator() {
        return operator;
    }

    @Override
    public void addListener(Listener listener) {

    }

    @Override
    public void removeListener(Listener listener) {

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

    @Override
    public void startTrain() {

    }

    @Override
    public void run() {
        System.out.println("CargoTrain started");
        super.run();
    }
}
