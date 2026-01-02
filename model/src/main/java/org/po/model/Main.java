package org.po.model;

public class Main {
    static void main() {
//        System.out.println("Model");

        //Przykładowe dane

        Train trainCargo = TrainFactory.getTrain(TrainTypes.CARGO.toString(),"CRG534","KRKBSTS",50000,true,false);
        Train trainPassenger = TrainFactory.getTrain("passeNGER","EIP123","WAWAOK",450,true,false);


//       Test
        System.out.println(trainCargo.getTrainData());
        System.out.println(trainPassenger.getTrainData());

    }
}
