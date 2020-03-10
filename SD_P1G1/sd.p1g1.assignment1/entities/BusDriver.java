package entities;

import java.util.*;

import sharedRegions.*;

public class BusDriver extends Thread {
    
    private Queue passengerQueue;
    int timeToWait;

    BusDriverState state;

    BusDriver (BusDriverState state){
        this.state = state;
        //System.out.println("Does nothing");
    }


    @Override
    public void run(){
        while(arrivalTermTransfQuay.hasDaysWorkEnded() != 'E'){
            arrivalTermTransfQuay.annoucingBusBoarding();						
            goToDepartureTerminal();
            departureTermTransfQuay.parkTheBusAndLetPassengerOff();
            goToArrivalTerminal();
            arrivalTermTransfQuay.parkTheBus();
        }
    }

    public void setState(BusDriverState state) {
        this.state = state;
    }
    public BusDriverState getBDriverState() {
        return this.state;
    }
    /*
    public BusDriverState getState() {
        return this.state;
    }
    */
    void goToDepartureTerminal(){

    }
    void goToArrivalTerminal(){

    }

}
    