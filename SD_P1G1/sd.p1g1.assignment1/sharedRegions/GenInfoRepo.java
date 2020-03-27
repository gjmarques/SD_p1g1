package sharedRegions;

import java.io.*;

import entities.*;
import mainProgram.*;

public class GenInfoRepo {
    

    private  File loggerF;
    private Porter porter;
    private BusDriver bDriver;
    private Passenger[] passengers;

    /**
     * Flight number
     */
    private int fn;
    /**
     * Number of pieces of luggage presently at the plane's hold
     */
    private int bn;
    /**
     * State of the porter
     */
    private PorterState porterState;
    /**
     * Number of pieces of luggage presently on the conveyor belt
     */
    private int cb;
    /**
     * Number of pieces of luggage belonging to passengers in transit presently stored at the storeroom
     */
    private int sr;
    /**
     * State of the driver
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
     * Situation of passenger # (# - 0 .. 5) – TRT (in transit) / FDT (has this airport as her  destination)
     */
    private String si;
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
     * Counter for total of number of bags lost.
     */
    private int missingBags = 1;

    // Abbreviations of the porter and driver states, in order
    private  String[] porterStates = {"WPTL", "APLH", "ALCB", "ASTR"};
    private  String[] bDriverStates = {"PKAT", "DRFW", "PKDT", "DRBW"};
    private  String[] passengerStates = {"WSD", "LCP", "BRO", "EAT", "ATT", "TRT", "DTT", "EDT"};

    public GenInfoRepo( File logger) {
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

        //this.passengerDest[0] = "hello";
        // this.nr[0] = 1;
        // this.na[0] = 1;

    }

    /**
     * Report missing bags.
     */
    public synchronized void missingBags(int nrBags, int passengerID){
        this.missingBags +=1;
    }

    /**
     * Number of pieces of luggage presently at the plane's hold.
     */
    public synchronized void nrBagsPlanesHold(int nrOfBags){
        this.bn += nrOfBags;
        updateStatePorterOrBDriver();
    }
   
    /**
     * Update state of the passenger
     * @param passengerID
     * @param passengerState
     * @param Dest
     * @param nr_bags
     */
    public synchronized void passengerState(int flight_nr, int passengerID, PassengerState passengerState,  boolean Dest,  int nr_bags){
        if(Dest) this.passengerDest[passengerID] = "FDT";
        else this.passengerDest[passengerID] = "TRT";
        this.nr[passengerID] = nr_bags;
        this.na[passengerID] = 0;
        this.passengerState[passengerID] = passengerState;
        this.fn = flight_nr;
        updateStatePorterOrBDriver();    
    }
    public synchronized void passengerState(int nPlane, int passengerID, PassengerState passengerState){
        if(this.passengerState[passengerID] != passengerState){
            this.passengerState[passengerID] = passengerState;
            updateStatePorterOrBDriver();
        }
    }
    public synchronized void passengerState(int passengerID, PassengerState passengerState){
        if(this.passengerState[passengerID] != passengerState){
            this.passengerState[passengerID] = passengerState;
            updateStatePorterOrBDriver();
        }
    }

    /**
     * State of the porter
     */
    public synchronized void porterState( PorterState porterState){
        if(porterState != this.porterState){
            this.porterState = porterState;
            updateStatePorterOrBDriver();
        } 
    }
    /**
     * State of the bus driver
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
     * It receives the passenger's id that are waiting for entering the bus.
     * @param int
     */
    synchronized void busSitting( int passengerID){
        //String tmp = passengerID + " ";

        // passenger is not waiting anymore
        for(int i = 0; i < this.q.length; i++){
            if(this.q[i].equals(Integer.toString(passengerID)) ){
                this.q[i] = "-";
                break;
            }
            System.out.println("this.q: " + this.q[i] + "this to delete!: " + passengerID);
        }
        // passenger is sitting in the bus
        for(int i = 0; i < this.s.length; i++){
            if(this.s[i] == "-" ){
                this.s[i] = Integer.toString(passengerID);
                break;
            } 
        }
        //System.out.println("THIS.q: " + this.q);
        updateStatePorterOrBDriver();
    }

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
     * It receives the passenger's id that are waiting for entering the bus.
     * @param int
     */
    synchronized void busWaitingLine(int passengerId){
        for(int i = 0; i < this.q.length; i++){
            if(this.q[i] == "-" ){
                this.q[i] = Integer.toString(passengerId);
                break;
            } 
        }
        //System.out.println("THIS.q: " + this.q);
        updateStatePorterOrBDriver();
    }

