package entities;

import sharedRegions.*;

public class Porter extends Thread {

    Bag bag;
    Boolean planeHoldEmpty;

    int id;
    PorterState state;

    
    public Porter (int id, PorterState state){
        this.id = id;
        this.state = state;
    }

    @Override
    public void run(){
        // while not end of the day
        while(arrivalLounge.takeARest() != 'E'){ 
            planeHoldEmpty = false;

	        while(!planeHoldEmpty){
                bag = arrivalLounge.tryToCollectBag();
                // when there is no bags left to peek
                if(bag == null){			
                    planeHoldEmpty = true;
                }
                // if bag is in trasit
                else if(bag.getDestination() == 'T'){ 
                    tempStorageArea.CarryItToAppropriateStore(bag);
                }
                else{					
                    // bag is at final aeroport
                    baggageCollectionPoint.CarryItToAppropriateStore(bag);
                }

                arrivalLounge.noMoreBagsToCollect();		
	        }
        }
    }

    int getID(){
        return this.id;
    }

    void setState(PorterState state){
        this.state = state;
    }

    PorterState getPorterState(){
        return this.state;
    }


}