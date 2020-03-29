package sharedRegions;

import java.util.*;
import java.util.concurrent.locks.*;
import entities.*;

/**
 * This datatype implements the Baggage Colelction Point shared memory region. In this
 * shared region, the Porter stores the passengers' bags. The passengers come here to colelct their bags.
 */
public class BaggageCollectionPoint {

	private final ReentrantLock rl;
	private final Condition waitBag;
	private final Condition noMoreBagsToCollect;
	private boolean noMoreBags = false;
	private int entered = 0;
	private int exited = 0;
	//private List<Bag> collectionMat = new ArrayList<>();
	private HashMap<Integer, List<Bag>> collectionMat = new HashMap<>();
	private GenInfoRepo rep;

	public BaggageCollectionPoint(int numPassengers, GenInfoRepo rep) {
		rl = new ReentrantLock(true);
		waitBag = rl.newCondition();
		noMoreBagsToCollect = rl.newCondition();
		this.rep = rep;
		for(int i=0; i<numPassengers; i++){
			collectionMat.put(i, new ArrayList<Bag>());
		}
	}

	/**
	 * Removes a bag from the collection point mat.
	 * 
	 * @return char
	 */
	public int goCollectABag(int passengerID) {
		rl.lock();
		try {
			rep.passengerState(passengerID, PassengerState.AT_THE_LUGGAGE_COLLECTION_POINT);
			int  collectedBags = 0;
			entered++;
			while(!noMoreBags){
				waitBag.await();
				collectedBags = collectionMat.get(passengerID).size();
			}

			
			rep.collectionMatConveyorBelt(collectionMat.values().stream().mapToInt(List::size).sum());

			collectionMat.get(passengerID).clear();
			exited++;
			if(entered==exited){
				noMoreBags = false;
			}
			return collectedBags;

		} catch (Exception e) {
			System.out.println("Thread: " + Thread.currentThread().getName() + " terminated.");
			System.out.println("Error: " + e.getMessage());
			System.exit(1);
			return 'z';
		} finally {
			rl.unlock();
		}
	}

	/**
	 * Adds a bag to the mat in the collection point.
	 * 
	 * @param bag
	 */
	public void carryItToAppropriateStore(Bag bag) {
		rl.lock();
		try {
			rep.porterState(PorterState.AT_THE_LUGGAGE_BELT_CONVEYOR);
			
			System.out.println("ADDED BAG FROM PASSENGER"+ bag.getID());
			collectionMat.get(bag.getID()).add(bag);
			noMoreBags = false;
			rep.lessBagsOnPlanesHold(bag);
			rep.collectionMatConveyorBelt(collectionMat.values().stream().mapToInt(List::size).sum());
			waitBag.signalAll();

		}catch (Exception e) {
			System.out.println("Thread: " + Thread.currentThread().getName() + " terminated.");
			System.out.println("Error: " + e.getMessage());
			System.exit(1);
		} finally {
			rl.unlock();
		}
	}

	public void noMoreBagsToCollect() {
		rl.lock();
		try {
			noMoreBags = true;
			waitBag.signalAll();
			noMoreBagsToCollect.signalAll();
		} catch (Exception e) {
			System.out.println("Thread: " + Thread.currentThread().getName() + " terminated.");
			System.out.println("Error: " + e.getMessage());
			System.exit(1);
		} finally {
			rl.unlock();
		}
	}
}