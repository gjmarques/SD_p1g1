package clientSide;

import global.*;
import comInf.Message;

public class GenInfoRepoStub{

    private String serverHostName = "localhost";
    private int serverPortNumb;

    public GenInfoRepoStub(String hostname, int port){
        serverHostName = "localhost";
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
     * Initiate passenger in {@link sharedRegions.GenInfoRep} (service request).
     * @param flight_nr Passenger flight number
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
    public void passengerState(int nPlane, int passengerID, PassengerState passengerState){
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
        outMessage = new Message (Message.PSGR_UPDATE_STATE_ATE, nPlane, passengerID, passengerState);   
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
    public   void bagAtStoreRoom(Bag bag){
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