package clientSide;

import global.*;
import comInf.Message;

/**
 * This class defines the stub of the {@link serverSide.DepartureTermTransfQuay} in the AIRPORT RAPSODY that implements the 
 * client-server model (type 2) with static launch of the threads
 */
public class DepartureTermTransfQuayStub{

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
    public DepartureTermTransfQuayStub(String hostname, int port){
        serverHostName = hostname;
        serverPortNumb = port;
    }

    /**
     * Signal passenger to leave the bus (service request)
     * @param passengerID passenger identification
     */
    public void leaveTheBus(int passengerID) {
        // create connection
        ClientCom con = new ClientCom(serverHostName, Global.departureTermTransfQuayStub_PORT);
        Message inMessage, outMessage;
        Passenger p_thread = (Passenger) Thread.currentThread();
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
     * Bus Driver signals that passengers are going to the departure terminal tranfer quay (service request)
     */
    public void goToDepartureTerminal(){
        // create connection
        ClientCom con = new ClientCom(serverHostName, Global.departureTermTransfQuayStub_PORT);
        Message inMessage, outMessage;
        BusDriver p_thread = (BusDriver) Thread.currentThread();
        while (!con.open ()){
            try{ 
                p_thread.sleep ((long) (10));
            }catch (InterruptedException e) {}
        }
        // send message to arrival lounge interface, and wait for answer
        outMessage = new Message (Message.GOTO_DTTQ);   
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
     * Bus driver signals passengers to leave the bus (service request)
     * @param busPassengers number of passengers inside the bus
     */
    public void parkTheBusAndLetPassengerOff(int busPassengers) {
        // create connection
        ClientCom con = new ClientCom(serverHostName, Global.departureTermTransfQuayStub_PORT);
        Message inMessage, outMessage;
        BusDriver p_thread = (BusDriver) Thread.currentThread();
        while (!con.open ()){
            try{ 
                p_thread.sleep ((long) (10));
            }catch (InterruptedException e) {}
        }
        // send message to arrival lounge interface, and wait for answer
        outMessage = new Message (Message.PARKBUS, busPassengers);   
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
     * Shutdown of the server (Service request)
     */
    public void shutdown() {
        // create connection
        ClientCom con = new ClientCom(serverHostName, Global.departureTermTransfQuayStub_PORT);
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