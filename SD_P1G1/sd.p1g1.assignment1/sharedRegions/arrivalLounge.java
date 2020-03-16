package sharedRegions;

import entities.*;
import java.util.Queue;
import java.util.Stack;

public class arrivalLounge {

    boolean endOfDay = false;
    // main init value
    int passengerCount;
    Stack<Bag>[] planehold;

    
    /** 
     * @return char
     */
    //PORTER

    
    public synchronized char takeARest(){
        while(!endOfDay){
        try{ 
            wait ();
        }
        catch (InterruptedException e){ 
            // log?
            return 'S';
        }
      }

      if (endOfDay) return 'E';   
      
      if (passengerCount == 5) return 'C';   
      
      return 'S';

    }

    //PASSENGER
    /** 
     * @param nLand
     * @return char
     */
    public synchronized char whatShouldIDo(int nLand){

        return 'c';
    }

	
    /** 
     * @return Bag
     */
    public static Bag tryToCollectBag() {
		return null;
	}

	public static void noMoreBagsToCollect() {
	}
   
/*  bag tryToCollectABag()
    void noMoreBagsToCollect() */

/*  void setEndOfWork() */

}