package mainProgram;

import java.util.*;

import entities.*;
import sharedRegions.*;

/**
 * AirportMain is the main thread of the program
 */

public class AirportMain {
	public static void main(String[] args) throws InterruptedException{
		
		
 		//Initialize shared regions
		ArrivalLounge arrivalLounge = new ArrivalLounge(Global.NR_PASSENGERS);
		ExitAirport exitAirport = new ExitAirport(Global.NR_PASSENGERS);

		
 		//Initialize passengers
		Passenger[] passengers = new Passenger[Global.NR_PASSENGERS];
		for(int i = 0; i < Global.NR_PASSENGERS; i++){
			Random r = new Random();

			boolean finalDestination = r.nextBoolean();
			List<Integer> numBags = generateBags(Global.NR_FLIGHTS, 0 /*Global.MAX_BAGS*/);
			passengers[i] = new Passenger(i, numBags, finalDestination, arrivalLounge, exitAirport);
			passengers[i].start();
			System.out.println("PASSAGEIRO "+ i +" -> "+numBags.toString());
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