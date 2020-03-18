package sharedRegions;

import java.util.concurrent.locks.*;

public class DepartureTermTransfQuay{

	private final ReentrantLock rl;
	private final Condition waitArrival;
	private final Condition waitEmpty;
	private int numPassengers = 0;


    public DepartureTermTransfQuay() {
        rl = new ReentrantLock(true);
		waitEmpty = rl.newCondition();
		waitArrival = rl.newCondition();
    }

    public void parkTheBusAndLetPassengerOff(int busPassengers) {
        rl.lock();
        try {
			numPassengers += busPassengers;
			waitArrival.signalAll();
            waitEmpty.await();
        } catch(Exception ex) {}
        finally {
            rl.unlock();
        }
    }

    public void leaveTheBus() {
        rl.lock();
        try {
            waitArrival.await();   
            numPassengers--;
            if(numPassengers == 0) {
                waitEmpty.signal();
            }
        } catch(Exception ex) {}
        finally {
            rl.unlock();
        }
    }
}