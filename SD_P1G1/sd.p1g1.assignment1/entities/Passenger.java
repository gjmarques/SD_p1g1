package entities;

import sharedRegions.*;
import mainProgram.*;
import java.util.*;

/**
 * This datatype implements the Passenger thread. In his lifecycle, the
 * passenger arrives at the airport, and decides on his course of action:
 * <ul>
 * <li>Retrieves his bags, if he has any, then goes home;
 * <li>Goes to the departure terminal if he's not at his final destination;
 * <li>Or if he has no bags and is in his final destination, immediatly goes
 * home.
 * </ul>
 */

public class Passenger extends Thread {

    private PassengerState state;
    private int collectedBags;
    private List<Integer> numBags = new ArrayList<>();
    private Bag[] bags;
    private int id;

    private final ArrivalLounge arrivalLounge;
    private final ArrivalTerminalExit arrivalTerminalExit;
    private final DepartureTerminalEntrance departureTerminalEntrance;
    private final ArrivalTermTransfQuay arrivalTermTransfQuay;
    private final DepartureTermTransfQuay departureTermTransfQuay;
    private final BaggageCollectionPoint baggageCollectionPoint;
    private final BaggageReclaimOffice baggageReclaimOffice;

    private boolean finalDestination;

    private GenInfoRepo rep;


    public Passenger(int id, List<Integer> numBags, ArrivalLounge arrivalLounge,
            ArrivalTermTransfQuay arrivalTermTransfQuay, DepartureTermTransfQuay departureTermTransfQuay,
            BaggageCollectionPoint baggageCollectionPoint, BaggageReclaimOffice baggageReclaimOffice,
            ArrivalTerminalExit arrivalTerminalExit, DepartureTerminalEntrance departureTerminalEntrance, GenInfoRepo rep) {
        this.id = id;
        this.numBags = numBags;
        //rep.passengerState(PassengerState.AT_THE_DISEMBARKING_ZONE);
        //this.state = PassengerState.AT_THE_DISEMBARKING_ZONE;
        this.arrivalLounge = arrivalLounge;
        this.arrivalTermTransfQuay = arrivalTermTransfQuay;
        this.departureTermTransfQuay = departureTermTransfQuay;
        this.baggageCollectionPoint = baggageCollectionPoint;
        this.baggageReclaimOffice = baggageReclaimOffice;
        this.arrivalTerminalExit = arrivalTerminalExit;
        this.departureTerminalEntrance = departureTerminalEntrance;
        Random r = new Random();
        int high = 2;
        int low = 0;
        int result = r.nextInt(high-low) + low;
        if(result == 0) this.finalDestination = true;
        else this.finalDestination = false;

        
    }

    /**
     * This method defines the life-cycle of the Passenger.
     */

    @Override
    public void run() {
        for (int i = 0; i < Global.NR_FLIGHTS; i++) {
            collectedBags = 0;
            bags = new Bag[numBags.get(i)];
            for (int j = 0; j < bags.length; j++) {
                bags[j] = new Bag(this.finalDestination ? 'H' : 'T', this.id);

            }

            char choice = arrivalLounge.whatShouldIDo(i, this.id, bags, this.finalDestination);
            arrivalTermTransfQuay.setFlight(i);
            arrivalLounge.setFlight(i);
            switch (choice) {
                case ('a'):
                    //System.out.println("PASSENGER GONE HOME");
                    arrivalTerminalExit.goHome(i, this.id, PassengerState.EXITING_THE_ARRIVAL_TERMINAL);
                    //setState(PassengerState.EXITING_THE_ARRIVAL_TERMINAL);
                    break;

                case ('b'):
                    arrivalTermTransfQuay.takeABus(this.id);
                    //setState(PassengerState.AT_THE_ARRIVAL_TRANSFER_TERMINAL);
                    arrivalTermTransfQuay.enterTheBus(this.id);
                    //setState(PassengerState.TERMINAL_TRANSFER);
                    departureTermTransfQuay.leaveTheBus(this.id);
                    // setState(PassengerState.AT_THE_DEPARTURE_TRANSFER_TERMINAL);
                    departureTerminalEntrance.prepareNextLeg(i, this.id);
                    //setState(PassengerState.ENTERING_THE_DEPARTURE_TERMINAL);
                    break;

                case ('c'):
                        while (collectedBags < numBags.get(i)) {
                            //setState(PassengerState.AT_THE_LUGGAGE_COLLECTION_POINT);
                            char status = baggageCollectionPoint.goCollectABag(this.id);
                            if ( status == 'S') {
                                // bag collected
                                collectedBags += 1;
                            } else if (status == 'E') {
                                // bag is missing
                                baggageReclaimOffice.reportMissingBags(numBags.get(i) - collectedBags, this.id);
                                break;
                            }
                        }

                    //System.out.println("PASSENGER GOT ALL BAGS");
                    arrivalTerminalExit.goHome(i, this.id, PassengerState.EXITING_THE_ARRIVAL_TERMINAL);
                    //setState(PassengerState.ENTERING_THE_DEPARTURE_TERMINAL);
                    break;
            }
        }
    }

    /**
     * @param state
     */
    public void setState(PassengerState state) {
        this.state = state;
    }

    /**
     * @return PassengerState
     */
    public PassengerState getPassengerState() {
        return this.state;
    }

    /**
     * Situation of passenger: true = final destination; false = in transit
     * @return true if passengers' final destination
     */
    public boolean getSituation(){
        if(this.finalDestination) return true;
        return false;
    }

    /**
     * Number of pieces of luggage the passenger carried at the start of her journey
     * @return int
     */
    public int getNumBags(){
        return bags.length;
    }

    /**
     * Number of pieces of luggage the passenger has presently collected
     * @return int
     */
    public int getCollectedBags(){
        return collectedBags;
    }


    /**
     * @return int
     */
    public int getPassID() {
        return this.id;
    }

    /**
     * @return String
     */
    @Override
    public String toString() {
        return "{" + " state='" + getState() + "'" + "}";
    }
}