package serverSide;

import java.util.concurrent.locks.*;

import clientSide.*;

/**
 * This datatype implements the Arrival Terminal TRansfer Quay shared memory region. In this
 * shared region, the BusDriver stand by until either enough Passengers arrive to fill the bus or the departure time is signalled.
 * Meanwhile the Passengers that arrive wait first until they have enough room to board the bus and thenuntil the BusDriver signals them to board.
 */
public class ArrivalTermTransfQuay {

    /**
     * A reentrant mutual exclusion Lock with the same basic behavior and semantics as the implicit monitor lock 
     * accessed using synchronized methods and statements
     */
	private final ReentrantLock rl;

	/**
     * Synchronization point where the {@link entities.Passenger}s until there is
	 * enough space for them to enter the bus
     */
	private final Condition waitLine;

	/**
     * Synchronization point where the {@link entities.BusDriver} waits for either the bus capacity to be reached
	 * or for the arrival of at least one {@link entities.Passenger} and the departure time
     */
	private final Condition waitFull;

    /**
     * Synchronization point where the {@link entities.Passenger}s wait for the {@link entities.BusDriver}
     * to signal they can enter the bus
     */
	private final Condition waitAnnouncement;
	
	/**
     * Synchronization point where the {@link entities.BusDriver} waits for all {@link entities.Passenger}s to enter the bus
     */
	private final Condition waitEnter;

    /**
     * Current flight count
     */
	private int flightCount;

    /**
     * Total number of flights to be simulated
     */
	private int maxFlights;

    /**
     * Total bus capacity
     */
	private int busSize;

	/**
     * Number of {@link entities.Passenger}s that arrived in the Arrival Terminal Transfer Quay
     */
	private int passengers = 0;

    /**
     * Number of {@link entities.Passenger}s currently inside the bus
     */
	private int passengersInside = 0;

	/**
     * Number of {@link entities.Passenger}s which are going to enter the bus in the next journey
     */
	private int passengersEntering = 0;

    /**
     * General Information Repository {@link GenInfoRepo}
     */
	private GenInfoRepoStub rep;

	/**
     * Instantiates ArrivalTermTransQuay shared region
     * @param busSize total bus capacity
	 * @param maxFlights total number of flights to be simulated
     * @param rep {@link GenInfoRepo}.
     */
	public ArrivalTermTransfQuay(int busSize, int maxFlights, GenInfoRepoStub rep) {
		rl = new ReentrantLock(true);
		waitLine = rl.newCondition();
		waitFull = rl.newCondition();
		waitAnnouncement = rl.newCondition();
		waitEnter = rl.newCondition();
		this.busSize = busSize;
		this.maxFlights = maxFlights;
		this.rep = rep;
	}

    /**
     * This method updates internal flight count.
     * @param nFlight
     */
	public void setFlight(int nFlight){
		flightCount = nFlight+1;
	}

    /**
     * Passengers wait until they have room in the bus, signal the passenger if bus capacity is reach and then wait
	 * for the {@link entities.BusDriver} to announce they can enter the bus
     * @param passengerID
     */
	public void takeABus(int passengerID) {
		rl.lock();
		try {
			rep.passengerState(passengerID, PassengerState.AT_THE_ARRIVAL_TRANSFER_TERMINAL);
			passengers++;
			
			rep.busWaitingLine(passengerID);
			
			while (passengersEntering >= busSize) {
				waitLine.await();
			}
			passengersEntering++;
			if (passengersEntering == busSize) {
				waitFull.signal();
			}
	
			waitAnnouncement.await();
		} catch (Exception e) {
			System.out.println("Thread: " + Thread.currentThread().getName() + " terminated.");
			System.out.println("Error: " + e.getMessage());
			System.exit(1);
		} finally {
			rl.unlock();
		}
	}

	/**
     * This method signals the {@link entities.BusDriver} at a fixed time interval defined by the {@link entities.BusTimer}
     */
	public void departureTime() {
		rl.lock();
		try {
			if (passengers > 0 || passengers == 0 && flightCount == maxFlights) {
				waitFull.signalAll();
			}

		} catch (Exception e) {
			System.out.println("Thread: " + Thread.currentThread().getName() + " terminated.");
			System.out.println("Error: " + e.getMessage());
			System.exit(1);
		} finally {
			rl.unlock();
		}
	}

	/**
     * This method signals the {@link entities.BusDriver} when all {@link entities.Passenger}s have entered the bus
	 * @param passengerID
     */
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

		} catch (Exception e) {
			System.out.println("Thread: " + Thread.currentThread().getName() + " terminated.");
			System.out.println("Error: " + e.getMessage());
			System.exit(1);
		} finally {
			rl.unlock();
		}
	}

	/**
     * This method signals all {@link entities.Passenger}s waiting to enter the bus. 
	 * {@link entities.BusDriver} waits until all {@link entities.Passenger}s have entered.
	 * 
	 * @return int
     */
	public int annoucingBusBoarding() {
		rl.lock();
		try {
			waitAnnouncement.signalAll();
			waitEnter.await();
			passengers = passengers - passengersInside;

			return passengersInside;
		} catch (Exception e) {
			System.out.println("Thread: " + Thread.currentThread().getName() + " terminated.");
			System.out.println("Error: " + e.getMessage());
			System.exit(1);
			return -1;
		} finally {
			rl.unlock();
		}
	}

	/**
	 * {@link entities.BusDriver} arrives at the Arrival Terminal Transfer Quay and internal variable of {@link entities.Passenger}s 
	 * inside the bus is reset.
     */
	public void parkTheBus() {
		
		passengersInside = 0;	
		rep.busDriverState(BusDriverState.PARKING_AT_THE_ARRIVAL_TERMINAL);	

	}

	/**
	 ** {@link entities.BusDriver} verifies if he has any passangers to transport and whether
	 *  the last flight has been simulated so he can terminate 
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

			return 'z';

		} catch (Exception e) {
			System.out.println("Thread: " + Thread.currentThread().getName() + " terminated.");
			System.out.println("Error: " + e.getMessage());
			System.exit(1);
			return 'z';
		} finally {
			rl.unlock();
		}
	}

	/**
	 *{@link entities.BusDriver} sleeps for 50 milliseconds to simulate deslocation to the {@link DepartureTermTransfQuay}
	 */
	public void goToArrivalTerminal(){
        try {
			rep.busDriverState(BusDriverState.DRIVING_BACKWARD);
            Thread.sleep(50);
        } catch (Exception e) {
			System.out.println("Thread: " + Thread.currentThread().getName() + " terminated.");
			System.out.println("Error: " + e.getMessage());
			System.exit(1);
		}

    }
}