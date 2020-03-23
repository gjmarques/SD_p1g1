package sharedRegions;

import java.util.*;
import java.util.concurrent.locks.*;
import entities.*;

public class BaggageCollectionPoint {

	private final ReentrantLock rl;
	private final Condition waitBag;
	private boolean noMoreBags = false;
	private List<Bag> collectionMat = new ArrayList<>();

	public BaggageCollectionPoint() {
		rl = new ReentrantLock(true);
		waitBag = rl.newCondition();
	}

	/**
	 * Removes a bag from the collection point mat.
	 * 
	 * @return char
	 */
	public char goCollectABag(int id) {
		rl.lock();
		try {

			//TEMPORARY CONDITION TO PREVENT DEADLOCK (STILL OCCURS SOME TIMES), SHOULD FIND BETTER SOLUTION
			if(!noMoreBags)
				waitBag.await();

			for (Bag bag : collectionMat) {
				if (bag.getID() == id) {
					collectionMat.remove(bag);
					return 'S';
				}
			}

			if (noMoreBags && collectionMat.isEmpty())
				return 'E';

			return 'F';
		} catch (Exception ex) {
			return 'F';
		} finally {
			rl.unlock();
		}
	}

	/**
	 * Adds a bag to the mat in the collection point.
	 * 
	 * @param bag
	 */
	public void CarryItToAppropriateStore(Bag bag) {
		rl.lock();
		try {
				noMoreBags = false;
				collectionMat.add(bag);
				waitBag.signalAll();
		} catch (Exception ex) {
		} finally {
			rl.unlock();
		}
	}

		

	public void noMoreBagsToCollect() {
        rl.lock();
        try{
			noMoreBags = true;
			waitBag.signalAll();
            System.out.println("INFO: noMoreBagsToCollect()");
        }catch(Exception e){
            System.out.println("ERROR: noMoreBagsToCollect()");
        }finally{
            rl.unlock();
        }
	}
}