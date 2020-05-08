package sharedRegions;

import comInf.Message;
import mainProgram.*;

public class ArrivalLoungeStub{

    private String serverHostName = null;
    private int serverPortNumb;

    public ArrivalLoungeStub(String hostname, int port){
        serverHostName = hostname;
        serverPortNumb = port;
    }

    char whatShouldIDo(int flight_number, int passengerID, Bag[] bags, boolean finalDestination){

        // create connection
        ClientCom con = new ClientCom(serverHostName, Global.arrivalLoungeStub_PORT);
        Message inMessage, outMessage;
        // create new out message
        while (!con.open ()){
            try{ 
                Thread.currentThread ().sleep ((long) (10));
            }catch (InterruptedException e) {}
        }
        // send message to arrival lounge interface, and wait for answer
        outMessage = new Message (Message.WSID, passengerID);   
        con.writeObject (outMessage);
 
        // receive new in message, and process it
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.WSID_ANSWER){ 
            System.out.println ("Thread " + Thread.currentThread ().getName () + ": Tipo inválido!");
            System.out.println (inMessage.toString ());
            System.exit (1);
        }
     con.close ();

     return inMessage.get_WSID();

    }
}