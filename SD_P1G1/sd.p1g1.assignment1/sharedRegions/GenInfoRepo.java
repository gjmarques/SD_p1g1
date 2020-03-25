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
    private String q;
    /**
     * Occupation state for seat in the bus (passenger id / - (empty))
     */
    private String s;
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

    // Abbreviations of the porter and driver states, in order
    private  String[] porterStates = {"WPTL", "APLH", "ALCB", "ASTR"};
    private  String[] bDriverStates = {"PKAT", "DRFW", "PKDT", "DRBW"};
    private  String[] passengerStates = {"WSD", "LCP", "EAT", "ATT", "TRT", "DTT", "EDT"};


    public GenInfoRepo( File logger) {
        this.loggerF = logger;
        
         String title = "               AIRPORT RHAPSODY - Description of the internal state of the problem";
         String subtitle = "PLANE    PORTER                  DRIVER";
         String subtitle2 = "FN BN  Stat CB SR   Stat  Q1 Q2 Q3 Q4 Q5 Q6  S1 S2 S3";
         String subtitle3 = "                                                         PASSENGERS";
         String subtitle4 = "St1 Si1 NR1 NA1 St2 Si2 NR2 NA2 St3 Si3 NR3 NA3 St4 Si4 NR4 NA4 St5 Si5 NR5 NA5 St6 Si6 NR6 NA6";

        initializeLogger("DELETE LATER: Don't forget  report!");

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
     * Update state of the passenger
     */
    public void passengerState(int passengerID, PassengerState passengerState,  boolean Dest,  int nr_bags){
        if(Dest) this.passengerDest[passengerID] = "FDT";
        else this.passengerDest[passengerID] = "TRT";
        this.nr[passengerID] = nr_bags;
        this.na[passengerID] = 0;
        this.passengerState[passengerID] = passengerState;
        updateStatePassengers();    
    }
    public void passengerState(int nPlane, int passengerID, PassengerState passengerState){
        if(this.passengerState[passengerID] != passengerState){
            this.passengerState[passengerID] = passengerState;
            updateStatePassengers();
        }
    }
    public void passengerState(int passengerID, PassengerState passengerState){
        if(this.passengerState[passengerID] != passengerState){
            this.passengerState[passengerID] = passengerState;
            updateStatePassengers();
        }
    }

    /**
     * State of the porter
     */
    public void porterState( PorterState porterState){
        if(porterState != this.porterState){
            this.porterState = porterState;
            updateStatePorterOrBDriver();
        } 
    }
    /**
     * State of the bus driver
     */
    public void busDriverState( BusDriverState busDriverState){
        if(busDriverState != this.bDriverState){
            this.bDriverState = busDriverState;
            updateStatePorterOrBDriver();
        } 
    }

    /**
     * Occupation state for the waiting queue (passenger id / - (empty)).
     * <p>
     * It receives the passenger's id that are waiting for entering the bus.
     * @param int
     */
    void busWaitingQueue( int passengerId){
         String tmp = passengerId + " ";
        this.q.concat(tmp);
        updateStatePorterOrBDriver();
    }

    /**
     * Number of pieces of luggage belonging to passengers in transit presently stored at the storeroom
     * @param int
     */
    void bagAtStorage(int nrBagsAtStorage){
        this.sr = nrBagsAtStorage;
        updateStatePorterOrBDriver();
    }

    /**
     * Number of flights arriving to the airpoirt
     * @param int
     */
    void nrFlights( int maxFlights){
        this.fn = maxFlights;
        updateStatePorterOrBDriver();
    }

    /**
     * Number of pieces of luggage presently on the conveyor belt
     * @param int
     */
    void collectionMat( int nrLuggageConvBelt){
        this.cb = nrLuggageConvBelt;
        updateStatePorterOrBDriver();
    }

    void updateStatePassengers(){

        String info2 = "";
        String tmp;
        for(int i = 0; i < Global.NR_PASSENGERS; i++){
            tmp = "";
            if(this.passengerState[i] == null) tmp = " --- ---  -   -";
            else{
                tmp =passengerStates[this.passengerState[i].ordinal()] + " " + this.passengerDest[i] + "  " + this.nr[i] + "   " + this.na[i];
            }
            info2 += " " + tmp;
        }
        writeToLogger(info2);
    }

    void updateStatePorterOrBDriver(){
        if(this.q == null) this.q = " -  -  -  -  -  -";
        if(this.s == null) this.s = " -  -  - ";

         String info1 = " " + this.fn + "  " + this.bn + "  " + porterStates[this.porterState.ordinal()] + "  " + this.cb + "  " + this.sr  + "   " + bDriverStates[this.bDriverState.ordinal()] + "  " 
                    + this.q + "  " + this.s;

        writeToLogger(info1);
    }


    /*void checkInternalState(){
        // Occupation state for the waiting queue (passenger id / - (empty))
        String qOccupation = "";
        for (int i = 1; i < 6; i++){
            qOccupation.concat(" -");
        }

        // Occupation state for the waiting queue (passenger id / - (empty))
        String sOccupation = "";
        for (int i = 1; i < 6; i++){
            sOccupation = sOccupation.concat(" -");
        }

        String info1 = " " + this.fn + " " + this.bn + "  " + this.porterStat + " " + this.cb + " " + this.sr  + "  " + this.bDriverStat + "  " 
                    + qOccupation + "  " + sOccupation;

        // for all passengers
        String info2 = "";
        int passengersCount = 0;
        for(int i = 0; i < this.passengers.length; i++){
            passengersCount +=1;
            passengerState = passengers[i].getPassengerState();
            if(passengers[i].getSituation()){
                si = "FDT";
            }else{
                si = "TRT";
            }
            nr = passengers[i].getNumBags();
            na = passengers[i].getCollectedBags();
            info2.concat(passengerState + " " + si + " " + nr + " " + na + " ");
        }
        String tmp = "--- --- -  -";
        while(passengersCount < 6){
            info2.concat(tmp);
            passengersCount += 1;
        }
    }*/

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