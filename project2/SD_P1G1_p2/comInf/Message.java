package comInf;

import java.io.*;
import entities.*;

public class Message implements Serializable {

    /**
     *  Chave de serialização
    */
    private static final long serialVersionUID = 1001L;

    /* Tipos das mensagens */
    /**
     *  What should I do request
    */
    public static final int WSID  =  1;
    /**
     * Successful action
     */
    public static final int ACK = 999;

    public static final int GOHOME  =  2;
    public static final int TAKEBUS  =  3;
    public static final int COLLECTBAG  =  4;
    /**
     * Final destination (or not) message
     */
    public static final int DEST = 5;
     /**
     * Initiate passenger (in repository) message
     */
    public static final int INITP = 6;
    /**
     * Updated arrival term. tranfer quay flight count
     */
    public static final int SET_FLIGHT_attq = 7;
    /**
     * Updated arrival lounge flight count
     */
    public static final int SET_FLIGHT_al = 7;
    /**
     * Signal that passenger is going home
     */
    public static final int GOINGHOME = 8;
    /**
     * Signal that passenger is going to take a bus
     */
    public static final int TAKINGBUS = 9;
    /**
     * Signal that passenger is entering a bus
     */
    public static final int ENTERINGBUS = 10;
    /**
     * Signal that passenger is leaving a bus
     */
    public static final int LEAVINGBUS = 11;
    /**
     * Signal that passenger is preparing next leg
     */
    public static final int PNEXTLEG = 12;


    /* Other variables */
    /**
     * What should I do option
     */
    public static final char WSID_ANSWER = 'z';
    /**
     * Number of pieces of luggage presently at the plane's hold.
     */
    public static final int BAGS_PL = -1;
    /**
     * List of bags per flight
     */
    public static final int[] bagsPerFlight = {0, 1, 2, 4};


    /* Messages arguments */
    /**
     *  Message type
    */
    private int msgType;
    /**
     * Passenger identification
     */
    private int passengerID;
    /**
     * Passenger destination (final or not)
     */
    private boolean final_destination;
    /**
     * Passenger flight number 
     */
    private int flight_nr;
    /**
     * Attq count flight number 
     */
    private int set_count_flights_attq;
    /**
     * Arrival lounge count flight number 
     */
    private int set_count_flights_al;
    /**
     * Number of passenger's {@link Bag}s per flight
     */
    private Bag[] bags;
    /**
     * Passenger state
     */
    private PassengerState passengerState;

    /* Messages type */

    public Message(int type, int nPlane, int passengerID, PassengerState passengerState){
        msgType = type;
        if(msgType == GOINGHOME){
            this.flight_nr = nPlane;
            this.passengerID = passengerID;
            this.passengerState = passengerState;
        }
    }

    /**
     * Message type 5
     * @param type message type
     * @param flight_number passenger flight number
     * @param passengerID passenger identification
     * @param bags number of passenger's bags per flight
     * @param finalDestination type of detination (final or not)
     */
    public Message(int type, int flight_number, int passengerID, Bag[] bags, boolean finalDestination){
        msgType = type;
        if(msgType == WSID){
            this.flight_nr = flight_number;
            this.passengerID = passengerID;
            this.bags = bags;
            this.final_destination = finalDestination;
        }
    }
    /**
     *  Message type 4
     *
     *    @param type message type
     *    @param id passenger id
     *    @param flight_nr passenger flight number
     */ 
    public Message (int type, int id, int flight_nr){
        msgType = type;
        if(type == INITP || msgType == PNEXTLEG){
            this.passengerID = id;
            this.flight_nr = flight_nr;
        }
    }
    /**
     *  Message type 3
     *
     *    @param type message type
     */ 
    public Message (int type, boolean final_destination){
        msgType = type;
        if(type == DEST){
            this.final_destination = final_destination;
        }
    }
    /**
     *  Message type 3
     *
     *    @param type message type
     */ 
    public Message (int type){
        msgType = type;
    }
    /**
     *  Message type 2
     *
     *    @param type message type
     *    @param bagsPerFlight list of bags per flight
     */ 
    public Message (int type, int[] bagsPerFlight){
        msgType = type;
        if (msgType == BAGS_PL){
            for(int i = 0; i< bagsPerFlight.length;i++){
                bagsPerFlight[i] = bagsPerFlight[i];
            }
        }
    }
    /**
     *  Message type 1
     *
     *    @param type message type
     *    @param i passenger identification / attq/al flight count
     */ 
    public Message (int type, int i){
        msgType = type;
        if (msgType == WSID || 
            msgType == TAKINGBUS || msgType == ENTERINGBUS || msgType == LEAVINGBUS){
            this.passengerID = i;
        }else if(msgType == SET_FLIGHT_attq){
            this.set_count_flights_attq = i+1;
        }else if(msgType == SET_FLIGHT_al){
            this.set_count_flights_al = i+1;
        }

    }

    /**
     * Get message type
     * @return int msgType
     */
    public int getType (){
        return (msgType);
    }
    /**
     * Get passengerID
     * @return int passengerID
     */
    public int get_passengerID(){
        return(passengerID);
    }
    /**
     * Get what should i do answer
     * @return int what should i do option
     */
    public char get_WSID (){
        return (WSID_ANSWER);
    }
    /**
     * Get list of bags per flight
     * @return int[] bagsPerFlight
     */
    public int[] get_nrBagsPerFlight (){
        return (bagsPerFlight);
    }
    /**
     * Get final destination
     * @return boolean final_destination
     */
    public boolean get_destination (){
        return (final_destination);
    }
    /**
     * Get passenger flight number
     * @return flight_nr Passenger flight number
     */
    public int get_flight(){
        return this.flight_nr;
    }
    /**
     * Get attq flight count
     * @return set_count_flights Attq flights count
     */
    public int get_setFlightCount_attq(){
        return this.set_count_flights_attq;
    }
    /**
     * Get arrival lounge flight count
     * @return set_count_flights Attq flights count
     */
    public int get_setFlightCount_al(){
        return this.set_count_flights_al;
    }
    /**
     * Get number of passenger's bags per flight
     * @return bags number of passenger's bags per flight
     */
    public Bag[] get_bags(){
        return this.bags;
    }
    /**
     * Get passenger state
     * @return passengerState passenger state
     */
    public PassengerState get_passengerState(){
        return this.passengerState;
    }

}

