package serverSide;

import java.io.*;

import global.*;
import clientSide.*;

/**
 * Implementation  General Repository of Information which works solely as the place where the
 * visible internal state of the problem is stored. The visible internal state is defined
 * by the set of variables whose value is printed in the logging file.
 * <p>
 * Whenever an entity ({@link entities.Porter}, {@link entities.Passenger}, {@link entities.BusDriver}) 
 * executes an operation that changes the values of some of these variables, the fact must be reported so that a
 * new line group is printed in the logging file. The report operation must be
 * atomic, that is, when two or more variables are changed, the report operation
 * must be unique so that the new line group reflects all the changes that have taken
 * place.
 */
public class GenInfoRepo {
    
    /**
     * Logging file.
     */
    private  File loggerF;
    /**
     * Flight number.
     */
    private int fn;
    /**
     * Number of pieces of luggage presently at the plane's hold.
     */
    private int[] bn = {0, 1, 2, 3, 4};
    /**
     * State of the {@link entities.Porter}.
     */
    private PorterState porterState;
    /**
     * Number of pieces of luggage presently on the conveyor belt.
     */
    private int cb;
    /**
     * Number of pieces of luggage belonging to passengers in transit presently stored at the storeroom.
     */
    private int sr;
    /**
     * State of the {@link entities.BusDriver}.
     */
    private BusDriverState bDriverState;
    /**
     * Occupation state for the waiting queue (passenger id / - (empty))
     */
    private String[] q = {"-", "-", "-", "-", "-", "-"};
    /**
     * Occupation state for seat in the bus (passenger id / - (empty))
     */
    private String[] s = {"-", "-", "-"};
    /**
     * State of passenger # (# - 0 .. 5)
     */
    private PassengerState[] passengerState = new PassengerState[Global.NR_PASSENGERS];
    /**
     * Number of pieces of luggage the passenger # (# - 0 .. 5) carried at the start of her journey
     */
    private int[] nr = new int[Global.NR_PASSENGERS]; 
    /**
     * Number of pieces of luggage the passenger # (# - 0 .. 5) she has presently collected
     */
    private int[] na = new int[Global.NR_PASSENGERS];
    /**
     * Shows if this airport is  destination of the passenger (TRT - in transit; FDT -  destination);
     */
    private String[] passengerDest = new String[Global.NR_PASSENGERS];
    /**
     * Counter for number of passengers with final destination
     */
    private int final_dest_passengers = 0;
    /**
     * Counter for number of passengers in transit
     */
    private int inTransit_dest_passengers = 0;
    /**
     * Counter for total of number of bags lost.
     */
    private int missingBags = 0;
    /**
     * Total number of bags that passed by the planes'hold.
     */
    private int bn_total;
    /**
     * Abbreviations of the {@link entities.Porter}'s' states, rdered by the {@link entities.PorterState}.
     */
    private  String[] porterStates = {"WPTL", "APLH", "ALCB", "ASTR"};
    /**
     * Abbreviations of the {@link entities.BusDriver}'s' states. Ordered by the {@link entities.BusDriverState}.
     */
    private  String[] bDriverStates = {"PKAT", "DRFW", "PKDT", "DRBW"};
    /**
     * Abbreviations of the {@link entities.Passenger}s' states. Ordered by the {@link entities.PassengerState}
     */
    private  String[] passengerStates = {"WSD", "LCP", "BRO", "EAT", "ATT", "TRT", "DTT", "EDT"};

    /**
     * General Repository of Information.
     * @param logger
     */
    public GenInfoRepo(File logger) {
        this.loggerF = logger;
        
         String title = "               AIRPORT RHAPSODY - Description of the internal state of the problem";
         String subtitle = "PLANE    PORTER                  DRIVER";
         String subtitle2 = "FN BN  Stat CB SR   Stat  Q1 Q2 Q3 Q4 Q5 Q6  S1 S2 S3";
         String subtitle3 = "                                                         PASSENGERS";
         String subtitle4 = "St1 Si1 NR1 NA1 St2 Si2 NR2 NA2 St3 Si3 NR3 NA3 St4 Si4 NR4 NA4 St5 Si5 NR5 NA5 St6 Si6 NR6 NA6";


        initializeLogger(title);
        initializeLogger("");
        initializeLogger(subtitle);
        initializeLogger(subtitle2);
        initializeLogger(subtitle3);
        initializeLogger(subtitle4);


        this.porterState = PorterState.WAITING_FOR_A_PLANE_TO_LAND;
        this.bDriverState = BusDriverState.PARKING_AT_THE_ARRIVAL_TERMINAL;

    }

   
    /**
     * Number of pieces of luggage presently at the plane's hold.
     * @param bagsPerFlight List of number of bags per flight.
     */
    public void nrBagsPlanesHold(int[] bagsPerFlight){
        for(int i = 0; i< bagsPerFlight.length;i++){
            this.bn[i] = bagsPerFlight[i];
            System.out.println("GIR update bn: " + bn[i]);
            this.bn_total += this.bn[i];
        }
    }

