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

    private final ReentrantLock rl;
    private final Condition waitingEndCV;

    private ArrivalTerminalExit arrivalTerminalExit;

    private int passengers = 0;
    private int numPassengers;

    private GenInfoRepo rep;

    // Create lock and conditions
    public DepartureTerminalEntrance(int numPassengers, GenInfoRepo rep) {
        rl = new ReentrantLock(true);
        waitingEndCV = rl.newCondition();
        this.numPassengers = numPassengers;
        this.rep = rep;
    }

    public void setArrivalTerminal(ArrivalTerminalExit arrivalTerminalExit) {

        this.arrivalTerminalExit = arrivalTerminalExit;
    }

    public void signalCompletion() {
        rl.lock();
        try {
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
        rl.lock();
        try {
            passengers++;
        } catch (Exception e) {
            System.out.println("Thread: " + Thread.currentThread().getName() + " terminated.");
			System.out.println("Error: " + e.getMessage());
			System.exit(1);
        } finally {
            rl.unlock();
        }
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
            if (passengers == numPassengers) {
                passengers = 0;
                arrivalTerminalExit.signalCompletion();
                waitingEndCV.signalAll();

                System.out.println("NEXTLEG VOO " + nPlane + " TERMINADO");
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