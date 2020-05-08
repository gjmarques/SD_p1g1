package mainProgram;

/**
 * This class initiates values of the entities.
 */

public class Global{

    /**
     * Passenger host name
     */
    public static final String passenger_HOSTNAME = null;
    /**
     * Passenger port number
     */
    public static final int passenger_PORT = 4001;
    /**
     * Arrival Lounge port number
     */
    public static final int arrivalLoungeStub_PORT = 4002;
    /**
     * Arrival Terminal Transfer Quay Stub port number
     */
    public static final int arrivalTermTransfQuayStub_PORT = 4003;
    /**
     * Departure Terminal Tranfer Quay port number
     */
    public static final int departureTermTransfQuayStub_PORT = 4004;
    /**
     * Baggage Collection Point port number
     */
    public static final int baggageCollectionPointStub_PORT = 4005;
    /**
     * Baggage Reclaim Office port number
     */
    public static final int baggageReclaimOfficeStub_PORT = 4006;
    /**
     * Arrival Terminal Exit port number
     */
    public static final int arrivalTerminalExitStub_PORT = 4007;
    /**
     * Departure Terminal Entrance port number
     */
    public static final int departureTerminalEntranceStub_PORT = 4008;
    /**
     * General Information Repository port number
     */
    public static final int genRepo_PORT = 4009;


    /**
     * Number of passengers
     */
    public static final int NR_PASSENGERS = 6;
    /**
     * Number of bags per passenger
     */
    public static final int MAX_BAGS = 2;
    /**
     * Number of seats in the transfer bus
     */
    public static final int BUS_SIZE = 3;
    /**
     * Number of flights incoming the airport
     */
    public static final int NR_FLIGHTS = 5;
    /**
     * Probability of losing a bag
     */
    public static final int LOST_BAG_PERCENTAGE = 10; 
}