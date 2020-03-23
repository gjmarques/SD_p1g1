package entities;

import sharedRegions.*;

public class Porter extends Thread {

    Bag bag;
    private boolean loop = true;
    PorterState state;

    private final ArrivalLounge arrivalLounge;
    private final TempStorageArea tempStorageArea;
    private final BaggageCollectionPoint baggageCollectionPoint;
    // private final ExitAirport exitAirport;

    public Porter(ArrivalLounge arrivalLounge, TempStorageArea tempStorageArea,
            BaggageCollectionPoint baggageCollectionPoint) {
        this.state = PorterState.WAITING_FOR_A_PLANE_TO_LAND;
        this.arrivalLounge = arrivalLounge;
        this.tempStorageArea = tempStorageArea;
        this.baggageCollectionPoint = baggageCollectionPoint;
    }

    /**
     * This method defines the life-cycle of the Porter.
     */
    @Override
    public void run() {
        while (loop) {
            char choice = arrivalLounge.takeARest();
            if (choice == 'W') {

                bag = arrivalLounge.tryToCollectBag();
                while (bag != null) {
                    // if bag is in trasit
                    if (bag.getDestination() == 'T') {
                        setState(PorterState.AT_THE_STOREOOM);
                        tempStorageArea.CarryItToAppropriateStore(bag);
                    } else {
                        // bag is at final aeroport
                        setState(PorterState.AT_THE_LUGGAGE_BELT_CONVEYOR);
                        baggageCollectionPoint.CarryItToAppropriateStore(bag);
                    }

                    bag = arrivalLounge.tryToCollectBag();
                }
                baggageCollectionPoint.noMoreBagsToCollect();
                setState(PorterState.AT_THE_PLANES_HOLD);
            } else if (choice == 'E') {
                loop = false;
            }
        }
    }

    /**
     * @param state
     */
    void setState(PorterState state) {
        this.state = state;
    }

    /**
     * @return PorterState
     */
    PorterState getPorterState() {
        return this.state;
    }

}