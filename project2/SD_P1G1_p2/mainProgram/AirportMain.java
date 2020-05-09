package mainProgram;

import java.io.*;
import java.util.*;

import entities.*;
import sharedRegions.*;

/**
 * AirportMain is the main thread of the program where all entities and shared regions are instantiated and terminated.
 * It is also where the logger file is created.
 */

public class AirportMain {

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws IOException{

		// creates new logger
		File logger = new File("logger.txt");
		if (logger.createNewFile()){
		}else{
			logger.delete();
			logger.createNewFile();
			
		}

		GenInfoRepo genInfoRepo = new GenInfoRepo(logger);

		// Initialize shared regions
		/**
		 * {@link sharedRegions.ArrivalLounge}
		 */
		ArrivalLounge arrivalLounge = new ArrivalLounge(genInfoRepo);
		/**
		 * {@link sharedRegions.TempStorageArea}
		 */
		TempStorageArea tempStorageArea = new TempStorageArea(genInfoRepo);
		/**
		 * {@link sharedRegions.BaggageCollectionPoint}
		 */
		BaggageCollectionPoint baggageCollectionPoint = new BaggageCollectionPoint(Global.NR_PASSENGERS, genInfoRepo);
		/**
		 * {@link sharedRegions.BaggageReclaimOffice}
		 */
		BaggageReclaimOffice baggageReclaimOffice = new BaggageReclaimOffice(genInfoRepo);
		/**
		 * {@link sharedRegions.ArrivalTermTransfQuay}
		 */
		ArrivalTermTransfQuay arrivalTermTransfQuay = new ArrivalTermTransfQuay(Global.BUS_SIZE, Global.flight_nrS, genInfoRepo);
		/**
		 * {@link sharedRegions.DepartureTermTransfQuay}
		 */
		DepartureTermTransfQuay departureTermTransfQuay = new DepartureTermTransfQuay(genInfoRepo);
		/**
		 * {@link sharedRegions.ArrivalTerminalExit}
		 */
		ArrivalTerminalExit arrivalTerminalExit = new ArrivalTerminalExit(Global.NR_PASSENGERS, genInfoRepo);
		/**
		 * {@link sharedRegions.DepartureTerminalEntrance}
		 */
		DepartureTerminalEntrance departureTerminalEntrance = new DepartureTerminalEntrance(Global.NR_PASSENGERS, genInfoRepo);
		
		arrivalTerminalExit.setDepartureTerminal(departureTerminalEntrance);
		departureTerminalEntrance.setArrivalTerminal(arrivalTerminalExit);

		/**
		 *{@link entities.BusDriver}
		 */
		BusDriver busdriver = new BusDriver(arrivalTermTransfQuay, departureTermTransfQuay);
		busdriver.start();
		/**
		 *{@link entities.BusTimer}
		 */
		BusTimer timer = new BusTimer(arrivalTermTransfQuay);
		timer.start();
		/**
		 *{@link entities.Porter}
		 */
		Porter porter = new Porter(arrivalLounge, tempStorageArea, baggageCollectionPoint, genInfoRepo);
		porter.start();

		// passar os stubs como argumento!!!
		PassengerMain passengerMain = new PassengerMain(arrivalLounge, arrivalTermTransfQuay, departureTermTransfQuay,
		baggageCollectionPoint, baggageReclaimOffice, arrivalTerminalExit, departureTerminalEntrance, genInfoRepo);



		
		/**
		 * List of {@link entities.Passenger}s.
		 */
		/*Passenger[] passengers = new Passenger[Global.NR_PASSENGERS];
		for (int i = 0; i < Global.NR_PASSENGERS; i++) {
			passengers[i] = new Passenger(i, bags.get(i), arrivalLounge, arrivalTermTransfQuay, departureTermTransfQuay, 
										baggageCollectionPoint, baggageReclaimOffice, arrivalTerminalExit, departureTerminalEntrance, genInfoRepo);
			passengers[i].start();
		*/

		
		try {
			// for (int i = 0; i < Global.NR_PASSENGERS; i++) {
				// passengers[i].join();
			// }	

			porter.join();

			busdriver.join();

			timer.stopTimer();
			timer.join();

			genInfoRepo.finalReport();
			
		} catch (Exception e) {
			System.out.println("Thread: " + Thread.currentThread().getName() + " terminated.");
			System.out.println("Error: " + e.getMessage());
			System.exit(1);
		}

	}
}