package clientSide;

import global.*;
import comInf.Message;

public class DepartureTermTransfQuayStub{

    private String serverHostName = null;
    private int serverPortNumb;

    public DepartureTermTransfQuayStub(String hostname, int port){
        serverHostName = hostname;
        serverPortNumb = port;
    }


    public void leaveTheBus(int passengerID) {
        // create connection
        ClientCom con = new ClientCom(serverHostName, Global.departureTermTransfQuayStub_PORT);
        Message inMessage, outMessage;
        Passenger p_thread = (Passenger) Thread.currentThread();
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
    public void goToDepartureTerminal(){
        // create connection
        ClientCom con = new ClientCom(serverHostName, Global.departureTermTransfQuayStub_PORT);
        Message inMessage, outMessage;
        BusDriver p_thread = (BusDriver) Thread.currentThread();
        while (!con.open ()){
            try{ 
                p_thread.sleep ((long) (10));
            }catch (InterruptedException e) {}
        }
        // send message to arrival lounge interface, and wait for answer
        outMessage = new Message (Message.GOTO_DTTQ);   
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
    public void parkTheBusAndLetPassengerOff(int busPassengers) {
        // create connection
        ClientCom con = new ClientCom(serverHostName, Global.departureTermTransfQuayStub_PORT);
        Message inMessage, outMessage;
        BusDriver p_thread = (BusDriver) Thread.currentThread();
        while (!con.open ()){
            try{ 
                p_thread.sleep ((long) (10));
            }catch (InterruptedException e) {}
        }
        // send message to arrival lounge interface, and wait for answer
        outMessage = new Message (Message.PARKBUS, busPassengers);   
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