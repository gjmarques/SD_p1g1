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

    /**
     * This method defines the life-cycle of the Bus Driver.
     */
    @Override
    public void run(){
        while(ArrivalTermTransfQuay.hasDaysWorkEnded() != 'E'){
            ArrivalTermTransfQuay.annoucingBusBoarding();						
            goToDepartureTerminal();
            DepartureTermTransfQuay.parkTheBusAndLetPassengerOff();
            goToArrivalTerminal();
            ArrivalTermTransfQuay.parkTheBus();
        }
    }

    
    /** 
     * @param state
     */
    public void setState(BusDriverState state) {
        this.state = state;
    }
    
    /** 
     * @return BusDriverState
     */
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
    