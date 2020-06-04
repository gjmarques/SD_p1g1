package serverSide;

import java.util.*;

import clientSide.*;

/**
 * This datatype implements the Temporary Storage Area shared memory region. In this
 * shared region, the {@link Porter} stores the {@link Passenger}s' {@link Bag}s.
 */
public class TempStorageArea {

    /**
     * This stack keeps record of all the {@link Bag}s in the Temporary Storage Area.
     */
    private Stack<Bag> tempStorageBags = new Stack<>();
    /**
     * General Information Repository ({@link GenInfoRepoStub}).
     */
    private GenInfoRepoStub rep;

    /**
     * Instantiates Temporary Storage Area.
     * @param rep General Information Repository.
     */
    public TempStorageArea(GenInfoRepoStub rep){
        this.rep = rep;
    }

    /** 
	 * Adds a bag to the mat in the collection point.
	 * @param bag {@link entities.Bag}
     */
	public void carryItToAppropriateStore(Bag bag) {
        rep.porterState(PorterState.AT_THE_STOREOOM);

        tempStorageBags.add(bag);

        rep.lessBagsOnPlanesHold(bag);
        rep.bagAtStoreRoom(bag);
	}
}