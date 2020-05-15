package sharedRegions;

import comInf.Message;
import entities.*;
import mainProgram.*;

public class DepartureTerminalEntranceStub{

    private String serverHostName = null;
    private int serverPortNumb;

    public DepartureTerminalEntranceStub(String hostname, int port){
        serverHostName = hostname;
        serverPortNumb = port;
    }
    //prepareNextLeg
    public void prepareNextLeg(int nPlane, int passengerID) {
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
        outMessage = new Message (Message.PNEXTLEG, passengerID, nPlane);   
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