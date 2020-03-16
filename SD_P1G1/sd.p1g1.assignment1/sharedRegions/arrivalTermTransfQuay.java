package sharedRegions;

import entities.*;
import java.util.Queue;
import java.util.Stack;

public class arrivalTermTransfQuay{

    private Queue<Integer> waitingLine;
    private int busCapacity;
    

    public synchronized void takeABus(){
        Passenger p = (Passenger) Thread.currentThread();
		p.setState(PassengerState.AT_THE_ARRIVAL_TRANSFER_TERMINAL);
		waitingLine.add(p.getPassID());

        notifyAll();
        
        if(waitingLine.size() == busCapacity) //signal busdrive

		while(waitingLine.contains(p.getPassID())){
			try{
				wait();
			}catch(InterruptedException e){}
		}
    }

    public synchronized void enterTheBus(){

    }


	public static void annoucingBusBoarding() {
	}

	public static void parkTheBus() {
	}

	
	/** 
	 * This method checks if work day has ended
	 * E - work day ended
	 * @return char
	 */
	public static char hasDaysWorkEnded() {
		return 0;
	}
}