package sharedRegions;

import java.util.*;

import entities.*;

public class TempStorageArea {

    private Stack<Bag> tempStorageBags = new Stack<>();

    void tempStorageArea(){
        System.out.println("Does nothing");
    }

	
    /** 
	 * Adds a bag to the mat in the collection point.
	 * @param bag
	 */
	public void CarryItToAppropriateStore(Bag bag) {
        tempStorageBags.add(bag);
	}


}