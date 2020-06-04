package serverSide;

import java.util.concurrent.locks.*;

import clientSide.*;

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
     * Synchronization point where the {@link entities.Passenger}s wait for each other
     * to complete their lifecycle before moving on to the next flight
     */
    private final Condition waitingEndCV;

    /**
     * Arrival Terminal Exit {@link ArrivalTerminalExit}
     */
    private ArrivalTerminalExitStub arrivalTerminalExitStub;

    /**
     * Total number of {@link entities.Passenger}s who finished their execution in the Arrival Terminal Exit
     */  
    private int passengers = 0;

    /**
     * Total number of {@link entities.Passenger}s who finished their execution in the Arrival Terminal Exit
     */  
    private int arrivalPassengers = 0;

    /**
     * Total number of {@link entities.Passenger}s
     */  
    private int numPassengers;

    /**
     * General Information Repository {@link GenInfoRepoStub}
     */
    private GenInfoRepoStub rep;

    /**
     * Instantiates the Departure Terminal Entrance.
     * @param numPassengers Number of {@link entities.Passenger}s.
     * @param rep {@link GenInfoRepoStub}.
     */
    public DepartureTerminalEntrance(int numPassengers, GenInfoRepoStub rep) {
        rl = new ReentrantLock(true);
        waitingEndCV = rl.newCondition();
        this.numPassengers = numPassengers;
        this.rep = rep;
    }

    /**
     * Instantiate {@link ArrivalTerminalExit} 
     * @param arrivalTerminalExitStub
     */
    public void setArrivalTerminal(ArrivalTerminalExitStub arrivalTerminalExitStub) {

        this.arrivalTerminalExitStub = arrivalTerminalExitStub;
    }

    /**
     * Signal {@link ArrivalTerminalExit} that all {@link entities.Passenger} have completed their lifecycle
     */
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

    /**
     * Signal {@link ArrivalTerminalExit} that a {@link entities.Passenger} has entered the Arrival Terminal Exit
     */
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
            arrivalTerminalExitStub.signalPassenger();
            if (passengers + arrivalPassengers == numPassengers) {
                passengers = 0;
                arrivalPassengers = 0;
                arrivalTerminalExitStub.signalCompletion();
                waitingEndCV.signalAll();
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