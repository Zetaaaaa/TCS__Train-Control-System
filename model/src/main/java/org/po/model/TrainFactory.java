package org.po.model;

public class TrainFactory extends Entity {

    public static Train getTrain(String type, String number, String operator, int capacity, boolean dining, boolean maintenance){
        if("CARGO".equalsIgnoreCase(type)){
//           funkcje ustawiające dane po kropce aby konstruktor nie był za długi a sam obiekt modularny
            return new CargoTrain.CargoTrainBuilder(number, operator)
                    .setCargoCapacity(capacity)
                    .setHasDiningCart(dining)
                    .setRequiresMaitenance(maintenance)
                    .build();
        }
        else if("PASSENGER".equalsIgnoreCase(type)){
            return new PassengerTrain.PassengerTrainBuilder(number, operator)
                    .setPassengerCapacity(capacity)
                    .setRequiresMaitenance(maintenance)
                    .setHasDiningCart(dining)
                    .build();
        }
        return null;
    };

}