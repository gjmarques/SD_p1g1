package entities;

import sharedRegions.*;

public class BusDriver extends Thread {

    private int nPassengers = 0;
    private boolean loop = true;
    private BusDriverState state;
    private final ArrivalTermTransfQuay arrivalTermTransfQuay;
    private final DepartureTermTransfQuay departureTermTransfQuay;

    public BusDriver (ArrivalTermTransfQuay arrivalTermTransfQuay, DepartureTermTransfQuay departureTermTransfQuay){
        this.state = BusDriverState.PARKING_AT_THE_ARRIVAL_TERMINAL;
        this.arrivalTermTransfQuay = arrivalTermTransfQuay;
        this.departureTermTransfQuay = departureTermTransfQuay;
    }

    /**
     * This method defines the life-cycle of the Bus Driver.
     */
    @Override
    public void run(){

        while(loop){
            char choice = arrivalTermTransfQuay.hasDaysWorkEnded();
            if(choice == 'W') {
                nPassengers = arrivalTermTransfQuay.annoucingBusBoarding();			
                goToDepartureTerminal();
                setState(BusDriverState.PARKING_AT_THE_DEPARTURE_TERMINAL);
                departureTermTransfQuay.parkTheBusAndLetPassengerOff(nPassengers);
                goToArrivalTerminal();
                setState(BusDriverState.PARKING_AT_THE_ARRIVAL_TERMINAL);
                arrivalTermTransfQuay.parkTheBus();
            }else if(choice == 'E'){
                loop = false;
            }
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

    void goToDepartureTerminal(){
        try {
            setState(BusDriverState.DRIVING_FORWARD); 
            Thread.sleep(50);
        } catch (Exception e) {}

    }
    void goToArrivalTerminal(){
        try {
            setState(BusDriverState.DRIVING_BACKWARD); 
            Thread.sleep(50);
        } catch (Exception e) {}

    }

}
    