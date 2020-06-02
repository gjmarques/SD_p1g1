package comInf;

import clientSide.*;

import java.io.*;

public class Message implements Serializable {

    /**
     * Chave de serialização
     */
    private static final long serialVersionUID = 1001L;

    /* Tipos das mensagens */
    /**
     * What should I do request
     */
    public static final int WSID = 1;
    /**
     * Successful action
     */
    public static final int ACK = 999;

    public static final int GOHOME = 2;
    public static final int TAKEBUS = 3;
    public static final int COLLECTBAG = 4;
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
    /**
     * Signal that passenger is going to collect a bag
     */
    public static final int GOCOLLECTBAG = 13;
    /**
     * Message type notification that a bag has been collected
     */
    public static final int BAG_COLLECTED = 14;
    /**
     * Signal Porter to carry bag to appropriate store
     */
    public static final int CARRYTOAPPSTORE = 15;
    /**
     * Report missing bag
     */
    public static final int REPORT_MISSING = 16;
    /**
     * Send bags per flight to repository
     */
    public static final int BAGS_P_FLIGHT = 17;
    /**
     * Signal Porter whether to take a rest or not
     */
    public static final int REST = 18;
    /**
     * Mesage to signal Porter to take a rest
     */
    public static final int REST_Y = 19;
    /**
     * Message to signal Porter to not take a rest
     */
    public static final int REST_N = 20;
    /**
     * Message to signal Porter to go pick up a bag
     */
    public static final int COLLECTBAG_PORTER = 21;
    /**
     * Message to signal Porter that there is not more bags to collect
     */
    public static final int NO_BAGS_TO_COLLECT = 22;
    /**
     * Signal General Information Repository that there is one less bag at the
     * plane's hold
     */
    public static final int LESSBAGS = 23;
    /**
     * Check if Bus Driver's work days ended
     */
    public static final int WORK_END = 24;
    /**
     * Signal Bus Driver that his work days ended
     */
    public static final int WORK_ENDED = 25;
    /**
     * Signal Bus Driver that his work days are not over
     */
    public static final int WORK_NOT_ENDED = 26;
    /**
     * Signal Bus Driver how many passengers there is to get on board of the bus
     */
    public static final int BUSBOARD = 27;
    /**
     * Signal Bus Driver to go to the Departure Terminal Transfer Quay
     */
    public static final int GOTO_DTTQ = 28;
    /**
     * Signal Bus Driver to park the bus and let the passengers out
     */
    public static final int PARKBUS = 29;
    /**
     * Signal Bus Driver to go to the arrival terminal transfer quay
     */
    public static final int GOTO_ATTQ = 30;
    /**
     * Signal Bus Driver to park the bus (at the attq)
     */
    public static final int PARK = 31;
    /**
     * Signal Bus Timer
     */
    public static final int D_TIME = 32;
    /**
     * Signal Proxys to shutdown
     */
    public static final int SHUT = 998;
    /**
     * Signal Passenger state update
     */
    public static final int PSGR_STATE = 33;
    /**
     * Signal Porter state update
     */
    public static final int PORTER_STATE = 34;
    /**
     * Signal Porter state update
     */
    public static final int BUSDRIVER_STATE = 35;
    /**
     * Signal PSGR state update
     */
    public static final int PSGR_UPDATE_STATE = 36;
    /**
     * Signal Passenger update state by Arrival Terminal Exit
     */
    public static final int PSGR_UPDATE_STATE_ATE = 37;
    /**
     * Signal passenger to wait in line for entering the bus (in arrival terminal tranfer quay)
     */
    public static final int BUS_WAITNG_LINE = 38;
    /**
     * Signal passenger to sit in the bus (in arrival terminal tranfer quay)
     */
    public static final int BUS_SITTING = 39;
    /**
     * Signal that passenger has collected his bags
     */
    public static final int PSGR_COLLECTED_BAGS = 40;
    /**
     * Signal that passenger is at the collection mat conveyor belt
     */
    public static final int COLLECTIONMAT_CONVBELT = 41;
    public static final int BAGS_TMP = 42;

    /**
     * Signal that passenger is at the collection mat conveyor belt
     */
    public static final int SIGNAL_PASSENGER = 42;

    /**
     * Signal that passenger is at the collection mat conveyor belt
     */
    public static final int SIGNAL_COMPLETION = 43;





    /* Other variables */
    /**
     * What should I do option go home - h (a) taking a bus - t (b) collect a bag -
     * c (c)
     */
    public static final char WSID_ANSWER = 'z';
    /**
     * List of bags per flight
     */
    public static final int[] bagsPerFlight = {0, 1, 2, 3, 4};
    /**
     * /* Tell Porter to take a rest
     */
    public boolean do_rest = false;
    /**
     * Tell Porter to not take a rest
     */
    public boolean no_rest = false;
    /**
     * Whether Bus driver ended ('E') (or not ('W')) his work day
     */
    public char busDriver_workDay;
    /**
     * Number of passengers that the Bus Driver has to let out of the bus
     */
    public int busPassengers;

