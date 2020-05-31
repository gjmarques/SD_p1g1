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

    private final GenInfoRepoStub rep;

    public BusDriver (ArrivalTermTransfQuayStub arrivalTermTransfQuayStub, DepartureTermTransfQuayStub departureTermTransfQuayStub, GenInfoRepoStub repoStub){
        this.arrivalTermTransfQuayStub = arrivalTermTransfQuayStub;
        this.departureTermTransfQuayStub = departureTermTransfQuayStub;
        this.rep = repoStub;
    }

    /**
     * This method defines the life-cycle of the Bus Driver.
     */
    @Override
    public void run(){

        while(loop){
            System.out.println("BUS DRIVER RUN");
            char choice = arrivalTermTransfQuayStub.hasDaysWorkEnded();
            
            rep.busDriverState(BusDriverState.PARKING_AT_THE_ARRIVAL_TERMINAL);
            
            System.out.println("BUS DRIVER AFTER ATTQ has days work ended");
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
    