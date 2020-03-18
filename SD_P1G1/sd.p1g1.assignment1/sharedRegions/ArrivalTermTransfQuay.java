package sharedRegions;

import java.util.concurrent.locks.*;

public class ArrivalTermTransfQuay {

	private final ReentrantLock rl;
	private final Condition waitLine;
	private final Condition waitFull;
	private final Condition waitAnnouncement;
	private final Condition waitEnter;
	private int flightCount;
	private int maxFlights;
	private int busSize;
	private int passengers = 0;
	private int passengersInside = 0;
	private int passengersEntering = 0;

	// create lock and conditions
	public ArrivalTermTransfQuay(int busSize, int maxFlights) {
		rl = new ReentrantLock(true);
		waitLine = rl.newCondition();
		waitFull = rl.newCondition();
		waitAnnouncement = rl.newCondition();
		waitEnter = rl.newCondition();
		this.busSize = busSize;
		this.maxFlights = maxFlights;
	}

	public void setFlight(int nFlight){

		flightCount = nFlight+1;
	}

	public void takeABus() {
		rl.lock();
		try {
			passengers++;
			while (passengersEntering >= busSize) {
				waitLine.await();
			}
			passengersEntering++;
			if (passengersEntering == busSize) {
				waitFull.signal();
			}
			waitAnnouncement.await();
		} catch (Exception ex) {
		} finally {
			rl.unlock();
		}
	}

	public void departureTime() {
		rl.lock();
		try {
			if (passengers > 0) {
				waitFull.signalAll();
			}

		} catch (Exception ex) {
		} finally {
			rl.unlock();
		}
	}

	public void enterTheBus() {
		rl.lock();
		try {

			passengersInside++;
			if(passengersInside == passengersEntering){
				passengersEntering = 0;
				waitEnter.signal();
			}

		} catch (Exception ex) {
		} finally {
			rl.unlock();
		}
	}

	public int annoucingBusBoarding() {
		rl.lock();
		try {
			waitAnnouncement.signalAll();
			waitEnter.await();
			passengers = passengers - passengersInside;

			return passengersInside;
		} catch (Exception ex) {
			return 0;
		} finally {
			rl.unlock();
		}
	}

	public void parkTheBus() {
		
		passengersInside = 0;		

	}

	/**
	 * @return char
	 */
	public char hasDaysWorkEnded() {
		rl.lock();
		try {
			waitLine.signalAll();
			
			if (passengers == 0 && flightCount == maxFlights)
				return 'E';	
			
			waitFull.await();

			if (passengers > 0)
				return 'W';

			else if (passengers == 0 && flightCount == maxFlights)
				return 'E';

			return 'S';

		} catch (Exception ex) {
			return 'S';
		} finally {
			rl.unlock();
		}
	}
}