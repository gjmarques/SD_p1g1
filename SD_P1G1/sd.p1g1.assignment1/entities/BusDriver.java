package entities;

import sharedRegions.*;

public class BusDriver extends Thread {

    private int nPassengers = 0;
    private boolean loop = true;
    private BusDriverState state;
    private final ArrivalTermTransfQuay arrivalTermTransfQuay;
    private final DepartureTermTransfQuay departureTermTransfQuay;
    private GenInfoRepo rep;

    public BusDriver (ArrivalTermTransfQuay arrivalTermTransfQuay, DepartureTermTransfQuay departureTermTransfQuay, GenInfoRepo rep){
        //this.state = BusDriverState.PARKING_AT_THE_ARRIVAL_TERMINAL;
        rep.busDriverState(BusDriverState.PARKING_AT_THE_ARRIVAL_TERMINAL);
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
                //setState(BusDriverState.PARKING_AT_THE_DEPARTURE_TERMINAL);
                departureTermTransfQuay.parkTheBusAndLetPassengerOff(nPassengers);
                arrivalTermTransfQuay.goToArrivalTerminal();
                //setState(BusDriverState.PARKING_AT_THE_ARRIVAL_TERMINAL);
                arrivalTermTransfQuay.parkTheBus();
            }else if(choice == 'E'){
                loop = false;
            }
        }
    }

    
    // /** 
    //  * @param {@link BusDriverState}
    //  */
    // private void setState(BusDriverState state) {
    //     this.state = state;
    // }
    
    /** 
     * @return BusDriverState
     */
    public BusDriverState getBDriverState() {
        return this.state;
    }

}
    