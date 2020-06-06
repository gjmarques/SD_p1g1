package clientSide;

import comInf.Message;
import global.*;
import serverSide.*;

/**
 * This class defines the stub of the {@link ArrivalTerminalExit} in the AIRPORT RAPSODY that implements the 
 * client-server model (type 2) with static launch of the threads
 */
public class ArrivalTerminalExitStub{

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
    public ArrivalTerminalExitStub(String hostname, int port){
        serverHostName = hostname;
        serverPortNumb = port;
    }
    
    /**
     * Passengers enter a lock state while waiting for every Passenger to finish their lifecycle (service request)
     * @param flight_number flight number
     * @param passengerID passenger identification
     * @param passengerState passenger state
     */
    public void goHome(int flight_number, int passengerID, PassengerState passengerState) {
        // create connection
        ClientCom con = new ClientCom(serverHostName, Global.arrivalTerminalExitStub_PORT);
        Message inMessage, outMessage;
        Passenger p_thread = (Passenger) Thread.currentThread();
        while (!con.open ()){
            try{ 
                p_thread.sleep ((long) (10));
            }catch (InterruptedException e) {}
        }
        // send message to arrival lounge interface, and wait for answer
        outMessage = new Message (Message.GOINGHOME, flight_number, passengerID, passengerState);   
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
     * Signal that passenger is in the departure terminal exit shared region (Service request)
     */
    public void signalPassenger(){
        // create connection
        ClientCom con = new ClientCom(serverHostName, Global.arrivalTerminalExitStub_PORT);
        Message inMessage, outMessage;
        DTEProxy p_thread = (DTEProxy) Thread.currentThread();
        while (!con.open ()){
            try{ 
                p_thread.sleep ((long) (10));
            }catch (InterruptedException e) {}
        }

        // send message to arrival lounge interface, and wait for answer
        outMessage = new Message (Message.SIGNAL_PASSENGER);   
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
     * Signal completion of tasks (service request)
     */
    public void signalCompletion(){    
        ClientCom con = new ClientCom(serverHostName, Global.arrivalTerminalExitStub_PORT);
        Message inMessage, outMessage;
        DTEProxy p_thread = (DTEProxy) Thread.currentThread();
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
     * Shutdown of the server (service request)
     */
    public void shutdown() {
        ClientCom con = new ClientCom(serverHostName, Global.arrivalTerminalExitStub_PORT);
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