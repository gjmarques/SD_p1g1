package entities;

import sharedRegions.*;

public class BusDriver extends Thread {

    private int timeToWait;
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
            System.out.println("BUSDRIVER START");
            char choice = arrivalTermTransfQuay.hasDaysWorkEnded(); 
            // if(timeToWait % 500 == 0) {
            //     arrivalTermTransfQuay.departureTime();
            // }

            if(choice == 'W') {
                nPassengers = arrivalTermTransfQuay.annoucingBusBoarding();	
                System.out.println(" BUSDRIVER nPassengers: "+ nPassengers);			
                goToDepartureTerminal();
                departureTermTransfQuay.parkTheBusAndLetPassengerOff(nPassengers);
                goToArrivalTerminal();
                arrivalTermTransfQuay.parkTheBus();

		    System.out.println("BUSDRIVER CYCLE");
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

    }
    void goToArrivalTerminal(){

    }

}
    