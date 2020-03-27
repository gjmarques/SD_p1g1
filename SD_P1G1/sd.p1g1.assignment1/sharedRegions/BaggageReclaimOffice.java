package sharedRegions;

import entities.*;

public class BaggageReclaimOffice {

	private GenInfoRepo rep;

	public BaggageReclaimOffice(GenInfoRepo rep){
		this.rep = rep;
	}

	/** 
	 * @param i
	 */
	public void reportMissingBags(int i, int passengerID) {
		rep.passengerState(passengerID, PassengerState.AT_THE_BAGGAGE_RECLAIM_OFFICE);
		rep.missingBags(i, passengerID);
	}

}