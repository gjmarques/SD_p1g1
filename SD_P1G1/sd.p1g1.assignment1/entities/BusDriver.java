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
                departureTermTransfQuay.parkTheBusAndLetPassengerOff(nPassengers);
                goToArrivalTerminal();
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
            Thread.sleep(100);
        } catch (Exception e) {}

    }
    void goToArrivalTerminal(){
        try {
            Thread.sleep(100);
        } catch (Exception e) {}

    }

}
    