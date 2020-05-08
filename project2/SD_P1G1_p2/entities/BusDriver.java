package entities;

import sharedRegions.*;

/**
 * Implementation of the Bus Driver whom is responsible for getting the {@link Passenger}s from
 * {@link sharedRegions.ArrivalTermTransfQuay} and leaving them at {@link sharedRegions.DepartureTermTransfQuay}.
 */
public class BusDriver extends Thread {

    /*
    * Number of {@link Passenger}s inside the bus
    */
    private int nPassengers = 0;
    
    /*
    * Boolean variable set to false to signal when the {@link Busdriver} has completed 
    * his lifecycle
    */
    private boolean loop = true;

    /*
    * BusDriver state
    * {@link BusDriverState}
    */
    private BusDriverState state;

    /**
     * Arrival Terminal Transfer Quay {@link sharedRegions.ArrivalTermTransfQuay}
     */
    private final ArrivalTermTransfQuay arrivalTermTransfQuay;

    /**
     * Departure Terminal Transfer Quay
     * {@link sharedRegions.DepartureTermTransfQuay}
     */
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
    