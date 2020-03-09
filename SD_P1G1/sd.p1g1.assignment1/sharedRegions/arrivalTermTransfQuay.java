package sharedRegions;

import entities.*;
import java.util.Queue;
import java.util.Stack;

public class arrivalTermTransfQuay{

    private Queue<Integer> waitingLine;
    private int busCapacity;
    

    public synchronized void takeABus(){
        passenger p = (passenger) Thread.currentThread();
		p.setState(passengerState.AT_THE_ARRIVAL_TRANSFER_TERMINAL);
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
}