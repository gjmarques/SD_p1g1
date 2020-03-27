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
	public static void main(String[] args) throws FileNotFoundException{

		// creates new logger
		File logger = new File("logger.txt");
		// if (logger.createNewFile()){
		// 	System.out.println("Logger created: " + logger.getName());
		// }else{
		// 	System.out.println("File already exists.");
		// }
		GenInfoRepo genInfoRepo = new GenInfoRepo(logger);

		// Initialize shared regions
		ArrivalLounge arrivalLounge = new ArrivalLounge(genInfoRepo);
		TempStorageArea tempStorageArea = new TempStorageArea(genInfoRepo);
		BaggageCollectionPoint baggageCollectionPoint = new BaggageCollectionPoint(genInfoRepo);
		BaggageReclaimOffice baggageReclaimOffice = new BaggageReclaimOffice(genInfoRepo);
		ArrivalTermTransfQuay arrivalTermTransfQuay = new ArrivalTermTransfQuay(Global.BUS_SIZE, Global.NR_FLIGHTS, genInfoRepo);
		DepartureTermTransfQuay departureTermTransfQuay = new DepartureTermTransfQuay(genInfoRepo);
		ExitAirport exitAirport = new ExitAirport(Global.NR_PASSENGERS, genInfoRepo);

		// Initialize busdriver and timer
		BusDriver busdriver = new BusDriver(arrivalTermTransfQuay, departureTermTransfQuay, genInfoRepo);
		busdriver.start();
		
		BusTimer timer = new BusTimer(arrivalTermTransfQuay);
		timer.start();

		//Itialize Porter
		Porter porter = new Porter(arrivalLounge, tempStorageArea, baggageCollectionPoint);
		porter.start();

		// Initialize passengers
		Passenger[] passengers = new Passenger[Global.NR_PASSENGERS];
		for (int i = 0; i < Global.NR_PASSENGERS; i++) {
			List<Integer> numBags = generateBags(Global.NR_FLIGHTS, Global.MAX_BAGS);
			passengers[i] = new Passenger(i, numBags, arrivalLounge, arrivalTermTransfQuay, departureTermTransfQuay, 
										baggageCollectionPoint, baggageReclaimOffice, exitAirport, genInfoRepo);
			System.out.println("Passenger id: " + i + "nr bags: " + numBags.toString());
			
			passengers[i].start();
			//System.out.println("PASSAGEIRO " + i + " -> " + numBags.toString());
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

		}

	}

	/**
	 * Generates a list with a random number of bags for each flight of a passenger
	 * 
	 * @param nrFlights
	 * @param maxBags
	 * @return List<Integer>
	 */
	public static List<Integer> generateBags(int nrFlights, int maxBags) {
		List<Integer> bags = new ArrayList<Integer>();

		for (int i = 0; i < nrFlights; i++) {
			Random r = new Random();
			bags.add(r.nextInt(maxBags + 1));
		}

		return bags;
	}
}