package entities;
 
/**
 * Possible Porter's states enumerator
 */
public enum PorterState {
    /**
     * Initial state
     * <p>
     * Porter is waken by the operation {@link sharedRegions.ArrivalLounge#whatShouldIDo(int, int, Bag[], boolean)} of the last
     * {@link Passenger} to reach the arrival lounge.
     */
    WAITING_FOR_A_PLANE_TO_LAND,
    /**
     * Transition state
     * <p>
     * Porter's at the plane hold.
     */
    AT_THE_PLANES_HOLD,
    /**
     * Transition state
     * <p>
      * Porter's at {@link sharedRegions.BaggageCollectionPoint}.
     */
    AT_THE_LUGGAGE_BELT_CONVEYOR,
    /**
     * Transition state
     * <p>
     * Porter's at {@link sharedRegions.TempStorageArea}.
     */
    AT_THE_STOREOOM
}