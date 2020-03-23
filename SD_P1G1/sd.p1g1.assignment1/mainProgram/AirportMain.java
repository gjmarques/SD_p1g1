package mainProgram;

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
	public static void main(String[] args) throws InterruptedException {

		// Initialize shared regions
		ArrivalLounge arrivalLounge = new ArrivalLounge(Global.NR_PASSENGERS, Global.NR_FLIGHTS);
		TempStorageArea tempStorageArea = new TempStorageArea();
		BaggageCollectionPoint baggageCollectionPoint = new BaggageCollectionPoint();
		BaggageReclaimOffice baggageReclaimOffice = new BaggageReclaimOffice();
		ArrivalTermTransfQuay arrivalTermTransfQuay = new ArrivalTermTransfQuay(Global.BUS_SIZE, Global.NR_FLIGHTS);
		DepartureTermTransfQuay departureTermTransfQuay = new DepartureTermTransfQuay();
		ExitAirport exitAirport = new ExitAirport(Global.NR_PASSENGERS);

		// Initialize busdriver and timer
		BusDriver busdriver = new BusDriver(arrivalTermTransfQuay, departureTermTransfQuay);
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
			passengers[i] = new Passenger(i, numBags, arrivalLounge, arrivalTermTransfQuay, departureTermTransfQuay, baggageCollectionPoint, baggageReclaimOffice, exitAirport);
			passengers[i].start();
			System.out.println("PASSAGEIRO " + i + " -> " + numBags.toString());
		}

		try {
			for (int i = 0; i < Global.NR_PASSENGERS; i++) {
				passengers[i].join();
			}	
			System.out.println("PASSENGER OVER");

			porter.join();
			System.out.println("PORTER OVER");

			busdriver.join();
			System.out.println("BUSMERDAS OVER");

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