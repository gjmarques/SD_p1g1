package serverSide;

import java.util.*;
import java.util.concurrent.locks.*;
import clientSide.*;

/**
 * This datatype implements the Baggage Colelction Point shared memory region. In this
 * shared region, the Porter stores the passengers' bags. The passengers come here to colelct their bags.
 */
public class BaggageCollectionPoint {

    /**
     * A reentrant mutual exclusion Lock with the same basic behavior and semantics as the implicit monitor lock 
     * accessed using synchronized methods and statements
     */	
	private final ReentrantLock rl;
	
	/**
     * Synchronization point where the {@link entities.Passenger}s wait until the {@link entities.Passenger}
	 * signals that a {@link entities.Bag} has placed in the collection mat.
     */
	private final Condition waitBag;

	/**
     * Variable to signal the {@link entities.Passenger}s that no more bags will be placed in
	 * the collection mat
     */
	private boolean noMoreBags = false;

	/**
     * Internal count of how many {@link entities.Passenger}s are trying to collect bags
	 * to prevent new flight starting before boolean variable noMoreBags has been reset
	 */
	private int entered = 0;

	/**
     * Internal count of how many {@link entities.Passenger}s are done collecting bags
	 * to prevent new flight starting before boolean variable noMoreBags has been reset
     */
	private int exited = 0;

	/**
     * Internal structure where the {@link entities.Porter} places each collected {@link entities.Bag}
	 * in the corresponding {@link entities.Passenger}s list allowing him to retrieve it.
	 */
	private HashMap<Integer, List<Bag>> collectionMat = new HashMap<>();

    /**
     * General Information Repository {@link GenInfoRepo}
     */
	private GenInfoRepoStub rep;

	/**
     * Instantiates BaggageCollectionPoint shared region
	 * @param numPassengers total number of passengers to create individual lists in the collectionMat data structure
     * @param rep {@link GenInfoRepo}.
     */
	public BaggageCollectionPoint(int numPassengers, GenInfoRepoStub rep) {
		rl = new ReentrantLock(true);
		waitBag = rl.newCondition();
		this.rep = rep;
		for(int i=0; i<numPassengers; i++){
			collectionMat.put(i, new ArrayList<Bag>());
		}
	}

	/**
	 * Removes a bag from the collection point mat.
	 * 
	 * @param passengerID
	 * @return int
	 */
	public int goCollectABag(int passengerID) {
		rl.lock();
		try {
			rep.passengerState(passengerID, PassengerState.AT_THE_LUGGAGE_COLLECTION_POINT);
			int  collectedBags = 0;
			entered++;
			while(!noMoreBags){
				waitBag.await();
				if(collectionMat.get(passengerID).size() != 0){
					collectedBags += 1;
					collectionMat.get(passengerID).remove(0);			
					rep.passengerCollectedBags(passengerID, 1);
					rep.collectionMatConveyorBelt(collectionMat.values().stream().mapToInt(List::size).sum());
				}
		
			}
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

	/**
	 * Signals {@link entities.Passenger}s that no more {@link entities.Bag}s
	 * will be placed in the colletionMat
	 *
	 */
	public void noMoreBagsToCollect() {
		rl.lock();
		try {
			noMoreBags = true;
			waitBag.signalAll();
		} catch (Exception e) {
			System.out.println("Thread: " + Thread.currentThread().getName() + " terminated.");
			System.out.println("Error: " + e.getMessage());
			System.exit(1);
		} finally {
			rl.unlock();
		}
	}
}