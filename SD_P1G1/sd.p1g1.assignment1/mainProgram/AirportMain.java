package mainProgram;

import java.util.*;
import java.util.Timer;

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
		ArrivalLounge arrivalLounge = new ArrivalLounge(Global.NR_PASSENGERS);
		TempStorageArea tempStorageArea = new TempStorageArea();
		BaggageCollectionPoint baggageCollectionPoint = new BaggageCollectionPoint();
		ArrivalTermTransfQuay arrivalTermTransfQuay = new ArrivalTermTransfQuay(Global.BUS_SIZE, Global.NR_FLIGHTS);
		DepartureTermTransfQuay departureTermTransfQuay = new DepartureTermTransfQuay();
		ExitAirport exitAirport = new ExitAirport(Global.NR_PASSENGERS);

		// Initialize busdriver and timer
		BusDriver busdriver = new BusDriver(arrivalTermTransfQuay, departureTermTransfQuay);
		busdriver.start();
		
		BusTimer timer = new BusTimer(arrivalTermTransfQuay);
		timer.start();

		//Itialize Porter
		// int porterID = 1;
		// Porter porter = new Porter(porterID, PorterState.WAITING_FOR_A_PLANE_TO_LAND, arrivalLounge, tempStorageArea, baggageCollectionPoint);
		// porter.start();

		// Initialize passengers
		Passenger[] passengers = new Passenger[Global.NR_PASSENGERS];
		for (int i = 0; i < Global.NR_PASSENGERS; i++) {
			List<Integer> numBags = generateBags(Global.NR_FLIGHTS, Global.MAX_BAGS);
			passengers[i] = new Passenger(i, numBags, arrivalLounge, arrivalTermTransfQuay, departureTermTransfQuay, exitAirport);
			passengers[i].start();
			System.out.println("PASSAGEIRO " + i + " -> " + numBags.toString());
		}

		try {
			for (int i = 0; i < Global.NR_PASSENGERS; i++) {
				passengers[i].join();
			}	
			
			busdriver.join();
			//porter.join();
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