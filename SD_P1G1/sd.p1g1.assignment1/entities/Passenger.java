package entities;

import sharedRegions.*;
import entities.*;
import mainProgram.*;

import java.lang.reflect.Array;
import java.util.*;


public class Passenger extends Thread{
    
    private PassengerState state;
    private char destination;
    private int collectedBags;
    private int[] numBags = {0,0,0,0,0};
    private int id;
    //number of bags per flight
    private Hashtable<Integer, Integer> flights = new Hashtable<Integer, Integer>();

    private arrivalLounge arrivalLounge;
    private arrivalTermExit arrivalTermExit;
    private arrivalTermTransfQuay arrivalTermTransfQuay;
 

    public Passenger(int id, PassengerState state) {
        this.id = id;
        this.state = state;
    }

    /**
     * This method describes a passenger's day.
     */
    @Override
    public void run(){
        for(int i = 0; i < Global.NR_VOOS-1; i++){
            char c  =  arrivalLounge.whatShouldIDo(i);
            switch(c){
                case('a'):  arrivalTermExit.goHome(i);

                case('b'):  arrivalTermTransfQuay.takeABus();		   
                            arrivalTermTransfQuay.enterTheBus();
                            departureTermTransfQuay.leaveTheBus();
                            departureTermEntrance.prepareNextLeg(i);

                case('c'):  collectedBags = 0;
                            while(collectedBags != numBags[i]){
                                if(baggageCollectionPoint.goCollectABag()){
                                    collectedBags+=1;
                                }else { baggageReclaimOffice.reportMissingBags(numBags[i]-collectedBags);
                                    break;
                                }
                            arrivalTermExit.goHome(i);


                }
            }
        }
    }


    
    /** 
     * @param state
     */
    public void setState(PassengerState state) {
        this.state = state;
    }

    
    /** 
     * @return PassengerState
     */
    public PassengerState getPassengerState() {
        return this.state;
    }

    
    /** 
     * @param flightNr
     * @return PassengerState
     */
    public PassengerState getPassengerState(int flightNr) {
        return this.state;
    }

    
    /** 
     * @return int
     */
    public int getPassID() {
        return this.id;
    }

    /*
    * Returns number of bags per flight
    * @param flightNr
    * @return int
    */
    int getBags(int flightNr){
        return flights.get(flightNr);
    }


    
    /** 
     * @param o
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Passenger)) {
            return false;
        }
        Passenger passenger = (Passenger) o;
        return Objects.equals(state, passenger.state) && Objects.equals(arrivalLounge, passenger.arrivalLounge);
    }

    
    /** 
     * @return int
     */
    @Override
    public int hashCode() {
        return Objects.hash(state, arrivalLounge);
    }

    
    /** 
     * @return String
     */
    @Override
    public String toString() {
        return "{" +
            " state='" + getState() + "'" +
            "}";
    }


}