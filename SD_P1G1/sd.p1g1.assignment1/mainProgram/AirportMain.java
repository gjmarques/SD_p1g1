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
<<<<<<< HEAD
		TempStorageArea tempStorageArea = new TempStorageArea();
		BaggageCollectionPoint baggageCollectionPoint = new BaggageCollectionPoint();
		ExitAirport exitAirport = new ExitAirport(Global.NR_PASSENGERS);

		//Itialize Porter
		int porterID = 1;
		Porter porter = new Porter(porterID, PorterState.WAITING_FOR_A_PLANE_TO_LAND, arrivalLounge, tempStorageArea, baggageCollectionPoint);
		porter.start();
		
 		//Initialize passengers
		Passenger[] passengers = new Passenger[Global.NR_PASSENGERS];
		for(int i = 0; i < Global.NR_PASSENGERS-1; i++){
			Random r = new Random();

			boolean finalDestination = r.nextBoolean();
			List<Integer> numBags = generateBags(Global.NR_FLIGHTS, 1 /*Global.MAX_BAGS*/);
			passengers[i] = new Passenger(i, numBags, finalDestination, arrivalLounge, exitAirport);
			passengers[i].start();
			//System.out.println("PASSAGEIRO "+ i +" -> "+numBags.toString());
=======
		ArrivalTermTransfQuay arrivalTermTransfQuay = new ArrivalTermTransfQuay(Global.BUS_SIZE, Global.NR_FLIGHTS);
		DepartureTermTransfQuay departureTermTransfQuay = new DepartureTermTransfQuay();
		ExitAirport exitAirport = new ExitAirport(Global.NR_PASSENGERS);

		// Initialize busdriver and timer
		BusDriver busdriver = new BusDriver(arrivalTermTransfQuay, departureTermTransfQuay);
		busdriver.start();
		
		BusTimer timer = new BusTimer(arrivalTermTransfQuay);
		timer.start();

		// Initialize passengers
		Passenger[] passengers = new Passenger[Global.NR_PASSENGERS];
		for (int i = 0; i < Global.NR_PASSENGERS; i++) {
			List<Integer> numBags = generateBags(Global.NR_FLIGHTS, Global.MAX_BAGS);
			passengers[i] = new Passenger(i, numBags, arrivalLounge, arrivalTermTransfQuay, departureTermTransfQuay, exitAirport);
			passengers[i].start();
			System.out.println("PASSAGEIRO " + i + " -> " + numBags.toString());
>>>>>>> b37d5f7efbbc7274c749c1787ab37098230ca506
		}

		try {
			for (int i = 0; i < Global.NR_PASSENGERS; i++) {
				passengers[i].join();
			}
			
			timer.stopTimer();
			timer.join();
			busdriver.join();
			
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

		System.out.println(bags);

		return bags;
	}
}