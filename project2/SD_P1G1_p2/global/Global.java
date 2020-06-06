package global;

/**
 * This class initiates values of the entities.
 */

public class Global{

    /**
     * Passenger host name
     */
    public static final String passenger_HOSTNAME = "localhost";
    /**
     * Passenger port number
     */
    public static final int passenger_PORT = 4001;
   /**
     * Arrival Lounge Stub host name
     */
    public static final String arrivalLoungeStub_HOSTNAME = "localhost";
    /**
     * Arrival Lounge port number
     */
    public static final int arrivalLoungeStub_PORT = 4002;
   /**
     * Arrival Terminal Transfer Quay Stub host name
     */
    public static final String arrivalTermTransfQuayStub_HOSTNAME = "localhost";
    /**
     * Arrival Terminal Transfer Quay Stub port number
     */
    public static final int arrivalTermTransfQuayStub_PORT = 4003;
   /**
     * Departure Terminal Tranfer Quay host name
     */
    public static final String departureTermTransfQuayStub_HOSTNAME = "localhost";
    /**
     * Departure Terminal Tranfer Quay port number
     */
    public static final int departureTermTransfQuayStub_PORT = 4004;
   /**
     * Baggage Collection Point host name
     */
    public static final String baggageCollectionPointStub_HOSTNAME = "localhost";
    /**
     * Baggage Collection Point port number
     */
    public static final int baggageCollectionPointStub_PORT = 4005;
   /**
     * Baggage Reclaim Office host name
     */
    public static final String baggageReclaimOfficeStub_HOSTNAME = "localhost";
    /**
     * Baggage Reclaim Office port number
     */
    public static final int baggageReclaimOfficeStub_PORT = 4006;
   /**
     * Arrival Terminal Exit host name
     */
    public static final String arrivalTerminalExitStub_HOSTNAME = "localhost";
    /**
     * Arrival Terminal Exit port number
     */
    public static final int arrivalTerminalExitStub_PORT = 4007;
   /**
     * Departure Terminal Entrance host name
     */
    public static final String departureTerminalEntranceStub_HOSTNAME = "localhost";
    /**
     * Departure Terminal Entrance port number
     */
    public static final int departureTerminalEntranceStub_PORT = 4008;
   /**
     * General Information Repository host name
     */
    public static final String genRepo_HOSTNAME = "localhost";
    /**
     * General Information Repository port number
     */
    public static final int genRepo_PORT = 4009;
   /**
     * Temporary Storage Area host name
     */
    public static final String tempStorageArea_HOSTNAME = "localhost";
    /**
     * Temporary Storage Area port number
     */
    public static final int tempStorageArea_PORT = 4010;
   /**
     * Porter host name
     */
    public static final String porter_HOSTNAME = "localhost";
    /**
     * Porter port number
     */
    public static final int porter_PORT = 4011;
   /**
     * Bus Driver host name
     */
    public static final String busTime_HOSTNAME = "localhost";
    /**
     * Bus Driver port number
     */
    public static final int busTime_PORT = 4012;


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
    public static final int MAX_FLIGHTS = 5;
    /**
     * Probability of losing a bag
     */
    public static final int LOST_BAG_PERCENTAGE = 10; 
}