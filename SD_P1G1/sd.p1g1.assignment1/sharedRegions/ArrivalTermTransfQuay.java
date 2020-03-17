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
	private Boolean endOfDay = false;

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

	public void takeABus(int nFlight) {
		rl.lock();
		try {
			flightCount = nFlight+1;
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
				System.out.println("WAKE UP BUSDRIVER");
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

			System.out.println("ENTER THE BUS->"+ passengersEntering);
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

			System.out.println("PASSENGERS" + passengers);
			return passengersInside;
		} catch (Exception ex) {
			return 0;
		} finally {
			rl.unlock();
		}
	}

	public void parkTheBus() {
		passengersInside = 0;

		if(passengers == 0 && flightCount == maxFlights){
			endOfDay = true;
		}
	}

	/**
	 * @return char
	 */
	public char hasDaysWorkEnded() {
		rl.lock();
		try {	
			waitLine.signalAll();
			if (endOfDay)
				return 'E';	
			waitFull.await();

			if (passengers > 0)
				return 'W';

			else if (endOfDay)
				return 'E';

			return 'S';

		} catch (Exception ex) {
			return 'S';
		} finally {
			rl.unlock();
		}
	}
}