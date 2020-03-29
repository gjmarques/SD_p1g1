package entities;
 
/**
 * Possible Bus Driver's states enumerator
 */
public enum BusDriverState {
    /**
     * Initial and final state of the {@link BusDriver}. Here, the driver is waken up the first 
     * time by the operation {@link sharedRegions.ArrivalTermTransfQuay#takeABus(int)} of the passenger who arrives at the 
     * {@link sharedRegions.ArrivalTermTransfQuay} and finds out her place in the waiting queue 
     * equals the bus capacity, or when the departure time has been reached (transition will only 
     * occurs if there is at least one {@link Passenger} forming the queue); the driver is waken up 
     * the second time by the operation {@link sharedRegions.ArrivalTermTransfQuay#enterTheBus(int)} of the last passenger to enter the bus.
     */
    PARKING_AT_THE_ARRIVAL_TERMINAL,
    /**
     * Transition state.
     */
    DRIVING_FORWARD,
    /**
     * Blocking state. The {@link BusDriver} is waken up by the operation {@link sharedRegions.DepartureTermTransfQuay#leaveTheBus(int)} 
     * of the last {@link Passenger} to exit the bus.
     */
    PARKING_AT_THE_DEPARTURE_TERMINAL,
    /**
     * TRansition state.
     */
    DRIVING_BACKWARD
}