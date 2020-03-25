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
	public void CarryItToAppropriateStore(Bag bag) {
        tempStorageBags.add(bag);
        rep.bagAtStorage(tempStorageBags.size());
        rep.porterState(PorterState.AT_THE_STOREOOM);
	}


}