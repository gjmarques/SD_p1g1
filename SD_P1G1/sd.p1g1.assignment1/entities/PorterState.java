package entities;
 
/**
 * Possible Porter's states enumerator
 */
public enum PorterState {
    /**
     * Initial state
     * <p>
     * Porter is waken by the operation whatShouldIDo of the last passenger to reach the arrival lounge
     */
    WAITING_FOR_A_PLANE_TO_LAND,
    /**
     * Transition state
     * <p>
     * Porter waits at the plane hold
     */
    AT_THE_PLANES_HOLD,
    /**
     * Transition state
     * <p>
     * Porter waits at the liggage belt conveyor
     */
    AT_THE_LUGGAGE_BELT_CONVEYOR,
    /**
     * Transition state
     * <p>
     * Porter waits at the store room
     */
    AT_THE_STOREOOM
}