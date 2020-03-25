package sharedRegions;

import java.util.concurrent.locks.*;

import entities.PassengerState;

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

    private GenInfoRepo rep;

    // Create lock and conditions
    public ExitAirport(int numPassengers, GenInfoRepo rep) {
        rl = new ReentrantLock(true);
        waitingEndCV = rl.newCondition();
        this.numPassengers = numPassengers;
        this.rep = rep;
    }

    
    /** 
     * @param nPlane
     */
    
    // PASSENGER

    //Passengers enter a lock state while waiting for every Passenger to finish their lifecycle
    public void goHome(int nPlane, int passengerID, PassengerState passengerState) {
        rl.lock();
        try {
            rep.passengerState(nPlane, passengerID, passengerState);

            passengers++;
            if (passengers == numPassengers) {
                passengers = 0;
                System.out.println("GOHOME VOO "+nPlane+" TERMINADO");
                waitingEndCV.signalAll();
            } else {
                waitingEndCV.await();
            }
        } catch (Exception ex) {
        } finally {
            rl.unlock();
        }
    }

    public void prepareNextLeg(int nPlane, int passengerID) {
        rl.lock();
        try {
            rep.passengerState(passengerID, PassengerState.ENTERING_THE_DEPARTURE_TERMINAL);
            passengers++;
            if (passengers == numPassengers) {
                passengers = 0;
                System.out.println("NEXTLEG VOO "+nPlane+" TERMINADO");
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