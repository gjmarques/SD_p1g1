package sharedRegions;

import java.util.*;
import java.util.concurrent.locks.*;
import entities.*;

public class BaggageCollectionPoint {

	private final ReentrantLock rl;
	private final Condition waitBag;
	private boolean noMoreBags = false;
	private List<Bag> collectionMat = new ArrayList<>();
	private GenInfoRepo rep;

	public BaggageCollectionPoint(GenInfoRepo rep) {
		rl = new ReentrantLock(true);
		waitBag = rl.newCondition();
		this.rep = rep;
	}

	/**
	 * Removes a bag from the collection point mat.
	 * 
	 * @return char
	 */
	public char goCollectABag(int passengerID) {
		rl.lock();
		try {
			rep.passengerState(passengerID, PassengerState.AT_THE_LUGGAGE_COLLECTION_POINT);

			// TEMPORARY CONDITION TO PREVENT DEADLOCK (STILL OCCURS SOME TIMES), SHOULD
			// FIND BETTER SOLUTION
			System.out.println(
					"NOMOREBAGS-" + noMoreBags + "  CollectionMat-" + collectionMat.size() + collectionMat.isEmpty());

			if (noMoreBags && collectionMat.isEmpty())
				//bag is missing; there's no bags in collection mat
				return 'E';

			for (Bag bag : collectionMat) {
				if (bag.getID() == passengerID) {
					collectionMat.remove(bag);
					rep.collectionMatConveyorBelt(collectionMat.size());
					rep.passengerCollectedBags(bag);
					// bag collected
					return 'S';
				}
			}

			waitBag.await();

			if (noMoreBags && collectionMat.isEmpty())
				//bag is missing; there's no bags in collection mat
				return 'E';

			return 'F';
		} catch (Exception ex) {
			System.out.println("ERROR: BaggageCollectionPoint.goCollectABag");
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
	public void carryItToAppropriateStore(Bag bag) {
		rl.lock();
		try {
			rep.porterState(PorterState.AT_THE_LUGGAGE_BELT_CONVEYOR);
			Random r = new Random();
			int answer = r.nextInt(10);
			//10% chance of losing a bag
			if (answer < 9) {
				collectionMat.add(bag);
				noMoreBags = false;
				rep.lessBagsOnPlanesHold(bag);
				rep.collectionMatConveyorBelt(collectionMat.size());
				waitBag.signalAll();
			}

		} catch (Exception ex) {
			System.out.println("ERROR: BaggageCollectionPoint.carryItToAppropriateStore");
		} finally {
			rl.unlock();
		}
	}

	public void noMoreBagsToCollect() {
		rl.lock();
		try {
			noMoreBags = true;
			waitBag.signalAll();
			System.out.println("INFO: noMoreBagsToCollect()");
		} catch (Exception e) {
			System.out.println("ERROR: noMoreBagsToCollect()");
		} finally {
			rl.unlock();
		}
	}
}