    /* Messages arguments */
    /**
     * Message type
     */
    private int msgType;
    /**
     * Passenger identification
     */
    private int passengerID;
    /**
     * Passenger destination (final or not) (related to bags)
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
    /**
     * Porter state
     */
    private PorterState porterState;
    /**
     * Bus Driver state
     */
    private BusDriverState busDriverState;
    /**
     * Bag identification collected
     */
    public int bag_id;
    /**
     * Bag (destination, id, flight number)
     */
    public Bag bag;
    /**
     * Number of passengers boarding a bus
     */
    public int bus_number_passengers;
    /**
     * Final destination boolean (related to general information repository)
     */
    public boolean final_dest = false;
    /**
     * Total number of bags of a passenger
     */
    public int nr_bags;
    /**
     * Number of bags at the conveyor belt
     */
    public int nrLuggageConvBelt;

    /* Messages type */

    /**
     * Message type 12
     * 
     * @param type
     * @param passengerID
     * @param passengerState
     */
    public Message(int type, int passengerID, PassengerState passengerState) {
        msgType = type;
        if (msgType == PSGR_UPDATE_STATE) {
            this.passengerID = passengerID;
            this.passengerState = passengerState;
        }
    }

    /**
     * Message type 11
     * 
     * @param type
     * @param busDriverState
     */
    public Message(int type, BusDriverState busDriverState) {
        msgType = type;
        if (msgType == BUSDRIVER_STATE) {
            this.busDriverState = busDriverState;
        }
    }

    /**
     * Message type 10
     * 
     * @param type
     * @param porterState
     */
    public Message(int type, PorterState porterState) {
        msgType = type;
        if (msgType == PORTER_STATE) {
            this.porterState = porterState;
        }
    }

    /**
     * Message type 9
     * 
     * @param type
     * @param flight_nr
     * @param passengerID
     * @param passengerState
     * @param dest
     * @param nr_bags
     */
    public Message(int type, int flight_nr, int passengerID, PassengerState passengerState, boolean dest, int nr_bags) {
        msgType = type;
        if (msgType == PSGR_STATE) {
            this.flight_nr = flight_nr;
            this.passengerID = passengerID;
            this.passengerState = passengerState;
            this.final_dest = dest;
            this.nr_bags = nr_bags;
        }
    }

    /**
     * Message type 8
     * 
     * @param type       message type
     * @param final_dest final destination (related to general information
     *                   repository)
     */
    public Message(int type, boolean final_dest) {
        msgType = type;
        if (msgType == DEST) {
            // related to general information repository
            this.final_dest = final_dest;
        }
    }

    /**
     * Message type 7
     * 
     * @param type message type
     * @param bag  Bag
     */
    public Message(int type, Bag bag) {
        msgType = type;
        if (msgType == CARRYTOAPPSTORE || msgType == COLLECTBAG_PORTER || msgType == LESSBAGS) {
            this.bag = bag;
        }
    }

    /**
     * Message type 6
     * 
     * @param type           message type
     * @param flight_number  passenger flight number
     * @param passengerID    passenger identification
     * @param passengerState passenger state
     */
    public Message(int type, int flight_number, int passengerID, PassengerState passengerState) {
        msgType = type;
        if (msgType == GOINGHOME || msgType == PSGR_UPDATE_STATE_ATE) {
            this.flight_nr = flight_number;
            this.passengerID = passengerID;
            this.passengerState = passengerState;
        }
    }

    /**
     * Message type 5
     * 
     * @param type             message type
     * @param flight_number    passenger flight number
     * @param passengerID      passenger identification
     * @param bags             number of passenger's bags per flight
     * @param finalDestination type of detination (final or not)
     */
    public Message(int type, int flight_number, int passengerID, Bag[] bags, boolean finalDestination) {
        msgType = type;
        if (msgType == WSID) {
            this.flight_nr = flight_number;
            this.passengerID = passengerID;
            this.bags = bags;
            this.final_destination = finalDestination; // related to bags
        }
    }

    /**
     * Message type 4
     *
     * @param type      message type
     * @param id        passenger id
     * @param i passenger flight number / passenger number of bags
     */
    public Message(int type, int id, int i) {
        msgType = type;
        if (type == INITP || msgType == PNEXTLEG || msgType == REPORT_MISSING) {
            this.passengerID = id;
            this.flight_nr = i;
        }else if( msgType == PSGR_COLLECTED_BAGS){
            this.passengerID = id;
            this.nr_bags = i;
        }
    }

    /**
     * Message type 3COLLECTBAG_PORTER
     */
    public Message(int type) {
        msgType = type;
        if (msgType == REST_Y) {
            this.do_rest = true;
            this.no_rest = false;
        } else if (msgType == REST_N) {
            this.do_rest = false;
            this.no_rest = true;
        } else if (msgType == WORK_ENDED) {
            this.busDriver_workDay = 'E';
        } else if (msgType == WORK_NOT_ENDED) {
            this.busDriver_workDay = 'W';
        }
    }

