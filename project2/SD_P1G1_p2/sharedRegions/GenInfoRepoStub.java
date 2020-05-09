package sharedRegions;

import comInf.Message;
import mainProgram.*;
import entities.*;

public class GenInfoRepoStub{

    private String serverHostName = null;
    private int serverPortNumb;

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
        Passenger p_thread = (Passenger) Thread.currentThread();
        while (!con.open ()){
            try{ 
                p_thread.sleep ((long) (10));
            }catch (InterruptedException e) {}
        }
        // send message to arrival lounge interface, and wait for answer
        outMessage = new Message (Message.BAGS_PL, bagsPerFlight);   
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

    /**
     * Count type of detination (final or not) (service request).
     * @param dest type of destination
     */
    public void countDest(boolean dest){
        // create connection
        ClientCom con = new ClientCom(serverHostName, Global.genRepo_PORT);
        Message inMessage, outMessage;
        Passenger p_thread = (Passenger) Thread.currentThread();
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
            System.out.println ("Thread " + p_thread.getName () + ": Tipo inválido!");
            System.out.println (inMessage.toString ());
            System.exit (1);
        }
        con.close ();
    }

    /**
     * Initiate passenger in {@link sharedRegions.GenInfoRep} (service request).
     * @param flight_nr Passenger flight number
     * @param passengerID Passenger identification 
     */
    public void initPassenger(int flight_nr, int passengerID){
        // create connection
        ClientCom con = new ClientCom(serverHostName, Global.genRepo_PORT);
        Message inMessage, outMessage;
        Passenger p_thread = (Passenger) Thread.currentThread();
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
            System.out.println ("Thread " + p_thread.getName () + ": Tipo inválido!");
            System.out.println (inMessage.toString ());
            System.exit (1);
        }
        con.close ();
    }
    
    


}