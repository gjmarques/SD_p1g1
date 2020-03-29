package entities;

import sharedRegions.*;
import mainProgram.*;

import java.util.*;


/**
 * Entity Porter.
 */
public class Porter extends Thread {

    /**
     * {@link Bag}
     */
    Bag bag;
    /**
     * Defines if {@link Porter} rests
     */
    private boolean rest = true;
    /*
    * Porter state
    * {@link PorterState}
    */
    PorterState state;
    /**
     * Arrival Lounge
     * {@link sharedRegions.ArrivalLounge}
     */
    private final ArrivalLounge arrivalLounge;
    /**
     * Temporary Storage Area
     * {@link sharedRegions.TempStorageArea}
     */
    private final TempStorageArea tempStorageArea;
    /**
     * Baggage Collection Point
     * {@link sharedRegions.BaggageCollectionPoint}
     */
    private final BaggageCollectionPoint baggageCollectionPoint;

    /**
     * Instantiates entity {@link Porter}
     * @param {@link sharedRegions.ArrivalLounge}
     * @param {@link sharedRegions.TempStorageArea}
     * @param {@link sharedRegions.BaggageCollectionPoint}
     */
    public Porter(ArrivalLounge arrivalLounge, TempStorageArea tempStorageArea,
            BaggageCollectionPoint baggageCollectionPoint) {
        this.arrivalLounge = arrivalLounge;
        this.tempStorageArea = tempStorageArea;
        this.baggageCollectionPoint = baggageCollectionPoint;
    }

    /**
     * This method defines the life-cycle of the Porter.
     */
    @Override
    public void run() {
        while (rest) {
            char choice = arrivalLounge.takeARest();
            if (choice == 'W') {

                bag = arrivalLounge.tryToCollectBag();
            
                while (bag != null) {
                    Random r = new Random();
                    int answer = r.nextInt(Global.LOST_BAG_PERCENTAGE);
                    if(answer==9){ System.out.println("LOST BAG FROM PASSENGER"+ bag.getID());}
                    if (answer < 9) {
                        // if bag is in trasit
                        if (bag.getDestination() == 'T') {
                            tempStorageArea.carryItToAppropriateStore(bag);
                        } else {
                            // bag is at final aeroport
                            baggageCollectionPoint.carryItToAppropriateStore(bag);
                        }
                    }
                    bag = arrivalLounge.tryToCollectBag();
                }
                baggageCollectionPoint.noMoreBagsToCollect();
            } else if (choice == 'E') {
                rest = false;
            }
        }
    }

    /**
     * Gets {@link Porter} state
     * @return {@link PorterState}
     */
    public PorterState getPorterState() {
        return this.state;
    }

}