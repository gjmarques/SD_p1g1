package sharedRegions;

import entities.*;
import mainProgram.Global;

import java.util.*;
import java.util.concurrent.locks.*;

/**
 * This datatype implements the ArrivalLounge shared memory region. In this
 * shared region, the Passengers decide on their course of action and the Porter
 * ..........
 */
public class ArrivalLounge {

    protected final ReentrantLock rl;
    protected final Condition planeHoldEmptyCV;
    private int flightCount;
    private int maxFlights;
    private int numPassengers;
    private int passengerCount = 0;
    private Stack<Bag> bags = new Stack<>();
    private GenInfoRepo rep;

    // Create lock and conditions
    public ArrivalLounge(GenInfoRepo rep) {
        rl = new ReentrantLock(true);
        planeHoldEmptyCV = rl.newCondition();
        this.numPassengers = Global.NR_PASSENGERS;
        this.maxFlights = Global.NR_FLIGHTS;

        this.rep = rep;

        //rep.nrFlights(this.maxFlights);

    }

    // PASSENGER

    public void setFlight(int nFlight) {
        flightCount = nFlight + 1;
    }

    // Passengers decide what to do based on their final destination and number of
    // bags
    public char whatShouldIDo(int nr_flight, int passengerID, Bag[] bags, boolean finalDestination) {
        rl.lock();
        
        try {    
            //rep.nrFlight(nr_flight);        
            rep.passengerState(nr_flight, passengerID, PassengerState.AT_THE_DISEMBARKING_ZONE, finalDestination, bags.length);
            
            passengerCount++;
            if (passengerCount == numPassengers) {
                passengerCount = 0;
                planeHoldEmptyCV.signal();
            }

            if(bags.length == 0 && finalDestination) return 'a';

            for (int b = 0; b < bags.length; b++) {
                this.bags.push(bags[b]);
            }
            rep.nrBagsPlanesHold(this.bags.size());

            return finalDestination ? 'c' : 'b';
        } catch (NullPointerException ex) {
            System.out.println("ERROR: whatShouldIDo");
            return 'a';
        } finally {
            rl.unlock();
        }
    }

    /**
     * This method checks if the porter stays resting or not.
     * <p>
     * Returns true in case of rest; false in case of wake up.
     * 
     * @return boolean
     */
    public char takeARest() {
        rl.lock();
        try {
            
            if (bags.empty() && flightCount == maxFlights)
                return 'E';
            
            rep.porterState(PorterState.WAITING_FOR_A_PLANE_TO_LAND);
            planeHoldEmptyCV.await();

            if (bags.empty() && flightCount == maxFlights)
                return 'E';

            return 'W';

        } catch (Exception ex) {
            return 'E';
        } finally {
            rl.unlock();
        }
    }

    /**
     * This method returns a Bag.
     * <p>
     * Returns null if there is not any bags to pick.
     * 
     * @return Bag
     */
    public Bag tryToCollectBag() {
        rl.lock();
        try {
            if (bags.empty())
                return null;
            else{
                rep.porterState(PorterState.AT_THE_PLANES_HOLD);
                return bags.pop();
            }

        } catch (Exception e) {
            System.out.println("ERROR: tryToCollectBag()");
            return null;
        } finally {
            rl.unlock();
        }
    }
}