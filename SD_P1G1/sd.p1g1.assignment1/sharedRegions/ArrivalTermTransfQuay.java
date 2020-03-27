package sharedRegions;

import java.util.concurrent.locks.*;

import entities.*;

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

	private GenInfoRepo rep;

	// create lock and conditions
	public ArrivalTermTransfQuay(int busSize, int maxFlights, GenInfoRepo rep) {
		rl = new ReentrantLock(true);
		waitLine = rl.newCondition();
		waitFull = rl.newCondition();
		waitAnnouncement = rl.newCondition();
		waitEnter = rl.newCondition();
		this.busSize = busSize;
		this.maxFlights = maxFlights;
		this.rep = rep;
	}

	public void setFlight(int nFlight){
		flightCount = nFlight+1;
	}

	public void takeABus(int passengerID) {
		rl.lock();
		//boolean entered = true;
		try {
			System.out.println("ArrivalTermTransfQuay.takeaBus");
			rep.passengerState(passengerID, PassengerState.AT_THE_ARRIVAL_TRANSFER_TERMINAL);
			passengers++;
			
			rep.busWaitingLine(passengerID);
			
			while (passengersEntering >= busSize) {
				//entered = false;
				waitLine.await();
			}
			passengersEntering++;
			if (passengersEntering == busSize) {
				waitFull.signal();
			}
			//if(entered) rep.busSitting(passengerID);
	
			waitAnnouncement.await();
		} catch (Exception ex) {
		} finally {
			rl.unlock();
		}
	}

	public void departureTime() {
		rl.lock();
		try {
			if (passengers > 0 || passengers == 0 && flightCount == maxFlights) {
				waitFull.signalAll();
			}

		} catch (Exception ex) {
		} finally {
			rl.unlock();
		}
	}

	public void enterTheBus(int passengerID) {
		rl.lock();
		try {
			rep.passengerState(passengerID, PassengerState.TERMINAL_TRANSFER);
			passengersInside++;

			rep.busSitting(passengerID);
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
		rep.busDriverState(BusDriverState.PARKING_AT_THE_ARRIVAL_TERMINAL);	

	}

	/**
	 * @return char
	 */
	public char hasDaysWorkEnded() {
		rl.lock();
		try {
			rep.busDriverState(BusDriverState.PARKING_AT_THE_ARRIVAL_TERMINAL);
			waitLine.signalAll();
			
			if (passengers == 0 && flightCount == maxFlights)
				return 'E';	
			
			waitFull.await();

			if (passengers > 0)
				return 'W';

			// else if (passengers == 0 && flightCount == maxFlights)
			// 	return 'E';

			return 'F';

		} catch (Exception ex) {
			return 'F';
		} finally {
			rl.unlock();
		}
	}

	public void goToArrivalTerminal(){
        try {
			rep.busDriverState(BusDriverState.DRIVING_BACKWARD);
            //setState(BusDriverState.DRIVING_BACKWARD); 
            //Thread.sleep(50);
        } catch (Exception e) {}

    }
}