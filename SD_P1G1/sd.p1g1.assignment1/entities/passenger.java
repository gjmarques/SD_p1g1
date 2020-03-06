package entities;

import sharedRegions.*;

import java.lang.reflect.Array;
import java.util.Objects;
import entities.passengerState;

public class passenger extends Thread{
    
    private passengerState state;
    private char destination;
    private int nBags;
    private arrivalLounge arrivalLounge;


    public passenger(passengerState state) {
        this.state = state;
    }

    @Override
    public void run(){
        for(int i = 0; i < 5;i++){
        char c  =  arrivalLounge.whatShouldIDo(i);
        switch(c){
            case('a'):

            case('b'):

            case('c'):


            }
        }

    }



    public void whatShouldIDo() {
		state = passengerState.AT_THE_DISEMBARKING_ZONE;

	}

    public void setState(passengerState state) {
        this.state = state;
    }

    public arrivalLounge getArrivalLounge() {
        return this.arrivalLounge;
    }

    public void setArrivalLounge(arrivalLounge arrivalLounge) {
        this.arrivalLounge = arrivalLounge;
    }

    public passenger state(passengerState state) {
        this.state = state;
        return this;
    }

    public passenger arrivalLounge(arrivalLounge arrivalLounge) {
        this.arrivalLounge = arrivalLounge;
        return this;
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
            ", arrivalLounge='" + getArrivalLounge() + "'" +
            "}";
    }


}