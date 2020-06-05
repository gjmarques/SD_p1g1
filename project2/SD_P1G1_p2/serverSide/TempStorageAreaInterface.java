package serverSide;

import comInf.*;

/**
 * This type of data defines the interface to the {@link TempStorageArea} in a solution to the AIRPORT RHAPSODY' Problem that implements
 * the type 2 client-server model (server replication) with static launch of the shared region threads.
 */
public class TempStorageAreaInterface {
    
    /** ArrivalTermTransfQuay represents the service to be provided */
    private TempStorageArea tempStorageArea;

    /**
    * Instantiation of the interface of the shared region {@link TempStorageArea}.
    *
    * @param tempStorageArea temporary storage area
    */
    public TempStorageAreaInterface(TempStorageArea tempStorageArea){
        this.tempStorageArea = tempStorageArea;
    }

    /**
     * Processing of messages by executing the corresponding task.
     * Generation of a reply message.
     * 
     * @param inMessage incoming message with request
     * @return reply message
     * @throws MessageException if the message with the request is found to be invalid
     */
    public Message processAndReply(Message inMessage) throws MessageException {
       
        Message outMessage = null;
        
        /* validation of the received message */
        switch (inMessage.getType ()){
            case Message.CARRYTOAPPSTORE:  
                if ((inMessage.get_Bag() == null))
                    throw new MessageException ("Bag not found!", inMessage);
                break;
            case Message.SHUT:                                                        
                // shutdown do servidor
                break;
            default:               
                throw new MessageException ("Tipo inválido!", inMessage);
        }
        /* message processing */
        switch(inMessage.getType()){
            case Message.CARRYTOAPPSTORE:
                this.tempStorageArea.carryItToAppropriateStore(inMessage.get_Bag());
                outMessage = new Message(Message.ACK);
                break;
            case Message.SHUT:       
                // server shutdown                               
                TSAMain.waitConnection = false;
                (((TSAProxy) (Thread.currentThread ())).getScon ()).setTimeout (10);
                // generate confirmation
                outMessage = new Message (Message.ACK);        
                break;
        }
        return outMessage;
    }
}