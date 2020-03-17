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
    protected final ReentrantLock rl;
    protected final Condition planeHoldEmptyCV;
    private int numPassengers;
    private int passengerCount = 0;
    private Stack<Bag> bags = new Stack<>();


    // Create lock and conditions
    public ArrivalLounge(int numPassengers) {
        rl = new ReentrantLock(true);
        planeHoldEmptyCV = rl.newCondition();
        this.numPassengers = numPassengers;
    }

    // PASSENGER

    //Passengers decide what to do based on their final destination and number of bags
    public char whatShouldIDo(Bag[] bags, boolean finalDestination) {
        rl.lock();
        char whatToDo;
        try {
            if (bags.length == 0) {
                System.out.println("INFO: There is no bags to pick up!");
                whatToDo = 'a';
            }
            for (int b = 0; b < bags.length; b++) {
                //System.out.println(bags[b]);
                this.bags.push(bags[b]);
            }
            passengerCount++;
            if (passengerCount == numPassengers) {
                planeHoldEmptyCV.signal();
                passengerCount = 0;
            }
            if(finalDestination) whatToDo = 'c';
            else whatToDo = 'b';
            //return finalDestination ? 'c' : 'b';
            return whatToDo;
        } catch (Exception ex) {
            return 'a';
        } finally {
            rl.unlock();
        }
    }

	
    /** 
     * This method checks if the porter stays resting or not.
     * <p>
     * Returns true in case of rest; false in case of wake up.
     * @return boolean
     */
    public boolean takeARest() {
        boolean rest = true;
        rl.lock();
        try{
            planeHoldEmptyCV.await();
            rest = false;
            return rest;
        }catch(InterruptedException e){
            System.out.println("ERROR: takeARest()");
            return rest;
        }finally{
            rl.unlock();
        }
	}

	
    /** 
     * This method returns a Bag.
     * <p> Returns null if there is not any bags to pick.
     * @return Bag
     */
    public Bag tryToCollectBag() {
        rl.lock();
        Bag tmp = null;
        try{
            if(bags.empty()) return tmp = null;
            else tmp = bags.pop();
            return tmp;
        }catch(Exception e){
            System.out.println("ERROR: tryToCollectBag()");
            return tmp;
        }finally{
            rl.unlock();
        }   
    }
	

	public void noMoreBagsToCollect() {
        rl.lock();
        try{
            planeHoldEmptyCV.await();
            System.out.println("INFO: noMoreBagsToCollect()");
        }catch(Exception e){
            System.out.println("ERROR: noMoreBagsToCollect()");
        }finally{
            rl.unlock();
        }
	}

}