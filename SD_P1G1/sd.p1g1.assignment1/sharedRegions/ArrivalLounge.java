package sharedRegions;

import entities.*;
import java.util.*;
import java.util.concurrent.locks.*;

/**
 * This datatype implements the ArrivalLounge shared memory region. 
 * In this shared region, the Passengers decide on their course of action
 * and the Porter ..........
 */
public class ArrivalLounge {

    private boolean endOfDay = false;
    private final ReentrantLock rl;
    private final Condition waitingPlane;
    private int numPassengers;
    private int passengerCount = 0;
    private Stack<Bag> bags = new Stack<>();;


    // Create lock and conditions
    public ArrivalLounge(int numPassengers) {
        rl = new ReentrantLock(true);
        waitingPlane = rl.newCondition();
        this.numPassengers = numPassengers;
    }

    
    /** 
     * @return char
     */
    // PORTER

    // public synchronized char takeARest() {
    //     while (!endOfDay) {
    //         try {
    //             wait();
    //         } catch (InterruptedException e) {
    //             // log?
    //             return 'S';
    //         }
    //     }

    //     if (endOfDay)
    //         return 'E';

    //     if (passengerCount == 5)
    //         return 'C';

    //     return 'S';

    // }

    // PASSENGER

    //Passengers decide what to do based on their final destination and number of bags
    public char whatShouldIDo(Bag[] bags, boolean finalDestination) {
        rl.lock();
        try {
            if (bags.length == 0) {
                return 'a';
            }

            for (int b = 0; b < bags.length; b++) {
                this.bags.push(bags[b]);
            }

            passengerCount++;
            if (passengerCount == numPassengers) {
                passengerCount = 0;
                waitingPlane.signal();
            }

            return finalDestination ? 'c' : 'b';
        } catch (Exception ex) {
            return 'a';
        } finally {
            rl.unlock();
        }
    }

	
    /** 
     * @return char
     */
    public static char takeARest() {
		return 0;
	}

	
    /** 
     * @return Bag
     */
    public static Bag tryToCollectBag() {
		return null;
	}

	public static void noMoreBagsToCollect() {
	}

    // public static Bag tryToCollectBag() {
    //     return null;
    // }

    // public static void noMoreBagsToCollect() {
    // }
}