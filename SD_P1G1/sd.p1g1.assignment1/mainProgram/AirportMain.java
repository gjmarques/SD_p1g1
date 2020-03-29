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
		ArrivalTermTransfQuay arrivalTermTransfQuay = new ArrivalTermTransfQuay(Global.BUS_SIZE, Global.NR_FLIGHTS, genInfoRepo);
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

		/**
		 * List of every {@link Bag} of every flight occurring in this airport.
		 */
		List<List<Integer>> bags = generateBags(genInfoRepo, Global.NR_PASSENGERS, Global.NR_FLIGHTS, Global.MAX_BAGS);
		
		/**
		 * List of {@link entities.Passenger}s.
		 */
		Passenger[] passengers = new Passenger[Global.NR_PASSENGERS];
		for (int i = 0; i < Global.NR_PASSENGERS; i++) {
			passengers[i] = new Passenger(i, bags.get(i), arrivalLounge, arrivalTermTransfQuay, departureTermTransfQuay, 
										baggageCollectionPoint, baggageReclaimOffice, arrivalTerminalExit, departureTerminalEntrance, genInfoRepo);
			passengers[i].start();
		}


		try {
			for (int i = 0; i < Global.NR_PASSENGERS; i++) {
				passengers[i].join();
			}	

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

	/**
	 * Generates a list with a random number of bags for each flight of a passenger
	 * 
	 * @param nrFlights
	 * @param maxBags
	 * @param nrPassengers
	 * @param genInfoRepo
	 * @return List<Integer>
	 */
	public static List<List<Integer>> generateBags(GenInfoRepo genInfoRepo, int nrPassengers, int nrFlights, int maxBags) {

		
		List<List<Integer>> bagsPerPassenger = new ArrayList<List<Integer>>(nrPassengers);
		int[] bagsPerFlight = new int[nrFlights];

		for (int j= 0; j < nrPassengers; j++){
			List<Integer> bags = new ArrayList<Integer>();
			for (int i = 0; i < nrFlights; i++) {
				Random r = new Random();
				int result = r.nextInt(maxBags + 1);
				bags.add(result);
				bagsPerFlight[i] += result;
			}
			bagsPerPassenger.add(bags);

		}
		genInfoRepo.nrBagsPlanesHold(bagsPerFlight);

		return bagsPerPassenger;
	}
}