package clientSide;

import global.*;
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
     * Arrival Lounge Stub {@link sharedRegions.ArrivalLoungeStub}
     */
    private final ArrivalLoungeStub arrivalLoungeStub;
    /**
     * Arrival Terminal Exit Stub {@link sharedRegions.ArrivalTerminalExitStub}
     */
    private final ArrivalTerminalExitStub arrivalTerminalExitStub;
    /**
     * Departure Terminal Entrance Stub {@link sharedRegions.DepartureTerminalEntranceStub}
     */
    private final DepartureTerminalEntranceStub departureTerminalEntranceStub;
    /**
     * Arrival Terminal Transfer Quay Stub {@link sharedRegions.ArrivalTermTransfQuayStub}
     */
    private final ArrivalTermTransfQuayStub arrivalTermTransfQuayStub;
    /**
     * Departure Terminal Transfer Quay Stub {@link sharedRegions.DepartureTermTransfQuayStub}
     */
    private final DepartureTermTransfQuayStub departureTermTransfQuayStub;
    /**
     * Baggage Collection Point Stub {@link sharedRegions.BaggageCollectionPointStub}
     */
    private final BaggageCollectionPointStub baggageCollectionPointStub;
    /**
     * Baggage Reclaim Office Stub {@link sharedRegions.BaggageReclaimOfficeStub}
     */
    private final BaggageReclaimOfficeStub baggageReclaimOfficeStub;
    /**
     * If this airport is passenger's final destination
     */
    private boolean finalDestination;
    /**
     * General Information Repository {@link sharedRegions.GenInfoRepo}.
     */
    private GenInfoRepoStub repoStub;

    /**
     * Instantiates Passenger entity
     * 
     * @param identification of the passenger.
     * @param numBags list of number of passengers' {@link Bag}s, per flight.
     * @param arrivalLoungeStub {@link sharedRegions.ArrivalLoungeStub}.
     * @param arrivalTermTransfQuayStub {@link sharedRegions.ArrivalTermTransfQuayStub}.
     * @param departureTermTransfQuayStub {@link sharedRegions.DepartureTermTransfQuayStub}.
     * @param baggageCollectionPointStub {@link sharedRegions.BaggageCollectionPointStub}.
     * @param baggageReclaimOfficeStub {@link sharedRegions.BaggageReclaimOfficeStub}.
     * @param arrivalTerminalExitStub {@link sharedRegions.ArrivalTerminalExitStub}.
     * @param departureTerminalEntranceStub {@link sharedRegions.DepartureTerminalEntranceStub}.
     * @param repoStub {@link sharedRegions.GenInfoRepoStub}.
     */
    public Passenger(int identification, List<Integer> numBags, ArrivalLoungeStub arrivalLoungeStub,
            ArrivalTermTransfQuayStub arrivalTermTransfQuayStub, DepartureTermTransfQuayStub departureTermTransfQuayStub,
            BaggageCollectionPointStub baggageCollectionPointStub, BaggageReclaimOfficeStub baggageReclaimOfficeStub,
            ArrivalTerminalExitStub arrivalTerminalExitStub, DepartureTerminalEntranceStub departureTerminalEntranceStub, GenInfoRepoStub repoStub) {

        this.id = identification;
        this.numBags = numBags;
        this.arrivalLoungeStub = arrivalLoungeStub;
        this.arrivalTermTransfQuayStub = arrivalTermTransfQuayStub;
        this.departureTermTransfQuayStub = departureTermTransfQuayStub;
        this.baggageCollectionPointStub = baggageCollectionPointStub;
        this.baggageReclaimOfficeStub = baggageReclaimOfficeStub;
        this.arrivalTerminalExitStub = arrivalTerminalExitStub;
        this.departureTerminalEntranceStub = departureTerminalEntranceStub;
        this.repoStub = repoStub;
    }

    /**
     * This method defines the life-cycle of the Passenger.
     */
    @Override
    public void run() {
        Random r;
        for (int i = 0; i < Global.MAX_FLIGHTS; i++) {
            r = new Random();
            this.finalDestination = r.nextBoolean();
            repoStub.countDest(this.finalDestination);
            repoStub.initPassenger(i, this.id);
            collectedBags = 0;
            bags = new Bag[numBags.get(i)];
            for (int j = 0; j < bags.length; j++) {
                bags[j] = new Bag(this.finalDestination ? 'H' : 'T', this.id, i);
            }
            char choice = arrivalLoungeStub.whatShouldIDo(i, this.id, bags, this.finalDestination);
            repoStub.passengerState(i, this.id, PassengerState.AT_THE_DISEMBARKING_ZONE, this.finalDestination, bags.length);
            arrivalTermTransfQuayStub.setFlight(i);
            arrivalLoungeStub.setFlight(i);
            switch (choice) {
                case ('a'):
                    arrivalTerminalExitStub.goHome(i, this.id, PassengerState.EXITING_THE_ARRIVAL_TERMINAL);
                    repoStub.passengerState(i, this.id, PassengerState.EXITING_THE_ARRIVAL_TERMINAL);
                    break;
                case ('b'):
                    arrivalTermTransfQuayStub.takeABus(this.id);
                    repoStub.passengerState(this.id, PassengerState.AT_THE_ARRIVAL_TRANSFER_TERMINAL);
                    arrivalTermTransfQuayStub.enterTheBus(this.id);
                    repoStub.passengerState(this.id, PassengerState.TERMINAL_TRANSFER);
                    departureTermTransfQuayStub.leaveTheBus(this.id);
                    repoStub.passengerState(this.id, PassengerState.AT_THE_DEPARTURE_TRANSFER_TERMINAL);
                    departureTerminalEntranceStub.prepareNextLeg(i, this.id);
                    repoStub.passengerState(this.id, PassengerState.ENTERING_THE_DEPARTURE_TERMINAL);
                    break;
                case ('c'):
                    collectedBags = baggageCollectionPointStub.goCollectABag(this.id);
        			repoStub.passengerState(this.id, PassengerState.AT_THE_LUGGAGE_COLLECTION_POINT);

                    if (collectedBags < numBags.get(i)) {
                        baggageReclaimOfficeStub.reportMissingBags(numBags.get(i) - collectedBags, this.id);
                        repoStub.passengerState(this.id, PassengerState.AT_THE_BAGGAGE_RECLAIM_OFFICE);
                    }
                    arrivalTerminalExitStub.goHome(i, this.id, PassengerState.EXITING_THE_ARRIVAL_TERMINAL);
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