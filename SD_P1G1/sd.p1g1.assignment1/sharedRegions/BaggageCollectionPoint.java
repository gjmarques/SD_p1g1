package sharedRegions;

import entities.*;
import java.util.Queue;
import java.util.Stack;

public class BaggageCollectionPoint {

	private Stack<Bag> collectionMat = new Stack<>();

	
	/** 
	 * Removes a bag from the collection point mat.
	 * @return boolean
	 */
	public boolean goCollectABag() {
		return false;
	}

	
	/** 
	 * Adds a bag to the mat in the collection point.
	 * @param bag
	 */
	public void CarryItToAppropriateStore(Bag bag) {
        collectionMat.add(bag);
	}




/*  PORTER 
    void carryItToAppropriateStore(bag)
 */

/*  PASSENGER 
    void goCollectABag(bag)
 */

}