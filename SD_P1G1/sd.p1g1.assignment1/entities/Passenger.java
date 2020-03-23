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
    private final ExitAirport exitAirport;
    private final ArrivalTermTransfQuay arrivalTermTransfQuay;
    private final DepartureTermTransfQuay departureTermTransfQuay;
    private final BaggageCollectionPoint baggageCollectionPoint;
    private final BaggageReclaimOffice baggageReclaimOffice;

    public Passenger(int id, List<Integer> numBags, ArrivalLounge arrivalLounge,
            ArrivalTermTransfQuay arrivalTermTransfQuay, DepartureTermTransfQuay departureTermTransfQuay,
            BaggageCollectionPoint baggageCollectionPoint, BaggageReclaimOffice baggageReclaimOffice,
            ExitAirport exitAirport) {
        this.id = id;
        this.numBags = numBags;
        this.state = PassengerState.AT_THE_DISEMBARKING_ZONE;
        this.arrivalLounge = arrivalLounge;
        this.arrivalTermTransfQuay = arrivalTermTransfQuay;
        this.departureTermTransfQuay = departureTermTransfQuay;
        this.baggageCollectionPoint = baggageCollectionPoint;
        this.baggageReclaimOffice = baggageReclaimOffice;
        this.exitAirport = exitAirport;

    }

    /**
     * This method defines the life-cycle of the Passenger.
     */

    @Override
    public void run() {
        Random r = new Random();
        for (int i = 0; i < Global.NR_FLIGHTS; i++) {
            boolean finalDestination = true; //r.nextBoolean();
            collectedBags = 0;
            bags = new Bag[numBags.get(i)];
            for (int j = 0; j < bags.length; j++) {
                bags[j] = new Bag(finalDestination ? 'H' : 'T', id);

            }

            char choice = arrivalLounge.whatShouldIDo(bags, finalDestination);
            arrivalTermTransfQuay.setFlight(i);
            arrivalLounge.setFlight(i);
            switch (choice) {
                case ('a'):
                System.out.println("PASSENGER GONE HOME");
                    exitAirport.goHome(i);
                    setState(PassengerState.EXITING_THE_ARRIVAL_TERMINAL);
                    break;

                case ('b'):
                    arrivalTermTransfQuay.takeABus();
                    setState(PassengerState.AT_THE_ARRIVAL_TRANSFER_TERMINAL);
                    arrivalTermTransfQuay.enterTheBus();
                    setState(PassengerState.TERMINAL_TRANSFER);
                    departureTermTransfQuay.leaveTheBus();
                    setState(PassengerState.AT_THE_DEPARTURE_TRANSFER_TERMINAL);
                    exitAirport.prepareNextLeg(i);
                    setState(PassengerState.ENTERING_THE_DEPARTURE_TERMINAL);
                    break;

                case ('c'):
                    while (collectedBags < numBags.get(i)) {
                        setState(PassengerState.AT_THE_LUGGAGE_COLLECTION_POINT);
                        char status = baggageCollectionPoint.goCollectABag(id);
                        if ( status == 'S') {
                            collectedBags += 1;
                        } else if (status == 'E') {
                            setState(PassengerState.AT_THE_BAGGAGE_RECLAIM_OFFICE);
                            baggageReclaimOffice.reportMissingBags(numBags.get(i) - collectedBags);
                            break;
                        }
                    }
                    System.out.println("PASSENGER GOT ALL BAGS");
                    exitAirport.goHome(i);
                    setState(PassengerState.ENTERING_THE_DEPARTURE_TERMINAL);
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