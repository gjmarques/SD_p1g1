package sharedRegions;

import comInf.Message;
import entities.*;
import mainProgram.*;

public class ArrivalTerminalExitStub{

    private String serverHostName = null;
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
        ClientCom con = new ClientCom(serverHostName, Global.arrivalLoungeStub_PORT);
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
            System.out.println ("Thread " + p_thread.getName () + ": Tipo inválido!");
            System.out.println (inMessage.toString ());
            System.exit (1);
        }
     con.close ();

    }
}