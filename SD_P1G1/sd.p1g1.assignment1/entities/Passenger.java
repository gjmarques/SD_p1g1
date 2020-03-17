package entities;

import sharedRegions.*;
import mainProgram.*;
import java.util.*;

/**
 * This datatype implements the Passenger thread. 
 * In his lifecycle, the passenger arrives at the airport,
 * and decides on his course of action:
 * <ul>
 * <li> Retrieves his bags, if he has any, then goes home;
 * <li> Goes to the departure terminal if he's not at his final destination;
 * <li> Or if he has no bags and is in his final destination, immediatly goes home.
 * </ul>
 */

public class Passenger extends Thread {

    private PassengerState state;
    private boolean finalDestination;
    private int collectedBags;
    private List<Integer> numBags = new ArrayList<>();
    private Bag[] bags;
    private int id;

    private final ArrivalLounge arrivalLounge;
    private final ExitAirport exitAirport;

    public Passenger(int id, List<Integer> numBags, boolean finalDestination, ArrivalLounge arrivalLounge, ExitAirport exitAirport) {
        this.id = id;
        this.numBags = numBags;
        this.finalDestination = finalDestination;
        this.state = PassengerState.AT_THE_DISEMBARKING_ZONE;
        this.arrivalLounge = arrivalLounge;
        this.exitAirport = exitAirport;
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
                bags[j] = new Bag(finalDestination ? 'H' : 'T', id);
            }

            char choice = arrivalLounge.whatShouldIDo(bags, finalDestination, i);
            switch (choice) {
                case ('a'):
                    //exitAirport.goHome(i);

                case ('b'):
                    // arrivalTermTransfQuay.takeABus();
                    // arrivalTermTransfQuay.enterTheBus();
                    // departureTermTransfQuay.leaveTheBus();
                    // departureTermEntrance.prepareNextLeg(i);

                case ('c'):
                    // while (collectedBags != numBags.get(i)) {
                    //     if (baggageCollectionPoint.goCollectABag()) {
                    //         collectedBags += 1;
                    //     } else {
                    //         baggageReclaimOffice.reportMissingBags(numBags.get(i) - collectedBags);
                    //         break;
                    //     }
                    //     arrivalTermExit.goHome(i);
                    // }
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