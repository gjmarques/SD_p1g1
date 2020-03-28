package entities;

import sharedRegions.*;

public class BusDriver extends Thread {

    private int nPassengers = 0;
    private boolean loop = true;
    private BusDriverState state;
    private final ArrivalTermTransfQuay arrivalTermTransfQuay;
    private final DepartureTermTransfQuay departureTermTransfQuay;

    public BusDriver (ArrivalTermTransfQuay arrivalTermTransfQuay, DepartureTermTransfQuay departureTermTransfQuay){
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
                departureTermTransfQuay.goToDepartureTerminal();
                departureTermTransfQuay.parkTheBusAndLetPassengerOff(nPassengers);
                arrivalTermTransfQuay.goToArrivalTerminal();
                arrivalTermTransfQuay.parkTheBus();
            }else if(choice == 'E'){
        // rep.busDriverState(BusDriverState.PARKING_AT_THE_ARRIVAL_TERMINAL);
                loop = false;
            }
        }
    }
    /** 
     * @return {@link BusDriverState}
     */
    public BusDriverState getBDriverState() {
        return this.state;
    }

}
    