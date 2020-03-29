package sharedRegions;

import java.util.concurrent.locks.*;

import entities.PassengerState;

/**
 * This datatype implements the DepartureTerminalEntrance shared memory region.
 * <p>
 * In this shared region, the Passengers wait for each others lifecycle to be
 * over before moving on to the next flight simulation or concluding the program
 * execution
 */

public class DepartureTerminalEntrance {

    /**
     * A reentrant mutual exclusion Lock with the same basic behavior and semantics as the implicit monitor lock 
     * accessed using synchronized methods and statements
     */
    private final ReentrantLock rl;
    /**
     * Synchronization point where the {@link entities.Passenger}s wait each others.
     */
    private final Condition waitingEndCV;
    /**
     * Instance of {@link ArrivalTerminalExit}.
     */
    private ArrivalTerminalExit arrivalTerminalExit;
    /**
     * Count of {@link entities.Passenger}s ready to leave the airport.
     */
    private int passengers = 0;
    /**
     * 
     */
    private int arrivalPassengers = 0;
    /**
     * Final number of {@link entities.Passenger}s per flight.
     */
    private int numPassengers;
    /**
     * Instance fo General repository of Information.
     */
    private GenInfoRepo rep;

    /**
     * Instantiates the Departure Terminal Entrance.
     * @param numPassengers Number of {@link entities.Passenger}s.
     * @param rep {@link GenInfoRepo}.
     */
    public DepartureTerminalEntrance(int numPassengers, GenInfoRepo rep) {
        rl = new ReentrantLock(true);
        waitingEndCV = rl.newCondition();
        this.numPassengers = numPassengers;
        this.rep = rep;
    }

    
    /** 
     * @param arrivalTerminalExit
     */
    public void setArrivalTerminal(ArrivalTerminalExit arrivalTerminalExit) {

        this.arrivalTerminalExit = arrivalTerminalExit;
    }

    public void signalCompletion() {
        rl.lock();
        try {
            passengers = 0;
            arrivalPassengers = 0;
            waitingEndCV.signalAll();
        } catch (Exception e) {
            System.out.println("Thread: " + Thread.currentThread().getName() + " terminated.");
			System.out.println("Error: " + e.getMessage());
			System.exit(1);
        } finally {
            rl.unlock();
        }

    }

    public void signalPassenger() {
        arrivalPassengers++;
    }

    /**
     * Passengers enter a lock state while waiting for every Passenger to finish their lifecycle
     * @param nPlane
     * @param passengerID
     */
    public void prepareNextLeg(int nPlane, int passengerID) {
        rl.lock();
        try {
            rep.passengerState(passengerID, PassengerState.ENTERING_THE_DEPARTURE_TERMINAL);
            passengers++;
            arrivalTerminalExit.signalPassenger();
            if (passengers + arrivalPassengers == numPassengers) {
                passengers = 0;
                arrivalPassengers = 0;
                arrivalTerminalExit.signalCompletion();
                waitingEndCV.signalAll();
                System.out.println("VOO " + nPlane + " TERMINADO");
            } else {
                waitingEndCV.await();
            }
        } catch (Exception e) {
            System.out.println("Thread: " + Thread.currentThread().getName() + " terminated.");
			System.out.println("Error: " + e.getMessage());
			System.exit(1);
        } finally {
            rl.unlock();
        }
    }
}