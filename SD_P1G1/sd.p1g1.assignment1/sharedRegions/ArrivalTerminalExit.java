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

    private final ReentrantLock rl;
    private final Condition waitingEndCV;

    private DepartureTerminalEntrance departureTerminalEntrance;

    private int passengers = 0;
    private int departurePassengers = 0;
    private int numPassengers;

    private GenInfoRepo rep;

    // Create lock and conditions
    public ArrivalTerminalExit(int numPassengers, GenInfoRepo rep) {
        rl = new ReentrantLock(true);
        waitingEndCV = rl.newCondition();
        this.numPassengers = numPassengers;
        this.rep = rep;
    }

    public void setDepartureTerminal(DepartureTerminalEntrance departureTerminalEntrance) {

        this.departureTerminalEntrance = departureTerminalEntrance;
    }

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