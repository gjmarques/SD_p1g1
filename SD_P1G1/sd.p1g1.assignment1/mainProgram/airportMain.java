package mainProgram;

import java.util.*;

import entities.*;
import sharedRegions.*;

/**
 * This represents an airport, where passengers arrive from a flight and can take multiple actions.
 * 
 * @author Gonçalo Marques, 80327
 * @author Joana Bernardino, 76475
 * 
 */
public class airportMain {

	
	/** 
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException{

		//initiatePassengers();
		//generateBags();

	}


	/**
	* Number of Customers 
	*/
	//public static final int N = 30; 

	/**
	 * This method instantiates the passengers
	*/
	public static void initiatePassengers(){
		ArrayList<Passenger> passengersList = new ArrayList<Passenger>();
		for(int i = 0; i < Global.NR_PASSENGERS-1; i++){
			Passenger passenger = new Passenger(i, pState);
			passengersList.add(passenger);
			passenger.start();
		}
	}

	/**
	 * This method instantiates the number of bags per passenger
	 */
	public static void generateBags(){
        int[] array = {0,1,2,2};
        int val = (int) Math.floor(Math.random() * Math.floor(3));
        for(int i = 0; i < val; i++){
			//Bag bag = new Bag(dest, id);
        }
	}



	
	//generatelostbag
}