    /**
     * Porter taking bags out of planes' hold.
     * @param bag {@link entities.Bag}
     */
    public synchronized void lessBagsOnPlanesHold(Bag bag){
        this.bn[bag.getflightNR()]-= 1;
        updateStatePorterOrBDriver();
    }

    /**
     * Report missing bags.
     * @param nrBags Number of {@link entities.Bag}.
     * @param passengerID {@link entities.Passenger} that lost a {@link entities.Bag} identification.
     */
    public synchronized void missingBags(int nrBags, int passengerID){
        this.missingBags +=1;
    }

    /**
     * Update state of the passenger
     * @param passengerID {@link entities.Passenger}'s identitifation.
     * @param passengerState {@link entities.PassengerState}.
     * @param Dest {@link entities.Passenger}'s detination.
     * @param nr_bags Number of {@link entities.Bag}s that the {@link entities.Passenger} brings.
     */
    public synchronized void passengerState(int flight_nr, int passengerID, PassengerState passengerState,  boolean Dest,  int nr_bags){
        if(Dest){
            this.passengerDest[passengerID] = "FDT";
        } else 
            this.passengerDest[passengerID] = "TRT";
        this.nr[passengerID] = nr_bags;
        this.na[passengerID] = 0;
        this.passengerState[passengerID] = passengerState;
        this.fn = flight_nr;
        updateStatePorterOrBDriver();    
    }
    
    /** 
     * Initializes {@link entities.Passenger}s to {@code null}, to fix logger at the beggining of each flight.
     * @param flight_nr Flight number.
     * @param passengerID {@link entities.Passenger}'s identification.
     */
    public synchronized void initPassenger(int flight_nr, int passengerID){
        if(flight_nr != this.fn){
            this.passengerState[passengerID] = null;
        }
    }

    /** 
     * This method is responsible for the counting of {@link entities.Passenger}s' destinations. It increments
     * the respective value for {@link entities.Passenger}s in transit or with final destination.
     * @param dest If {@code true}, then this airport is the final destination of the respective {@link entities.Passenger}. False, otherwise.
     */
    public synchronized void countDest(boolean dest){
        if(dest){
            this.final_dest_passengers++;  
        } else 
            this.inTransit_dest_passengers++;
    }
    
    /** 
     * This method updates a {@link entities.Passenger}s' state if it changes.
     * @param nPlane {@link entities.Passenger}'s flight number.
     * @param passengerID {@link entities.Passenger}'s identification.
     * @param passengerState {@link entities.Passenger}'s satate.
     */
    public synchronized void passengerState(int nPlane, int passengerID, PassengerState passengerState){
        if(this.passengerState[passengerID] != passengerState){
            this.passengerState[passengerID] = passengerState;
            updateStatePorterOrBDriver();
        }
    }
    
    /** 
     * This method updates a {@link entities.Passenger}s' state if it changes.
     * @param passengerID {@link entities.Passenger}'s identification.
     * @param passengerState {@link entities.Passenger}'s satate.
     */
    public synchronized void passengerState(int passengerID, PassengerState passengerState){
        if(this.passengerState[passengerID] != passengerState){
            this.passengerState[passengerID] = passengerState;
            updateStatePorterOrBDriver();
        }
    }

    /**
     * This method updates a {@link entities.Porter}s' state if it changes.
     * @param porterState {@link entities.Passenger}'s satate.
     */
    public synchronized void porterState(PorterState porterState){
        if(porterState != this.porterState){
            this.porterState = porterState;
            updateStatePorterOrBDriver();
        } 
    }
    /**
     * This method updates a {@link entities.BusDriver}s' state if it changes.
     * @param busDriverState {@link entities.BusDriver}'s satate.
     */
    public synchronized void busDriverState( BusDriverState busDriverState){
        if(busDriverState != this.bDriverState){
            this.bDriverState = busDriverState;
            updateStatePorterOrBDriver();
        } 
    }

    /**
     * Occupation state for the sitting queue (passenger id / - (empty)).
     * <p>
     * It receives the {@link entities.Passenger}s' idetification that are waiting for entering the bus.
     * @param passengerID {@link entities.Passenger}s' idetification.
     */
    synchronized void busSitting( int passengerID){
        // passenger is not waiting anymore
        for(int i = 0; i < this.q.length; i++){
            if(this.q[i].equals(Integer.toString(passengerID)) ){
                this.q[i] = "-";
                break;
            }
        }
        // passenger is sitting in the bus
        for(int i = 0; i < this.s.length; i++){
            if(this.s[i] == "-" ){
                this.s[i] = Integer.toString(passengerID);
                break;
            } 
        }
        updateStatePorterOrBDriver();
    }

    
    /** 
     * {@link entities.Passenger} leaves the bus.
     * @param passengerID {@link entities.Passenger}s' idetification.
     */
    synchronized void leaveBus(int passengerID){
        for(int i = 0; i < this.s.length; i++){
            if(this.s[i].equals(Integer.toString(passengerID))){
                this.s[i] = "-";
                break;
            } 
        }
        updateStatePorterOrBDriver();
    }

