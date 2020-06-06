package clientSide;

import global.*;
import comInf.Message;
import serverSide.*;

/**
 * This class defines the stub of the {@link DepartureTerminalEntrance} in the AIRPORT RAPSODY that implements the 
 * client-server model (type 2) with static launch of the threads
 */
public class DepartureTerminalEntranceStub{

    /**
     * Name of the computational system where the server is located
     */
    private String serverHostName = "localhost";
    /**
     * Server listening port number
     */
    private int serverPortNumb;

    /**
     * Inntantiation of the stub to the Departure Terminal Entrance
     * @param hostname name of the computer system where the server is located
     * @param port ort server listening port number
     */
    public DepartureTerminalEntranceStub(String hostname, int port){
        serverHostName = hostname;
        serverPortNumb = port;
    }
    /**
     * Prepare next leg (service request)
     * @param flight_number number of flight
     * @param passengerID passenger identification
     */
    public void prepareNextLeg(int flight_number, int passengerID) {
        // create connection
        ClientCom con = new ClientCom(serverHostName, Global.departureTerminalEntranceStub_PORT);
        Message inMessage, outMessage;
        Passenger p_thread = (Passenger) Thread.currentThread();
        while (!con.open ()){
            try{ 
                p_thread.sleep ((long) (10));
            }catch (InterruptedException e) {}
        }
        // send message to arrival lounge interface, and wait for answer
        outMessage = new Message (Message.PNEXTLEG, passengerID, flight_number);   
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
     * Signal passenger (service request)
     */
    public void signalPassenger(){
        // create connection
        ClientCom con = new ClientCom(serverHostName, Global.departureTerminalEntranceStub_PORT);
        Message inMessage, outMessage;
        ATEProxy p_thread = (ATEProxy) Thread.currentThread();
        while (!con.open ()){
            try{ 
                p_thread.sleep ((long) (10));
            }catch (InterruptedException e) {}
        }

        // send message to arrival lounge interface, and wait for answer
        outMessage = new Message (Message.SIGNAL_PASSENGER);   
        con.writeObject (outMessage);;
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
     * Signal completion (service request)
     */
    public void signalCompletion(){       
        ClientCom con = new ClientCom(serverHostName, Global.departureTerminalEntranceStub_PORT);
        Message inMessage, outMessage;
        ATEProxy p_thread = (ATEProxy) Thread.currentThread();
        while (!con.open ()){
            try{ 
                p_thread.sleep ((long) (10));
            }catch (InterruptedException e) {}
        }
        // send message to arrival lounge interface, and wait for answer
        outMessage = new Message (Message.SIGNAL_COMPLETION);   
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
        ClientCom con = new ClientCom(serverHostName, Global.departureTerminalEntranceStub_PORT);
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