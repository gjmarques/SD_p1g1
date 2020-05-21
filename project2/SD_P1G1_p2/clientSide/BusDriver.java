package clientSide;

import global.*;
import serverSide.*;

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
    private final ArrivalTermTransfQuayStub arrivalTermTransfQuayStub;

    /**
     * Departure Terminal Transfer Quay
     * {@link sharedRegions.DepartureTermTransfQuay}
     */
    private final DepartureTermTransfQuayStub departureTermTransfQuayStub;

    public BusDriver (ArrivalTermTransfQuayStub arrivalTermTransfQuayStub, DepartureTermTransfQuayStub departureTermTransfQuayStub){
        this.arrivalTermTransfQuayStub = arrivalTermTransfQuayStub;
        this.departureTermTransfQuayStub = departureTermTransfQuayStub;
    }

    /**
     * This method defines the life-cycle of the Bus Driver.
     */
    @Override
    public void run(){

        while(loop){
            char choice = arrivalTermTransfQuayStub.hasDaysWorkEnded();
            if(choice == 'W') {
                nPassengers = arrivalTermTransfQuayStub.annoucingBusBoarding();			
                departureTermTransfQuayStub.goToDepartureTerminal();
                departureTermTransfQuayStub.parkTheBusAndLetPassengerOff(nPassengers);
                arrivalTermTransfQuayStub.goToArrivalTerminal();
                arrivalTermTransfQuayStub.parkTheBus();
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
    