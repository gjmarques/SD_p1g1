package mainProgram;

import java.util.*;

import entities.*;
import sharedRegions.*;

/**
 * RepairShop is the main thread of the program
 */
public class airportMain {

	public static void main(String[] args) throws InterruptedException{

		//initiatePassengers();
		//generateBags();

	}


	/**
	* Number of Customers 
	*/
	//public static final int N = 30; 


	public static void initiatePassengers(){
		ArrayList<Passenger> passengersList = new ArrayList<Passenger>();
		for(int i = 0; i < Global.NR_PASSENGERS-1; i++){
			Passenger passenger = new Passenger(i, pState);
			passengersList.add(passenger);
			passenger.start();
		}
	}

	public static void generateBags(){
        int[] array = {0,1,2,2};
        int val = (int) Math.floor(Math.random() * Math.floor(3));
        for(int i = 0; i < val; i++){
			//Bag bag = new Bag(dest, id);
        }
	}



	
	//generatelostbag
}