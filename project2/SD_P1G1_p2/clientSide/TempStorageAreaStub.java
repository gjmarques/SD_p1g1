package clientSide;

import global.*;

import comInf.Message;

public class TempStorageAreaStub{

    private String serverHostName = null;
    private int serverPortNumb;

    public TempStorageAreaStub(String hostname, int port){
        serverHostName = hostname;
        serverPortNumb = port;
    }
    /**
     * This method transforms the request into a message to signal Porter to carry a bag to the appropriate storage
     */
    public void carryItToAppropriateStore(Bag bag) {
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

}