    /**
     * Occupation state for the waiting queue (passenger id / - (empty)).
     * <p>
     * It receives the {@link entities.Passenger}s' idetification that are waiting for entering the bus.
     * @param passengerId {@link entities.Passenger}s' idetification.
     */
    synchronized void busWaitingLine(int passengerId){
        for(int i = 0; i < this.q.length; i++){
            if(this.q[i] == "-" ){
                this.q[i] = Integer.toString(passengerId);
                break;
            } 
        }
        updateStatePorterOrBDriver();
    }

    /**
     * Number of pieces of luggage belonging to {@link entities.Passenger}s in transit presently stored at the storeroom.
     * @param Bag
     */
    synchronized void bagAtStoreRoom(Bag bag){
        if(bag.getDestination() == 'T'){
            this.sr += 1;
            updateStatePorterOrBDriver();
        }
    }

    /**
     * Number of flights arriving to this airpoirt.
     * @param int
     */
    synchronized void nrFlights( int maxFlights){
        this.fn = maxFlights;
        updateStatePorterOrBDriver();
    }

    /**
     * Number of pieces of luggage presently on the conveyor belt.
     * @param int
     */
    synchronized void collectionMatConveyorBelt(int nrLuggageConvBelt){
        this.cb = nrLuggageConvBelt;
        updateStatePorterOrBDriver();
    }
    
    /** 
     * Number of bags colelcted by each {@link entities.Passenger}.
     * @param passengerID {@link entities.Passenger}'s identification.
     * @param nBags {@link entities.Passenger}'s number of {@link entities.Bag}s.
     */
    synchronized void passengerCollectedBags(int passengerID, int nBags){
        int bag_passengers_id = passengerID;
        this.na[bag_passengers_id] += nBags;
        updateStatePorterOrBDriver();
    }

    /**
     * This method is responsible for conencting the information reqquire for each group of lines in the logger.
     */
    private void updateStatePorterOrBDriver(){

        String info1 = " " + this.fn + "  " + this.bn[this.fn] + "  " + porterStates[this.porterState.ordinal()] + "  " + this.cb + "  " + this.sr  + "   " 
                           + bDriverStates[this.bDriverState.ordinal()] + "   " 
                           + this.q[0] + "  " + this.q[1] + "  " + this.q[2] + "  " + this.q[3] + "  " + this.q[4] + "  " + this.q[5] + "  " 
                           + this.s[0] + "  " + this.s[1] + "  " + this.s[2];
        String info2 = "";
        String tmp;
        for(int i = 0; i < Global.NR_PASSENGERS; i++){
            tmp = "";
            if(this.passengerState[i] == null) tmp = "--- ---  -   -";
            else{
                tmp = passengerStates[this.passengerState[i].ordinal()] + " " + this.passengerDest[i] + "  " + this.nr[i] + "   " + this.na[i];
            }
            info2 += tmp + "  ";
        }
        writeToLogger(info1);
        writeToLogger(info2);
    }

    /**
     * Puts together the final report to write in the logger file.
     */
    public synchronized void finalReport(){
        writeToLogger("");
        writeToLogger("Final report");
   
        writeToLogger("N. of passengers which have this airport as their final destination = " + final_dest_passengers);
        writeToLogger("N. of passengers which are in transit = " + inTransit_dest_passengers);
        writeToLogger("N. of bags that should have been transported in the planes hold = " + this.bn_total);
        writeToLogger("N. of bags that were lost = " + this.missingBags);
    }

    /** 
     * This method is responsible for writing information in the logger file while the simulation is running, and, then
     * the final report.
     * <p>
     * First it opens the file, then writes into it in the end of the file, finally it closes the file.
     * @param toWrite String to write in the file with the information updated.
     */
    void writeToLogger( String toWrite){
        assert toWrite != null : "ERROR: nothing to update!";

        try {
            FileWriter myWriter = new FileWriter(this.loggerF, true);
            myWriter.write(toWrite + '\n');
            myWriter.close();
        } catch ( IOException e) {
            System.out.println("Thread: " + Thread.currentThread().getName() + " terminated.");
			System.out.println("Error: " + e.getMessage());
			System.exit(1);
        } 
    }
    
    /** 
     * This method is responsible for writing the initial information/subtitles in the logger file in the 
     * beggining of the simulation.
     * First it opens the file, then writes into it in the end of the file, finally it closes the file.
     * @param toWrite String to write in the file.
     */
    void initializeLogger( String toWrite){
        assert toWrite != null : "ERROR: nothing to update!";

        try {
             FileWriter myWriter = new FileWriter(this.loggerF, true);
            myWriter.write(toWrite + '\n');
            myWriter.close();
        } catch ( IOException e) {
            System.out.println("Thread: " + Thread.currentThread().getName() + " terminated.");
			System.out.println("Error: " + e.getMessage());
			System.exit(1);
        } 
    }
 }