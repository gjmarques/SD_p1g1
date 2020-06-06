package clientSide;

import global.*;
import comInf.Message;

/**
 * This class defines the stub of the {@link serverSide.GenInfoRepo} in the AIRPORT RAPSODY that implements the 
 * client-server model (type 2) with static launch of the threads
 */
public class GenInfoRepoStub{

    /**
     * Name of the computational system where the server is located
     */
    private String serverHostName = "localhost";
    /**
     * Server listening port number
     */
    private int serverPortNumb;

    /**
     * Inntantiation of the stub to the Departure Terminal Tranfer Quay
     * @param hostname name of the computer system where the server is located
     * @param port ort server listening port number
     */
    public GenInfoRepoStub(String hostname, int port){
        serverHostName = hostname;
        serverPortNumb = port;
    }

    /**
     * Number of pieces of luggage presently at the plane's hold (service request).
     * @param bagsPerFlight List of number of bags per flight.
     */
   public void nrBagsPlanesHold(int[] bagsPerFlight){
        // create connection
        ClientCom con = new ClientCom(serverHostName, Global.genRepo_PORT);
        Message inMessage, outMessage;
        Thread p_thread = Thread.currentThread();
        while (!con.open ()){
            try{ 
                p_thread.sleep ((long) (10));
            }catch (InterruptedException e) {}
        }
        // send message to arrival lounge interface, and wait for answer
        outMessage = new Message (Message.BAGS_P_FLIGHT, bagsPerFlight);   
        con.writeObject (outMessage);

        // receive new in message, and process it
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.ACK){ 
            System.out.println ("Thread " + p_thread.getName () + ": Invalid message type!");
            System.out.println (inMessage.toString ());
            System.exit (1);
        }
        con.close ();
    }
    /**
     * Count type of detination (final or not) (service request).
     * @param dest type of destination
     */
    public void countDest(boolean dest){
        // create connection
        ClientCom con = new ClientCom(serverHostName, Global.genRepo_PORT);
        Message inMessage, outMessage;
        Thread p_thread = Thread.currentThread();
        while (!con.open ()){
            try{ 
                p_thread.sleep ((long) (10));
            }catch (InterruptedException e) {}
        }
        // send message to arrival lounge interface, and wait for answer
        outMessage = new Message (Message.DEST, dest);   
        con.writeObject (outMessage);

        // receive new in message, and process it
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.ACK){ 
            System.out.println ("Thread " + p_thread.getName () + ": Invalid message type!");
            System.out.println (inMessage.toString ());
            System.exit (1);
        }
        con.close ();
    }

    /**
     * Initiate passenger in {@link serverSide.GenInfoRepo} (service request).
     * @param flight_nr Passenger's flight number
     * @param passengerID Passenger identification 
     */
    public void initPassenger(int flight_nr, int passengerID){
        // create connection
        ClientCom con = new ClientCom(serverHostName, Global.genRepo_PORT);
        Message inMessage, outMessage;
        Thread p_thread = Thread.currentThread();
        while (!con.open ()){
            try{ 
                p_thread.sleep ((long) (10));
            }catch (InterruptedException e) {}
        }
        // send message to arrival lounge interface, and wait for answer
        outMessage = new Message (Message.INITP, passengerID, flight_nr);   
        con.writeObject (outMessage);

        // receive new in message, and process it
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.ACK){ 
            System.out.println ("Thread " + p_thread.getName () + ": Invalid message type!");
            System.out.println (inMessage.toString ());
            System.exit (1);
        }
        con.close ();
    }
    /**
     * This methods updates the count of the number of bags on plane's hold, present at the general repository information (service request)
     * @param bag {@link Bag}
     */
    public void lessBagsOnPlanesHold(Bag bag){
        // create connection
        ClientCom con = new ClientCom(serverHostName, Global.genRepo_PORT);
        Message inMessage, outMessage;
        Thread p_thread = Thread.currentThread();
        while (!con.open ()){
            try{ 
                p_thread.sleep ((long) (10));
            }catch (InterruptedException e) {}
        }
        // send message to arrival lounge interface, and wait for answer
        outMessage = new Message (Message.LESSBAGS, bag);   
        con.writeObject (outMessage);

        // receive new in message, and process it
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.ACK){ 
            System.out.println ("Thread " + p_thread.getName () + ": Invalid message type!");
            System.out.println (inMessage.toString ());
            System.exit (1);
        }
        con.close ();
    }
    /**
     * Update Passenger state (service request)
     * @param flight_nr Passenger's flight number
     * @param passengerID pasenger identification
     * @param passengerState {@link PassengerState}
     * @param dest Passsenger destination
     * @param nr_bags number of bags that the Passengers brings
     */
    public void passengerState(int flight_nr, int passengerID, PassengerState passengerState,  boolean dest,  int nr_bags){
        // create connection
        ClientCom con = new ClientCom(serverHostName, Global.genRepo_PORT);
        Message inMessage, outMessage;
        Thread p_thread = Thread.currentThread();
        while (!con.open ()){
            try{ 
                p_thread.sleep ((long) (10));
            }catch (InterruptedException e) {}
        }
        // send message to arrival lounge interface, and wait for answer
        outMessage = new Message (Message.PSGR_STATE, flight_nr, passengerID, passengerState, dest, nr_bags);   
        con.writeObject (outMessage);

        // receive new in message, and process it
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.ACK){ 
            System.out.println ("Thread " + p_thread.getName () + ": Invalid message type!");
            System.out.println (inMessage.toString ());
            System.exit (1);
        }
        con.close (); 
    }
    /**
     * Updates Passenger state (service request)
     * @param passengerID passenger identification
     * @param passengerState {@link PassengerState}
     */
    public void passengerState(int passengerID, PassengerState passengerState){
        // create connection
        ClientCom con = new ClientCom(serverHostName, Global.genRepo_PORT);
        Message inMessage, outMessage;
        Thread p_thread = Thread.currentThread();
        while (!con.open ()){
            try{ 
                p_thread.sleep ((long) (10));
            }catch (InterruptedException e) {}
        }
        // send message to arrival lounge interface, and wait for answer
        outMessage = new Message (Message.PSGR_UPDATE_STATE, passengerID, passengerState);   
        con.writeObject (outMessage);

        // receive new in message, and process it
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.ACK){ 
            System.out.println ("Thread " + p_thread.getName () + ": Invalid message type!");
            System.out.println (inMessage.toString ());
            System.exit (1);
        }
        con.close (); 
    }
    /**
     * Updates Passenger state when he is at the Arrival Terminal Exit (service request)
     * @param flight_number passenger's flight number
     * @param passengerID passenger identification
     * @param passengerState {@link PassengerState}
     */
    public void passengerState(int flight_number, int passengerID, PassengerState passengerState){
        // create connection
        ClientCom con = new ClientCom(serverHostName, Global.genRepo_PORT);
        Message inMessage, outMessage;
        Thread p_thread = Thread.currentThread();
        while (!con.open ()){
            try{ 
                p_thread.sleep ((long) (10));
            }catch (InterruptedException e) {}
        }
        // send message to arrival lounge interface, and wait for answer
        outMessage = new Message (Message.PSGR_UPDATE_STATE_ATE, flight_number, passengerID, passengerState);   
        con.writeObject (outMessage);

        // receive new in message, and process it
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.ACK){ 
            System.out.println ("Thread " + p_thread.getName () + ": Invalid message type!");
            System.out.println (inMessage.toString ());
            System.exit (1);
        }
        con.close (); 
    }
    /**
     * Updates Porter state (service request)
     * @param porterState {@link PorterState}
     */
    public void porterState(PorterState porterState){
        // create connection
        ClientCom con = new ClientCom(serverHostName, Global.genRepo_PORT);
        Message inMessage, outMessage;
        Thread p_thread = Thread.currentThread();
        while (!con.open ()){
            try{ 
                p_thread.sleep ((long) (10));
            }catch (InterruptedException e) {}
        }
        // send message to arrival lounge interface, and wait for answer
        outMessage = new Message (Message.PORTER_STATE, porterState);   
        con.writeObject (outMessage);

        // receive new in message, and process it
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.ACK){ 
            System.out.println ("Thread " + p_thread.getName () + ": Invalid message type!");
            System.out.println (inMessage.toString ());
            System.exit (1);
        }
        con.close (); 
    }
    /**
     * Updates Bus Driver state (service request)
     * @param busDriverState {@link BusDriverState}
     */
    public void busDriverState(BusDriverState busDriverState){
        // create connection
        ClientCom con = new ClientCom(serverHostName, Global.genRepo_PORT);
        Message inMessage, outMessage;
        Thread p_thread = Thread.currentThread();
        while (!con.open ()){
            try{ 
                p_thread.sleep ((long) (10));
            }catch (InterruptedException e) {}
        }
        // send message to arrival lounge interface, and wait for answer
        outMessage = new Message (Message.BUSDRIVER_STATE, busDriverState);   
        con.writeObject (outMessage);

        // receive new in message, and process it
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.ACK){ 
            System.out.println ("Thread " + p_thread.getName () + ": Invalid message type!");
            System.out.println (inMessage.toString ());
            System.exit (1);
        }
        con.close (); 
    }
    /**
     * Signal passenger to leave the bus (service request)
     * @param passengerID passenger identification
     */
    public void leaveBus(int passengerID){
        // create connection
        ClientCom con = new ClientCom(serverHostName, Global.genRepo_PORT);
        Message inMessage, outMessage;
        Thread p_thread = Thread.currentThread();
        while (!con.open ()){
            try{ 
                p_thread.sleep ((long) (10));
            }catch (InterruptedException e) {}
        }
        // send message to arrival lounge interface, and wait for answer
        outMessage = new Message (Message.LEAVINGBUS, passengerID);   
        con.writeObject (outMessage);

        // receive new in message, and process it
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.ACK){ 
            System.out.println ("Thread " + p_thread.getName () + ": Invalid message type!");
            System.out.println (inMessage.toString ());
            System.exit (1);
        }
        con.close (); 
    }
    /**
     * A bag was carryied into the store room (service request)
     * @param bag {@link Bag}
     */
    public void bagAtStoreRoom(Bag bag){
        // create connection
        ClientCom con = new ClientCom(serverHostName, Global.genRepo_PORT);
        Message inMessage, outMessage;
        Thread p_thread = Thread.currentThread();
        while (!con.open ()){
            try{ 
                p_thread.sleep ((long) (10));
            }catch (InterruptedException e) {}
        }
        // send message to arrival lounge interface, and wait for answer
        outMessage = new Message (Message.CARRYTOAPPSTORE, bag);   
        con.writeObject (outMessage);

        // receive new in message, and process it
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.ACK){ 
            System.out.println ("Thread " + p_thread.getName () + ": Invalid message type!");
            System.out.println (inMessage.toString ());
            System.exit (1);
        }
        con.close (); 
    }
    /**
     * Updates the bus waiting line (ervice request). Meaning: Passenger passengerID is waiting to enter the bus.
     * @param passengerId
     */
    public void busWaitingLine(int passengerId){
        // create connection
        ClientCom con = new ClientCom(serverHostName, Global.genRepo_PORT);
        Message inMessage, outMessage;
        Thread p_thread = Thread.currentThread();
        while (!con.open ()){
            try{ 
                p_thread.sleep ((long) (10));
            }catch (InterruptedException e) {}
        }
        // send message to arrival lounge interface, and wait for answer
        outMessage = new Message (Message.BUS_WAITNG_LINE, passengerId);   
        con.writeObject (outMessage);

        // receive new in message, and process it
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.ACK){ 
            System.out.println ("Thread " + p_thread.getName () + ": Invalid message type!");
            System.out.println (inMessage.toString ());
            System.exit (1);
        }
        con.close (); 
    }
    /**
     * Update sitting queue of the bus (service request). Meaning: Passenger passengerID is sitted on the bus.
     * @param passengerId passenger identification
     */
    public void busSitting(int passengerId){
        // create connection
        ClientCom con = new ClientCom(serverHostName, Global.genRepo_PORT);
        Message inMessage, outMessage;
        Thread p_thread = Thread.currentThread();
        while (!con.open ()){
            try{ 
                p_thread.sleep ((long) (10));
            }catch (InterruptedException e) {}
        }
        // send message to arrival lounge interface, and wait for answer
        outMessage = new Message (Message.BUS_SITTING, passengerId);   
        con.writeObject (outMessage);

        // receive new in message, and process it
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.ACK){ 
            System.out.println ("Thread " + p_thread.getName () + ": Invalid message type!");
            System.out.println (inMessage.toString ());
            System.exit (1);
        }
        con.close (); 
    }
    /**
     * Number of bags collected by each passenger (service request)
     * @param passengerID passenger identification
     * @param nBags number of bags
     */
    public void passengerCollectedBags(int passengerID, int nBags){
        // create connection
        ClientCom con = new ClientCom(serverHostName, Global.genRepo_PORT);
        Message inMessage, outMessage;
        Thread p_thread = Thread.currentThread();
        while (!con.open ()){
            try{ 
                p_thread.sleep ((long) (10));
            }catch (InterruptedException e) {}
        }
        // send message to arrival lounge interface, and wait for answer
        outMessage = new Message (Message.PSGR_COLLECTED_BAGS, passengerID, nBags);   
        con.writeObject (outMessage);

        // receive new in message, and process it
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.ACK){ 
            System.out.println ("Thread " + p_thread.getName () + ": Invalid message type!");
            System.out.println (inMessage.toString ());
            System.exit (1);
        }
        con.close (); 
    }
    /**
     * Update the number of bags currently at the conveyor belt (service request)
     * @param nrLuggageConvBelt number of bags currently at the conveyor belt
     */
    public void collectionMatConveyorBelt(int nrLuggageConvBelt){
        // create connection
        ClientCom con = new ClientCom(serverHostName, Global.genRepo_PORT);
        Message inMessage, outMessage;
        Thread p_thread = Thread.currentThread();
        while (!con.open ()){
            try{ 
                p_thread.sleep ((long) (10));
            }catch (InterruptedException e) {}
        }
        // send message to arrival lounge interface, and wait for answer
        outMessage = new Message (Message.COLLECTIONMAT_CONVBELT, nrLuggageConvBelt);   
        con.writeObject (outMessage);

        // receive new in message, and process it
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.ACK){ 
            System.out.println ("Thread " + p_thread.getName () + ": Invalid message type!");
            System.out.println (inMessage.toString ());
            System.exit (1);
        }
        con.close (); 
    }
    /**
     * Report missing bags
     * @param nrBags number of bags lost
     * @param passengerID passenger identification
     */
    public void missingBags(int nrBags, int passengerID){
        // create connection
        ClientCom con = new ClientCom(serverHostName, Global.genRepo_PORT);
        Message inMessage, outMessage;
        Thread p_thread = Thread.currentThread();
        while (!con.open ()){
            try{ 
                p_thread.sleep ((long) (10));
            }catch (InterruptedException e) {}
        }
        // send message to arrival lounge interface, and wait for answer
        outMessage = new Message (Message.REPORT_MISSING_GIR, passengerID, nrBags);   
        con.writeObject (outMessage);

        // receive new in message, and process it
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.ACK){ 
            System.out.println ("Thread " + p_thread.getName () + ": Invalid message type!");
            System.out.println (inMessage.toString ());
            System.exit (1);
        }
        con.close (); 
    }
    /**
     * Shutdown of the server (service request)
     */
    public void shutdown(){
        // create connection
        ClientCom con = new ClientCom(serverHostName, Global.genRepo_PORT);
        Message inMessage, outMessage;
        Thread p_thread = Thread.currentThread();
        while (!con.open ()){
            try{ 
                p_thread.sleep ((long) (10));
            }catch (InterruptedException e) {}
        }
        // send message to arrival lounge interface, and wait for answer
        outMessage = new Message (Message.SHUT);   
        con.writeObject (outMessage);

        // receive new in message, and process it
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.ACK){ 
            System.out.println ("Thread " + p_thread.getName () + ": Invalid message type!");
            System.out.println (inMessage.toString ());
            System.exit (1);
        }
        con.close (); 
    }

}