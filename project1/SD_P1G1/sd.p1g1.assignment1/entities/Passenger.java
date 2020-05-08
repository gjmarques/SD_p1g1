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

    /**
     * Passenger's state {@link PassengerState}
     */
    private PassengerState state;
    /**
     * Count of bags collected by the passenger
     */
    private int collectedBags;
    /**
     * List of {@link Bag}s of the passenger
     */
    private List<Integer> numBags = new ArrayList<>();
    /**
     * Number of passenger's {@link Bag}s per flight
     */
    private Bag[] bags;
    /**
     * This Passenger's identification
     */
    private int id;
    /**
     * Arrival Lounge {@link sharedRegions.ArrivalLounge}
     */
    private final ArrivalLounge arrivalLounge;
    /**
     * Arrival Terminal Exit {@link sharedRegions.ArrivalTerminalExit}
     */
    private final ArrivalTerminalExit arrivalTerminalExit;
    /**
     * Departure Terminal Entrance {@link sharedRegions.DepartureTerminalEntrance}
     */
    private final DepartureTerminalEntrance departureTerminalEntrance;
    /**
     * Arrival Terminal Transfer Quay {@link sharedRegions.ArrivalTermTransfQuay}
     */
    private final ArrivalTermTransfQuay arrivalTermTransfQuay;
    /**
     * Departure Terminal Transfer Quay
     * {@link sharedRegions.DepartureTermTransfQuay}
     */
    private final DepartureTermTransfQuay departureTermTransfQuay;
    /**
     * Baggage Collection Point {@link sharedRegions.BaggageCollectionPoint}
     */
    private final BaggageCollectionPoint baggageCollectionPoint;
    /**
     * Baggage Reclaim Office {@link sharedRegions.BaggageReclaimOffice}
     */
    private final BaggageReclaimOffice baggageReclaimOffice;
    /**
     * If this airport is passenger's final destination
     */
    private boolean finalDestination;
    /**
     * General Information Repository {@link sharedRegions.GenInfoRepo}.
     */
    private GenInfoRepo rep;

    /**
     * Instantiates Passenger entity
     * 
     * @param identification of the passenger.
     * @param numBags list of number of passengers' {@link Bag}s, per flight.
     * @param arrivalLounge {@link sharedRegions.ArrivalLounge}.
     * @param arrivalTermTransfQuay {@link sharedRegions.ArrivalTermTransfQuay}.
     * @param departureTermTransfQuay {@link sharedRegions.DepartureTermTransfQuay}.
     * @param baggageCollectionPoint {@link sharedRegions.BaggageCollectionPoint}.
     * @param baggageReclaimOffice {@link sharedRegions.BaggageReclaimOffice}.
     * @param arrivalTerminalExit {@link sharedRegions.ArrivalTerminalExit}.
     * @param departureTerminalEntrance {@link sharedRegions.DepartureTerminalEntrance}.
     * @param rep {@link sharedRegions.GenInfoRepo}.
     */
    public Passenger(int identification, List<Integer> numBags, ArrivalLounge arrivalLounge,
            ArrivalTermTransfQuay arrivalTermTransfQuay, DepartureTermTransfQuay departureTermTransfQuay,
            BaggageCollectionPoint baggageCollectionPoint, BaggageReclaimOffice baggageReclaimOffice,
            ArrivalTerminalExit arrivalTerminalExit, DepartureTerminalEntrance departureTerminalEntrance, GenInfoRepo rep) {
        this.id = identification;
        this.numBags = numBags;
        this.arrivalLounge = arrivalLounge;
        this.arrivalTermTransfQuay = arrivalTermTransfQuay;
        this.departureTermTransfQuay = departureTermTransfQuay;
        this.baggageCollectionPoint = baggageCollectionPoint;
        this.baggageReclaimOffice = baggageReclaimOffice;
        this.arrivalTerminalExit = arrivalTerminalExit;
        this.departureTerminalEntrance = departureTerminalEntrance;
        this.rep = rep;
    }

    /**
     * This method defines the life-cycle of the Passenger.
     */
    @Override
    public void run() {
        Random r;
        for (int i = 0; i < Global.NR_FLIGHTS; i++) {
            r = new Random();
            this.finalDestination = r.nextBoolean();
            rep.countDest(this.finalDestination);
            rep.initPassenger(i, this.id);
            collectedBags = 0;
            bags = new Bag[numBags.get(i)];
            for (int j = 0; j < bags.length; j++) {
                bags[j] = new Bag(this.finalDestination ? 'H' : 'T', this.id, i);
            }

            char choice = arrivalLounge.whatShouldIDo(i, this.id, bags, this.finalDestination);
            arrivalTermTransfQuay.setFlight(i);
            arrivalLounge.setFlight(i);
            switch (choice) {
                case ('a'):
                    arrivalTerminalExit.goHome(i, this.id, PassengerState.EXITING_THE_ARRIVAL_TERMINAL);
                    break;

                case ('b'):
                    arrivalTermTransfQuay.takeABus(this.id);
                    arrivalTermTransfQuay.enterTheBus(this.id);
                    departureTermTransfQuay.leaveTheBus(this.id);
                    departureTerminalEntrance.prepareNextLeg(i, this.id);

                    break;
                case ('c'):
                    collectedBags = baggageCollectionPoint.goCollectABag(this.id);
                    if (collectedBags < numBags.get(i)) {
                        baggageReclaimOffice.reportMissingBags(numBags.get(i) - collectedBags, this.id);
                    }
                    arrivalTerminalExit.goHome(i, this.id, PassengerState.EXITING_THE_ARRIVAL_TERMINAL);
                    break;
            }
        }
    }

    /**
     * Gets passenger state
     * 
     * @return PassengerState.
     */
    public PassengerState getPassengerState() {
        return this.state;
    }

    /**
     * Situation of passenger: true = final destination; false = in transit
     * 
     * @return true if this airport is the final destination of the passenger. False, otherwise.
     */
    public boolean getSituation() {
        if (this.finalDestination)
            return true;
        return false;
    }

    /**
     * Number of pieces of luggage the passenger carried at the start of her journey
     * 
     * @return int
     */
    public int getNumBags() {
        return bags.length;
    }

    /**
     * Number of pieces of luggage the passenger has presently collected
     * 
     * @return int
     */
    public int getCollectedBags() {
        return collectedBags;
    }

    /**
     * Gets this passenger's id
     * 
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