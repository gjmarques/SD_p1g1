package sharedRegions;

import entities.*;
import java.util.Queue;
import java.util.Stack;

public class arrivalLounge {

    boolean endOfDay;
    // main init value
    int passengerCount;
    Stack<bag>[] planehold;

    //PORTER

    public synchronized char takeARest(){
        while(!endOfDay){
        try
        { wait ();
        }
        catch (InterruptedException e)
        { return 'S';
        }
      }

      if (endOfDay) return 'E';   
      
      if (passengerCount == 5) return 'C';   
      
      return 'S';

    }

    //PASSENGER

    public synchronized char whatShouldIDo(int nLand){

        return 'c';
    }
   
/*  bag tryToCollectABag()
    void noMoreBagsToCollect() */

/*  void setEndOfWork() */

}