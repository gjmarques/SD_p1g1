package sharedRegions;

import comInf.Message;
import mainProgram.*;

public class BaggageReclaimOfficeStub{

    private String serverHostName = null;
    private int serverPortNumb;

    public BaggageReclaimOfficeStub(String hostname, int port){
        serverHostName = hostname;
        serverPortNumb = port;
    }
    
    public void reportMissingBags(int i, int passengerID) {
        // create connection
        ClientCom con = new ClientCom(serverHostName, Global.arrivalLoungeStub_PORT);
        Message inMessage, outMessage;
        Thread p_thread = Thread.currentThread();
        while (!con.open ()){
            try{ 
                p_thread.sleep ((long) (10));
            }catch (InterruptedException e) {}
        }
        // send message to arrival lounge interface, and wait for answer
        outMessage = new Message (Message.REPORT_MISSING, passengerID, i);   
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