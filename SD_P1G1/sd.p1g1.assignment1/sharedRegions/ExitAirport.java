package sharedRegions;

import java.util.concurrent.locks.*;

/**
 * This datatype implements the ExitAirport shared memory region. 
 * <p>
 * In this shared region, the Passengers wait for each others lifecycle
 * to be over before moving on to the next flight simulation
 * or concluding the program execution
 */

public class ExitAirport {

    private final ReentrantLock rl;
    private final Condition waitingEndCV;

    private int passengers = 0;
    private int numPassengers;

    // Create lock and conditions
    public ExitAirport(int numPassengers) {
        rl = new ReentrantLock(true);
        waitingEndCV = rl.newCondition();
        this.numPassengers = numPassengers;
    }

    
    /** 
     * @param nPlane
     */
    // PASSENGER

    //Passengers enter a lock state while waiting for every Passenger to finish their lifecycle
    public void goHome(int nPlane) {
        rl.lock();
        try {
            passengers++;
            if (passengers == numPassengers) {
                passengers = 0;
                System.out.println("VOO "+nPlane+" TERMINADO");
                waitingEndCV.signalAll();
            } else {
                waitingEndCV.await();
            }
        } catch (Exception ex) {
        } finally {
            rl.unlock();
        }

    }

    public void prepareNextLeg() {
        rl.lock();
        try {
            passengers++;
            if (passengers == numPassengers) {
                passengers = 0;
                waitingEndCV.signalAll();
            } else {
                waitingEndCV.await();
            }
        } catch (Exception ex) {
        } finally {
            rl.unlock();
        }
    }

}