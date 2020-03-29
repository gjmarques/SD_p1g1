package mainProgram;

import java.io.*;
import java.util.*;

import entities.*;
import sharedRegions.*;

/**
 * AirportMain is the main thread of the program
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
			//System.out.println("Logger created: " + logger.getName());
		}else{
			logger.delete();
			logger.createNewFile();
			// System.out.println("File already exists.");
			
		}
		GenInfoRepo genInfoRepo = new GenInfoRepo(logger);

		// Initialize shared regions
		ArrivalLounge arrivalLounge = new ArrivalLounge(genInfoRepo);
		TempStorageArea tempStorageArea = new TempStorageArea(genInfoRepo);
		BaggageCollectionPoint baggageCollectionPoint = new BaggageCollectionPoint(Global.NR_PASSENGERS, genInfoRepo);
		BaggageReclaimOffice baggageReclaimOffice = new BaggageReclaimOffice(genInfoRepo);
		ArrivalTermTransfQuay arrivalTermTransfQuay = new ArrivalTermTransfQuay(Global.BUS_SIZE, Global.NR_FLIGHTS, genInfoRepo);
		DepartureTermTransfQuay departureTermTransfQuay = new DepartureTermTransfQuay(genInfoRepo);
		ArrivalTerminalExit arrivalTerminalExit = new ArrivalTerminalExit(Global.NR_PASSENGERS, genInfoRepo);
		DepartureTerminalEntrance departureTerminalEntrance = new DepartureTerminalEntrance(Global.NR_PASSENGERS, genInfoRepo);
		arrivalTerminalExit.setDepartureTerminal(departureTerminalEntrance);
		departureTerminalEntrance.setArrivalTerminal(arrivalTerminalExit);

		// Initialize busdriver and timer
		BusDriver busdriver = new BusDriver(arrivalTermTransfQuay, departureTermTransfQuay);
		busdriver.start();
		
		BusTimer timer = new BusTimer(arrivalTermTransfQuay);
		timer.start();

		//Itialize Porter
		Porter porter = new Porter(arrivalLounge, tempStorageArea, baggageCollectionPoint);
		porter.start();

		List<List<Integer>> bags = generateBags(genInfoRepo, Global.NR_PASSENGERS, Global.NR_FLIGHTS, Global.MAX_BAGS);
		

		// Initialize passengers
		Passenger[] passengers = new Passenger[Global.NR_PASSENGERS];
		for (int i = 0; i < Global.NR_PASSENGERS; i++) {
			passengers[i] = new Passenger(i, bags.get(i), arrivalLounge, arrivalTermTransfQuay, departureTermTransfQuay, 
										baggageCollectionPoint, baggageReclaimOffice, arrivalTerminalExit, departureTerminalEntrance);
			passengers[i].start();
		}


		try {
			for (int i = 0; i < Global.NR_PASSENGERS; i++) {
				passengers[i].join();
			}	
			System.out.println("PASSENGER OVER");

			porter.join();
			System.out.println("PORTER OVER");

			busdriver.join();
			System.out.println("BUSDRIVER OVER");

			genInfoRepo.finalReport();

			timer.stopTimer();
			timer.join();
	

			
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