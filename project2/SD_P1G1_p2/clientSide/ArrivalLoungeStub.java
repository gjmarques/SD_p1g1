package clientSide;

import comInf.Message;
import global.*;

public class ArrivalLoungeStub{

    private String serverHostName = "localhost";
    private int serverPortNumb;

    public ArrivalLoungeStub(String hostname, int port){
        serverHostName = "localhost";
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
        if (inMessage.getType () != Message.GOHOME || inMessage.getType () != Message.COLLECTBAG || inMessage.getType () != Message.TAKEBUS){ 
            System.out.println ("Thread " + p_thread.getName () + ": Invalid message type!");
            System.out.println (inMessage.toString ());
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
     * This method updates internal arrival lounge flight count.
     * @param flight_number 
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
        outMessage = new Message (Message.SET_FLIGHT_al, flight_number);   
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
     * This methods signals Porter whether no take a rest or not
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
        if (inMessage.getType () != Message.REST_Y || inMessage.getType () != Message.REST_N){ 
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
     * This methods signals Porter to go and try to collect a Bag
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
}