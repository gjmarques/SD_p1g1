package sharedRegions;

import java.util.concurrent.locks.*;

import entities.PassengerState;

/**
 * This datatype implements the ArrivalTerminalExit shared memory region.
 * <p>
 * In this shared region, the Passengers wait for each others lifecycle to be
 * over before moving on to the next flight simulation or concluding the program
 * execution
 */

public class ArrivalTerminalExit {

    /**
     * A reentrant mutual exclusion Lock with the same basic behavior and semantics as the implicit monitor lock 
     * accessed using synchronized methods and statements
     */
    private final ReentrantLock rl;

    /**
     * Synchronization point where the {@link entities.Passenger}s wait for each other
     * to complete their lifecycle before moving on to the next flight
     */
    private final Condition waitingEndCV;

    /**
     * Departure Terminal Entrance {@link DepartureTerminalEntrance}
     */
    private DepartureTerminalEntrance departureTerminalEntrance;

    /**
     * Total number of {@link entities.Passenger}s who finished their execution in the Arrival Terminal Exit
     */  
    private int passengers = 0;

    /**
     * Total number of {@link entities.Passenger}s who finished their execution in the Departure Terminal Entrance
     */  
    private int departurePassengers = 0;

    /**
     * Total number of {@link entities.Passenger}s
     */  
    private int numPassengers;
    
    /**
     * General Information Repository {@link GenInfoRepo}
     */
    private GenInfoRepo rep;

    // Create lock and conditions
    public ArrivalTerminalExit(int numPassengers, GenInfoRepo rep) {
        rl = new ReentrantLock(true);
        waitingEndCV = rl.newCondition();
        this.numPassengers = numPassengers;
        this.rep = rep;
    }

    /**
     * Instantiate {@link DepartureTerminalEntrance} 
     * @param departureTerminalEntrance
     */
    public void setDepartureTerminal(DepartureTerminalEntrance departureTerminalEntrance) {

        this.departureTerminalEntrance = departureTerminalEntrance;
    }

    /**
     * Signal {@link DepartureTerminalEntrance} that all {@link entities.Passenger} have completed their lifecycle
     */
    public void signalCompletion() {
        rl.lock();
        try {
            passengers = 0;
            departurePassengers = 0;
            waitingEndCV.signalAll();
        } catch (Exception e) {
            System.out.println("Thread: " + Thread.currentThread().getName() + " terminated.");
			System.out.println("Error: " + e.getMessage());
			System.exit(1);
        } finally {
            rl.unlock();
        }

    }

    /**
     * Signal {@link DepartureTerminalEntrance} that a {@link entities.Passenger} has entered the Arrival Terminal Exit
     */
    public void signalPassenger() {
        departurePassengers++;
    }


    /**
     * Passengers enter a lock state while waiting for every Passenger to finish their lifecycle
     * @param nPlane
     * @param passengerID
     * @param passengerState
     */
    public void goHome(int nPlane, int passengerID, PassengerState passengerState) {
        rl.lock();
        try {
            rep.passengerState(nPlane, passengerID, passengerState);

            passengers++;
            departureTerminalEntrance.signalPassenger();
            if (passengers + departurePassengers == numPassengers) {
                passengers = 0;
                departurePassengers = 0;
                departureTerminalEntrance.signalCompletion();
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