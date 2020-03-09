package entities;

import sharedRegions.*;

import java.lang.reflect.Array;
import java.util.Objects;
import entities.passengerState;

public class passenger extends Thread{
    
    private passengerState state;
    private char destination;
    private int collectedBags;
    private int[] numBags = {0,0,0,0,0};
    private int passID = 0;

    private arrivalLounge arrivalLounge;
    private arrivalTermExit arrivalTermExit;
    private arrivalTermTransfQuay arrivalTermTransfQuay;
 

    public passenger(passengerState state) {
        this.state = state;
    }

    @Override
    public void run(){
        for(int i = 0; i < 5;i++){
        char c  =  arrivalLounge.whatShouldIDo(i);
        switch(c){
            case('a'):  arrivalTermExit.goHome(i);

            case('b'):  arrivalTermTransfQuay.takeABus();		   
                        arrivalTermTransfQuay.enterTheBus();
                        departureTerminalTransferQuay.leaveTheBus();
                        departureTerminalEntrance.prepareNextLeg(i);

            case('c'):  collectedBags = 0;
                        while(collectedBags != numBags[i]){
                            if(baggageCollectionPoint.goCollectABag()){
                                collectedBags+=1;
                            }else { baggageReclaimOffice.reportMissingBags(numBags[i]-collectedBags);
                                break;
                            }
                        arrivalTerminalExit.goHome(i);


            }
        }
    }


    public void setState(passengerState state) {
        this.state = state;
    }

    public int getPassID() {
        return this.passID;
    }


    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof passenger)) {
            return false;
        }
        passenger passenger = (passenger) o;
        return Objects.equals(state, passenger.state) && Objects.equals(arrivalLounge, passenger.arrivalLounge);
    }

    @Override
    public int hashCode() {
        return Objects.hash(state, arrivalLounge);
    }

    @Override
    public String toString() {
        return "{" +
            " state='" + getState() + "'" +
            "}";
    }


}