package clientSide;

import comInf.Message;
import global.*;

/**
 * This class defines the stub of the {@link serverSide.ArrivalLounge} in the AIRPORT RAPSODY that implements the 
 * client-server model (type 2) with static launch of the threads
 */
public class ArrivalLoungeStub{

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
    public ArrivalLoungeStub(String hostname, int port){
        serverHostName = "localhost";
        serverPortNumb = port;
    }

    /**
     * Passenger checks what should do (service request)
     * @param flight_number flight number
     * @param passengerID passenger identificartion
     * @param bags number of bags per flight
     * @param finalDestination final destination, or not
     * @return <li> 'a', if passenger should go home
     *         <li> 'b', if passenger should go collect his bags
     *         <li> 'c', if passenger should take a bus
     */
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
        con.writeObject(outMessage);
        // receive new in message, and process it
        inMessage = (Message) con.readObject();
        if (inMessage.getType() == Message.GOHOME && inMessage.getType() == Message.COLLECTBAG && inMessage.getType() == Message.TAKEBUS){ 
            System.out.println ("Thread " + p_thread.getName() + ": Invalid message type!");
            System.out.println (inMessage.toString());
            System.exit (1);
        }
        char res = 'z';
        if(inMessage.getType() == Message.GOHOME) res = 'a';
        else if(inMessage.getType() == Message.COLLECTBAG) res = 'b';
        else if(inMessage.getType() == Message.TAKEBUS) res = 'c';
        
        con.close ();
        return res;
    }
    /**
     * This method updates internal arrival lounge flight count (service request)
     * @param flight_number flight number
     */
	public void setFlight(int flight_number){
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
        outMessage = new Message (Message.SET_FLIGHT, flight_number);   
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
     * This methods signals Porter whether no take a rest or not (service request)
     * @return char 'W' Porter takes a rest, 'E' otherwise
     */
    public char takeARest(){
		// create connection
        ClientCom con = new ClientCom(serverHostName, Global.arrivalLoungeStub_PORT);
        Message inMessage, outMessage;
        Porter p_thread = (Porter) Thread.currentThread();
        while (!con.open ()){
            try{ 
                p_thread.sleep ((long) (10));
            }catch (InterruptedException e) {}
        }
        // send message to arrival lounge interface, and wait for answer
        outMessage = new Message (Message.REST);   
        con.writeObject (outMessage);
        // receive new in message, and process it
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.REST_Y && inMessage.getType () != Message.REST_N){ 
            System.out.println ("Thread " + p_thread.getName () + ": Invalid message type!");
            System.out.println (inMessage.toString ());
            System.exit (1);
        }
        
        char res = 'z';
        if(inMessage.getType() == Message.REST_Y) res = 'E';
        else if(inMessage.getType() == Message.REST_N) res = 'W';

        con.close ();
        return res;
    }
    /**
     * This methods signals Porter to go and try to collect a Bag (service request)
     * @return Bag
     */
    public Bag tryToCollectBag() {
        // create connection
        ClientCom con = new ClientCom(serverHostName, Global.arrivalLoungeStub_PORT);
        Message inMessage, outMessage;
        Porter p_thread = (Porter) Thread.currentThread();
        while (!con.open ()){
            try{ 
                p_thread.sleep ((long) (10));
            }catch (InterruptedException e) {}
        }
        // send message to arrival lounge interface, and wait for answer
        outMessage = new Message (Message.COLLECTBAG_PORTER);   
        con.writeObject (outMessage);
 
        // receive new in message, and process it
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.COLLECTBAG_PORTER){ 
            System.out.println ("Thread " + p_thread.getName () + ": Invalid message type!");
            System.out.println (inMessage.toString ());
            System.exit (1);
        }
        con.close ();
        return inMessage.get_Bag();
    }
    /**
     * Shutdown of the server (service request)
     */
    public void shutdown() {
        ClientCom con = new ClientCom(serverHostName, Global.arrivalLoungeStub_PORT);
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