    /**
     * Message type 2
     *
     * @param type          message type
     * @param bagsPerFlight list of bags per flight
     */
    public Message(int type, int[] bagsPerFlight) {
        msgType = type;
        if (msgType == BAGS_P_FLIGHT) {
            for (int i = 0; i < bagsPerFlight.length; i++) {
                bagsPerFlight[i] = bagsPerFlight[i];
            }
        }
    }

    /**
     * Message type 1
     *
     * @param type message type
     * @param i    passenger identification / attq/al flight count
     */
    public Message(int type, int i) {
        msgType = type;
        if (msgType == WSID || msgType == TAKINGBUS || msgType == ENTERINGBUS || msgType == LEAVINGBUS 
            || msgType == BUS_WAITNG_LINE || msgType == BUS_SITTING || msgType == COLLECTIONMAT_CONVBELT) {
            this.passengerID = i;
        } else if (msgType == SET_FLIGHT_attq) {
            this.set_count_flights_attq = i;
        } else if (msgType == SET_FLIGHT_al) {
            this.set_count_flights_al = i;
        } else if (msgType == BAG_COLLECTED) {
            this.bag_id = i;
        } else if (msgType == GOCOLLECTBAG) {
            this.passengerID = i;
        } else if (msgType == BUSBOARD) {
            this.bus_number_passengers = i;
        } else if (msgType == PARKBUS) {
            this.busPassengers = i;
        }
    }

    /**
     * Get message type
     * 
     * @return int msgType
     */
    public int getType() {
        return (msgType);
    }

    /**
     * Get passengerID
     * 
     * @return int passengerID
     */
    public int get_passengerID() {
        return (passengerID);
    }

    /**
     * Get what should i do answer
     * 
     * @return int what should i do option
     */
    public char get_WSID() {
        return (WSID_ANSWER);
    }

    /**
     * Get list of bags per flight
     * 
     * @return int[] bagsPerFlight
     */
    public int[] get_nrBagsPerFlight() {
        return (bagsPerFlight);
    }

    /**
     * Get final destination (related to Bags)
     * 
     * @return boolean final_destination
     */
    public boolean get_destination() {
        return (final_destination);
    }

    /**
     * Get passenger flight number
     * 
     * @return flight_nr Passenger flight number
     */
    public int get_flight() {
        return this.flight_nr;
    }

    /**
     * Get attq flight count
     * 
     * @return set_count_flights Attq flights count
     */
    public int get_setFlightCount_attq() {
        return this.set_count_flights_attq;
    }

    /**
     * Get arrival lounge flight count
     * 
     * @return set_count_flights Attq flights count
     */
    public int get_setFlightCount_al() {
        return this.set_count_flights_al;
    }

    /**
     * Get number of passenger's bags per flight
     * 
     * @return bags number of passenger's bags per flight
     */
    public Bag[] get_bags() {
        return this.bags;
    }

    /**
     * Get passenger state
     * 
     * @return passengerState passenger state
     */
    public PassengerState get_passengerState() {
        return this.passengerState;
    }

    /**
     * Get porter state
     * 
     * @return porterState porter state
     */
    public PorterState get_porterState() {
        return this.porterState;
    }

    /**
     * Get Bus Driver state
     * 
     * @return busDriverState Bus Driver state
     */
    public BusDriverState get_busDriverState() {
        return this.busDriverState;
    }

    /**
     * Get passenger bags id
     * 
     * @return bag identification
     */
    public int get_Bag_id() {
        return this.bag_id;
    }

    /**
     * Get passenger bags
     * 
     * @return bag Bag
     */
    public Bag get_Bag() {
        return this.bag;
    }

    /**
     * Get signal wether Porter can rest ('E') or not ('W')
     * 
     * @return rest
     */
    public char get_Rest() {
        if (this.do_rest)
            return 'E';
        if (this.no_rest)
            return 'W';
        else
            return 'z';
    }

    /**
     * Get if the Bus Driver's work days are over
     * 
     * @return busDriver_workDay
     */
    public char get_Work_days() {
        return this.busDriver_workDay;
    }

    /**
     * Get number of passengers waiting to get on the bus
     * 
     * @return bus_number_passengers
     */
    public int get_Bus_numPassengers_boarding() {
        return this.bus_number_passengers;
    }

    /**
     * Get the number of passengers inside the bus that the Bus Driver has to let
     * get out
     * 
     * @return busPassengers
     */
    public int get_BusPassengers() {
        return this.busPassengers;
    }

    /**
     * Get final destination (related to general information repository)
     * 
     * @return
     */
    public boolean get_FinalDestREPO() {
        return this.final_dest;
    }
    /**
     * Get total number of bags of a passenger
     * 
     * @return int nr_bags
     */
    public int get_nrBags() {
        return this.nr_bags;
    }
    /**
     * Get number of bags at conveyor belt collection point
     * @return nrLuggageConvBelt
     */
    public int ger_nrLuggageCovBelt(){
        return this.nrLuggageConvBelt;
    }
    public boolean get_destination_passenger(){
        return final_dest;
    }

}
