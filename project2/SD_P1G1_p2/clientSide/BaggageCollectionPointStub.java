package clientSide;

import global.*;
import comInf.Message;

/**
 * This class defines the stub of the {@link serverSide.BaggageCollectionPoint} in the AIRPORT RAPSODY that implements the 
 * client-server model (type 2) with static launch of the threads
 */
public class BaggageCollectionPointStub{

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
    public BaggageCollectionPointStub(String hostname, int port){
        serverHostName = hostname;
        serverPortNumb = port;
    }
    /**
     * PAssenger is going to collect a bag (service request)
     * @param passengerID passenger identification
     * @return int bag identification
     */
    public int goCollectABag(int passengerID) {
        // create connection
        ClientCom con = new ClientCom(serverHostName, Global.baggageCollectionPointStub_PORT);
        Message inMessage, outMessage;
        Passenger p_thread = (Passenger) Thread.currentThread();
        while (!con.open ()){
            try{ 
                p_thread.sleep ((long) (10));
            }catch (InterruptedException e) {}
        }
        // send message to arrival lounge interface, and wait for answer
        outMessage = new Message (Message.GOCOLLECTBAG, passengerID);   
        con.writeObject (outMessage);

        // receive new in message, and process it
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.BAG_COLLECTED){ 
            System.out.println ("Thread " + p_thread.getName () + ": Invalid message type!");
            System.out.println (inMessage.toString ());
            System.exit (1);
        }
        con.close ();
        return inMessage.get_Bag_id();
    }
    /**
     * Porter announces that he is carrying a bag to its appropriate store (Service request)
     * @param bag {@link Bag}
     */
    public void carryItToAppropriateStore(Bag bag) {
        // create connection
        ClientCom con = new ClientCom(serverHostName, Global.baggageCollectionPointStub_PORT);
        Message inMessage, outMessage;
        Porter p_thread = (Porter) Thread.currentThread();
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
     * Porter announces that there is no more bags to collet (Service request) 
     */
    public void noMoreBagsToCollect() {
        // create connection
        ClientCom con = new ClientCom(serverHostName, Global.baggageCollectionPointStub_PORT);
        Message inMessage, outMessage;
        Porter p_thread = (Porter) Thread.currentThread();
        while (!con.open ()){
            try{ 
                p_thread.sleep ((long) (10));
            }catch (InterruptedException e) {}
        }
        // send message to arrival lounge interface, and wait for answer
        outMessage = new Message (Message.NO_BAGS_TO_COLLECT);   
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
        ClientCom con = new ClientCom(serverHostName, Global.baggageCollectionPointStub_PORT);
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