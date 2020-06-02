package clientSide;

import comInf.Message;
import global.*;
import serverSide.*;

public class ArrivalTerminalExitStub{

    private String serverHostName = "localhost";
    private int serverPortNumb;

    public ArrivalTerminalExitStub(String hostname, int port){
        serverHostName = hostname;
        serverPortNumb = port;
    }
    
    /**
     * Passengers enter a lock state while waiting for every Passenger to finish their lifecycle
     * @param nPlane
     * @param passengerID
     * @param passengerState
     */
    public void goHome(int nPlane, int passengerID, PassengerState passengerState) {
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
        outMessage = new Message (Message.GOINGHOME, nPlane, passengerID, passengerState);   
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

    public void signalCompletion(){        // create connection
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
}