package entities;
 
/**
 * Possible Passenger's states enumerator.
 */
public enum PassengerState {
    /**
     * Transition/Initial state.
     */
    AT_THE_DISEMBARKING_ZONE,
    /**
     * Blocking state with eventual transition.
     * <p>
     * {@link Passenger} is at the {@link sharedRegions.BaggageCollectionPoint}
     */
    AT_THE_LUGGAGE_COLLECTION_POINT,
    /**
     * Transition state.
     * <p>
     * {@link Passenger} is at the {@link sharedRegions.BaggageReclaimOffice}
     */
    AT_THE_BAGGAGE_RECLAIM_OFFICE,
    /**
     * Blocking state with eventual transition (final state)
     * <p>
     * {@link Passenger} is at the {@link sharedRegions.ArrivalTerminalExit}
     */
    EXITING_THE_ARRIVAL_TERMINAL,
    /**
     * Blocking state.
     * <p>
     * {@link Passenger} is at the {@link sharedRegions.ArrivalTermTransfQuay}
     */
    AT_THE_ARRIVAL_TRANSFER_TERMINAL,
    /**
     * Blocking state.
     * <p>
     * The {@link Passenger} is waken up by the operation parkTheBusAndLetPassengerOff in {@link sharedRegions.DepartureTermTransfQuay} of the {@link BusDriver}
     */
    TERMINAL_TRANSFER,
    /**
     * Transition state.
     * <p>
     * {@link Passenger} is at the {@link sharedRegions.DepartureTermTransfQuay}
     */
    AT_THE_DEPARTURE_TRANSFER_TERMINAL,
    /**
     * Blocking state with eventual transiction (final state).
     * <p>
     * {@link Passenger} is at the {@link sharedRegions.DepartureTerminalEntrance}
     */
    ENTERING_THE_DEPARTURE_TERMINAL
}