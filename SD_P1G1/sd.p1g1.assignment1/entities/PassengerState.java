package entities;
 
/**
 * Possible Passenger's states enumerator.
 */
public enum PassengerState {
    /**
     * Initial state.
     */
    AT_THE_DISEMBARKING_ZONE,
    /**
     * Blocking state with eventual transition.
     */
    AT_THE_LUGGAGE_COLLECTION_POINT,
    AT_THE_BAGGAGE_RECLAIM_OFFICE,
    EXITING_THE_ARRIVAL_TERMINAL,
    AT_THE_ARRIVAL_TRANSFER_TERMINAL,
    /**
     * Blocking state.
     * <p>
     * The passenger is waken up by the operation {@link BusDriver.parkTheBusAndLetPassOff} of the {@link BusDriver}
     */
    TERMINAL_TRANSFER,
    AT_THE_DEPARTURE_TRANSFER_TERMINAL,
    ENTERING_THE_DEPARTURE_TERMINAL
}