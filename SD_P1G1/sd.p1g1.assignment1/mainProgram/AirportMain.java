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
	public static void main(String[] args) throws InterruptedException{
		
		
 		//Initialize shared regions
		ArrivalLounge arrivalLounge = new ArrivalLounge(Global.NR_PASSENGERS);
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
		}

		try {
            for(int i = 0; i < Global.NR_PASSENGERS; i++) {
                passengers[i].join();
            }
        } catch (Exception e) {

        }

	}

	/** 
	 * Generates a list with a random number of bags for each flight of a passenger
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