package entities;

import sharedRegions.*;

public class Porter extends Thread {

    Bag bag;
    Boolean planeHoldEmpty;

    int id;
    PorterState state;

    private final ArrivalLounge arrivalLounge;
    private final TempStorageArea tempStorageArea;
    private final BaggageCollectionPoint baggageCollectionPoint;
    //private final ExitAirport exitAirport;

    
    public Porter (int id, PorterState state, ArrivalLounge arrivalLounge, TempStorageArea tempStorageArea, BaggageCollectionPoint baggageCollectionPoint){
        this.id = id;
        this.state = state;
        this.arrivalLounge = arrivalLounge;
        this.tempStorageArea = tempStorageArea;
        this.baggageCollectionPoint = baggageCollectionPoint;
    }

    /**
     * This method defines the life-cycle of the Porter.
     */
    @Override
    public void run(){
        // while not end of the day ?
        while(!arrivalLounge.takeARest()){ 
            planeHoldEmpty = false;
            this.state = PorterState.AT_THE_PLANES_HOLD;
            System.out.println("AM I AWAKE ");

	        while(!planeHoldEmpty){
                bag = arrivalLounge.tryToCollectBag();
                // when there is no bags left to peek
                if(bag == null){			
                    planeHoldEmpty = true;
                }
                // if bag is in trasit
                else if(bag.getDestination() == 'T'){ 
                    setState(PorterState.AT_THE_STOREOOM);
                    tempStorageArea.CarryItToAppropriateStore(bag);
                }
                else{					
                    // bag is at final aeroport
                    setState(PorterState.AT_THE_LUGGAGE_BELT_CONVEYOR);
                    baggageCollectionPoint.CarryItToAppropriateStore(bag);
                }

                arrivalLounge.noMoreBagsToCollect();
                setState(PorterState.AT_THE_PLANES_HOLD);
	        }
        }
    }

    
    /** 
     * @return int
     */
    int getID(){
        return this.id;
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
    PorterState getPorterState(){
        return this.state;
    }


}