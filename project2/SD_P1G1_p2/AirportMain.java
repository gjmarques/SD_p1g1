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

		String hostname = null;
		/**
         * Inicialization Stub Areas
         */
        ArrivalLoungeStub arrivalLoungeStub = new ArrivalLoungeStub(hostname, Global.arrivalLoungeStub_PORT);
        ArrivalTermTransfQuayStub arrivalTermTransfQuayStub = new ArrivalTermTransfQuayStub(hostname, Global.arrivalTerminalExitStub_PORT);
        DepartureTermTransfQuayStub departureTermTransfQuayStub = new DepartureTermTransfQuayStub(hostname, Global.departureTermTransfQuayStub_PORT);
        BaggageCollectionPointStub baggageCollectionPointStub = new BaggageCollectionPointStub(hostname, Global.baggageCollectionPointStub_PORT);
        BaggageReclaimOfficeStub baggageReclaimOfficeStub = new BaggageReclaimOfficeStub(hostname, Global.baggageReclaimOfficeStub_PORT);
        ArrivalTerminalExitStub arrivalTerminalExitStub = new ArrivalTerminalExitStub(hostname, Global.arrivalTerminalExitStub_PORT);
        DepartureTerminalEntranceStub departureTerminalEntranceStub = new DepartureTerminalEntranceStub(hostname, Global.departureTermTransfQuayStub_PORT);
		GenInfoRepoStub repoStub = new GenInfoRepoStub(hostname, Global.genRepo_PORT);
		TempStorageAreaStub tempStorageAreaStub = new TempStorageAreaStub(hostname, Global.tempStorageArea_PORT);


		/**
		 *{@link entities.BusDriver}
		 */
		//BusDriver busdriver = new BusDriver(arrivalTermTransfQuay, departureTermTransfQuay);
		//busdriver.start();
		/**
		 *{@link entities.BusTimer}
		 */
		//BusTimer timer = new BusTimer(arrivalTermTransfQuay);
		//timer.start();
		/**
		 *{@link entities.Porter}
		 */
		//PorterMain porter = new PorterMain(arrivalLoungeStub, tempStorageAreaStub, baggageCollectionPointStub, repoStub);
		//porter.start();

		// passar os stubs como argumento!!!
		//PassengerMain passengerMain = new PassengerMain(arrivalLoungeStub, arrivalTermTransfQuayStub, departureTermTransfQuayStub,
		//baggageCollectionPointStub, baggageReclaimOfficeStub, arrivalTerminalExitStub, departureTerminalEntranceStub, repoStub);



		
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