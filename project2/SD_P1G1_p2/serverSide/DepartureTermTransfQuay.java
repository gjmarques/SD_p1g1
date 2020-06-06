package serverSide;

import java.util.concurrent.locks.*;

import clientSide.*;

/**
 * A synchronization point for the {@link clientSide.BusDriver} where he blocks while waiting for
 * all the {@link clientSide.Passenger}s to leave the bus.
 */
public class DepartureTermTransfQuay{

    /**
     * A reentrant mutual exclusion Lock with the same basic behavior and semantics as the implicit monitor lock 
     * accessed using synchronized methods and statements
     */
    private final ReentrantLock rl;
    /**
     * Synchronization point where the {@link clientSide.Passenger}s wait for the {@link clientSide.BusDriver} to arrive.
     */
    private final Condition waitArrival;
    /**
     * Synchronization point where the {@link clientSide.BusDriver} waits for the {@link clientSide.Passenger}s to leave the bus.
     */
    private final Condition waitEmpty;
    /**
     * Count of {@link clientSide.Passenger}s inside the bus.
     */
    private int numPassengers = 0;
    /**
     * Instance fo General repository of Information.
     */
    private GenInfoRepoStub rep;


    /**
     * Instantiates the Departure Terminal Tranfer Quay.
     * @param rep {@link GenInfoRepo}.
     */
    public DepartureTermTransfQuay(GenInfoRepoStub rep) {
        rl = new ReentrantLock(true);
		waitEmpty = rl.newCondition();
        waitArrival = rl.newCondition();
        this.rep = rep;
    }

    /**
     * When the {@link clientSide.BusDriver} arrives at this shared region he stops and let the passengers get off the bus.
     * @param busPassengers Number of passengers inside the bus.
     */
    public void parkTheBusAndLetPassengerOff(int busPassengers) {
        rl.lock();
        try {
            numPassengers += busPassengers;
            rep.busDriverState(BusDriverState.PARKING_AT_THE_ARRIVAL_TERMINAL);
			waitArrival.signalAll();
            waitEmpty.await();
        } catch(Exception e) {
            System.out.println("Thread: " + Thread.currentThread().getName() + " terminated.");
			System.out.println("Error: " + e.getMessage());
			System.exit(1);
        }
        finally {
            rl.unlock();
        }
    }

    /**
     * {@link clientSide.Passenger}s leave the bus at this shared region.
     * @param passengerID {@link clientSide.Passenger}'s identification.
     */
    public void leaveTheBus(int passengerID) {
        rl.lock();
        try {
            rep.passengerState(passengerID, PassengerState.AT_THE_DEPARTURE_TRANSFER_TERMINAL);
            waitArrival.await();   
            numPassengers--;
            rep.leaveBus(passengerID);
            if(numPassengers == 0) {
                waitEmpty.signal();
            }
        } catch(Exception e) {
            System.out.println("Thread: " + Thread.currentThread().getName() + " terminated.");
			System.out.println("Error: " + e.getMessage());
			System.exit(1);
        }
        finally {
            rl.unlock();
        }
    }

    /**
     * This method tells the {@link GenInfoRepo} that the {@link clientSide.BusDriver} is driving forward during 50 miliseconds.
     */
    public void goToDepartureTerminal(){
        try {
            rep.busDriverState(BusDriverState.DRIVING_FORWARD); 
            Thread.sleep(50);
        } catch (Exception e) {
            System.out.println("Thread: " + Thread.currentThread().getName() + " terminated.");
			System.out.println("Error: " + e.getMessage());
			System.exit(1);
        }

    }
}