package sharedRegions;

import java.util.concurrent.locks.*;

import entities.*;

public class DepartureTermTransfQuay{

	private final ReentrantLock rl;
	private final Condition waitArrival;
	private final Condition waitEmpty;
	private int numPassengers = 0;
    private GenInfoRepo rep;


    public DepartureTermTransfQuay(GenInfoRepo rep) {
        rl = new ReentrantLock(true);
		waitEmpty = rl.newCondition();
        waitArrival = rl.newCondition();
        this.rep = rep;
    }

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