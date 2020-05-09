package sharedRegions;

import comInf.Message;
import mainProgram.*;
import entities.*;

public class ArrivalLoungeStub{

    private String serverHostName = null;
    private int serverPortNumb;

    public ArrivalLoungeStub(String hostname, int port){
        serverHostName = hostname;
        serverPortNumb = port;
    }

    public char whatShouldIDo(int flight_number, int passengerID, Bag[] bags, boolean finalDestination){
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
        outMessage = new Message (Message.WSID, flight_number, passengerID, bags, finalDestination);   
        con.writeObject (outMessage);
 
        // receive new in message, and process it
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.WSID_ANSWER){ 
            System.out.println ("Thread " + p_thread.getName () + ": Tipo inválido!");
            System.out.println (inMessage.toString ());
            System.exit (1);
        }
        con.close ();
        return inMessage.get_WSID();
    }
    /**
     * This method updates internal arrival lounge flight count.
     * @param nFlight
     */
	public void setFlight(int nFlight){
		// create connection
        ClientCom con = new ClientCom(serverHostName, Global.arrivalTermTransfQuayStub_PORT);
        Message inMessage, outMessage;
        Passenger p_thread = (Passenger) Thread.currentThread();
        while (!con.open ()){
            try{ 
                p_thread.sleep ((long) (10));
            }catch (InterruptedException e) {}
        }
        // send message to arrival lounge interface, and wait for answer
        outMessage = new Message (Message.SET_FLIGHT_al, nFlight);   
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