    /**
     * Number of pieces of luggage belonging to passengers in transit presently stored at the storeroom
     * @param int
     */
    synchronized void bagAtStoreRoom(int nrBagsAtStoreRoom){
        this.sr += nrBagsAtStoreRoom;
        this.bn +=1 ;
        updateStatePorterOrBDriver();
    }

    /**
     * Number of flights arriving to the airpoirt
     * @param int
     */
    synchronized void nrFlights( int maxFlights){
        this.fn = maxFlights;
        updateStatePorterOrBDriver();
    }

    /**
     * Number of pieces of luggage presently on the conveyor belt
     * @param int
     */
    synchronized void collectionMatConveyorBelt( int nrLuggageConvBelt){
        this.cb += nrLuggageConvBelt;
        this.bn += nrLuggageConvBelt;
        updateStatePorterOrBDriver();
    }
    

    synchronized void passengerCollectedBags(Bag bag){
        int bag_passengers_id = bag.getID();
        this.na[bag_passengers_id] += 1;
        updateStatePorterOrBDriver();
    }
    // private void updateStatePassengers(){

    //     String info2 = "";
    //     String tmp;
    //     for(int i = 0; i < Global.NR_PASSENGERS; i++){
    //         tmp = "";
    //         if(this.passengerState[i] == null) tmp = "--- ---  -   - ";
    //         else{
    //             // System.out.println("PASSENGER state update to: " + this.passengerState[i] );
    //             // System.out.println("I'm updating state to: " + passengerStates[this.passengerState[i].ordinal()] );
    //             //getAbrv();
    //             tmp = passengerStates[this.passengerState[i].ordinal()] + " " + this.passengerDest[i] + "  " + this.nr[i] + "   " + this.na[i];
    //         }
    //         info2 += tmp + " ";
    //     }
    //     writeToLogger(info2);
    // }

    private void updateStatePorterOrBDriver(){
        //if(this.q == null) this.q = " -  -  -  -  -  -";
        //if(this.s == null) this.s = " -  -  - ";


        String info1 = " " + this.fn + "  " + this.bn + "  " + porterStates[this.porterState.ordinal()] + "  " + this.cb + "  " + this.sr  + "   " 
                           + bDriverStates[this.bDriverState.ordinal()] + "  " 
                           + this.q[0] + "  " + this.q[1] + "  " + this.q[2] + "  " + this.q[3] + "  " + this.q[4] + "  " + this.q[5] + "  " 
                           + this.s[0] + "  " + this.s[1] + "  " + this.s[2];

        String info2 = "";
        String tmp;
        for(int i = 0; i < Global.NR_PASSENGERS; i++){
            tmp = "";
            if(this.passengerState[i] == null) tmp = "--- ---  -   - ";
            else{
                // System.out.println("PASSENGER state update to: " + this.passengerState[i] );
                // System.out.println("I'm updating state to: " + passengerStates[this.passengerState[i].ordinal()] );
                //getAbrv();
                tmp = passengerStates[this.passengerState[i].ordinal()] + " " + this.passengerDest[i] + "  " + this.nr[i] + "   " + this.na[i];
            }
            info2 += tmp + " ";
        }

        writeToLogger(info1);
        writeToLogger(info2);
        

    }

    public synchronized void finalReport(){
        writeToLogger("");
        writeToLogger("Final report");
        for(int i = 0; i < Global.NR_PASSENGERS; i++){
            if(this.passengerDest[i] == "FDT") final_dest_passengers +=1;
        }
        System.out.println("this.bn = " + (this.bn));
        System.out.println("this.missingbags = " + (this.missingBags));
        writeToLogger("N. of passengers which have this airport as their final destination = " + final_dest_passengers);
        writeToLogger("N. of passengers which are in transit = " + (Global.NR_PASSENGERS - final_dest_passengers));
        writeToLogger("N. of bags that should have been transported in the planes hold = " + this.bn);
        writeToLogger("N. of bags that were lost = " + this.missingBags);
    }



    void writeToLogger( String toWrite){
        assert toWrite != null : "ERROR: nothing to update!";

        try {
            FileWriter myWriter = new FileWriter(this.loggerF, true);
            myWriter.write(toWrite + '\n');
            myWriter.close();
        } catch ( IOException e) {
            System.out.println("ERROR: An error occurred writting to logger.");
            e.printStackTrace();
        } 

    }


    void initializeLogger( String toWrite){
        assert toWrite != null : "ERROR: nothing to update!";

        try {
             FileWriter myWriter = new FileWriter(this.loggerF, true);
            myWriter.write(toWrite + '\n');
            myWriter.close();
        } catch ( IOException e) {
            System.out.println("ERROR: An error occurred writting to logger.");
            e.printStackTrace();
        } 

    }
 }