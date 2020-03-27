package sharedRegions;

import java.util.*;

import entities.*;

public class TempStorageArea {

    private Stack<Bag> tempStorageBags = new Stack<>();
    private GenInfoRepo rep;

    public TempStorageArea(GenInfoRepo rep){
        this.rep = rep;
    }

	
    /** 
	 * Adds a bag to the mat in the collection point.
	 * @param bag
	 */
	public void carryItToAppropriateStore(Bag bag) {
        rep.porterState(PorterState.AT_THE_STOREOOM);

        tempStorageBags.add(bag);

        rep.lessBagsOnPlanesHold(bag);
        rep.bagAtStoreRoom(bag);
	}


}