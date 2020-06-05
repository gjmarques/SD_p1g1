package serverSide;

import comInf.*;
import global.*;

/**
 * This type of data defines the interface to the {@link BaggageReclaimOffice} in a solution to the AIRPORT RHAPSODY' Problem that implements
 * the type 2 client-server model (server replication) with static launch of the shared region threads.
 */
public class BaggageReclaimOfficeInterface {

    /** BaggageReclaimOffice represents the service to be provided */
    BaggageReclaimOffice bro;

    /**
    * Instantiation of the interface of the shared region {@link BaggageReclaimOffice}.
    *
    * @param bro baggage reclaim office
    */
    public BaggageReclaimOfficeInterface(BaggageReclaimOffice bro){
        this.bro = bro;
    }

    /**
    * Processing of messages by executing the corresponding task.
    * Generation of a reply message.
    *
    * @param inMessage message with the request
    *
    * @return reply message
    *
    * @throws MessageException if the message with the request is considered invalid
    */
    public Message processAndReply(Message inMessage) throws MessageException{

        Message outMessage = null;

        /* validation of the received message */
        switch(inMessage.getType()){
            case Message.REPORT_MISSING: 
                if(inMessage.get_passengerID() < 0 || inMessage.get_passengerID() > Global.NR_PASSENGERS){
                    throw new MessageException ("Invalid passenger ID!", inMessage);
                }else if(inMessage.get_numLostBags() < 0 ){
                    throw new MessageException ("Invalid number of lost bags!", inMessage);
                }
                break;
            case Message.SHUT:       
                // server shutdown 
                break;
            default:                              
                throw new MessageException ("Invalid message type!", inMessage);
        }
        /* message processing */
        switch(inMessage.getType()){
            case Message.REPORT_MISSING: 
                this.bro.reportMissingBags(inMessage.get_numLostBags(), inMessage.get_passengerID());
                outMessage = new Message(Message.ACK);
                break;
            case Message.SHUT:       
                // server shutdown                               
                BROMain.waitConnection = false;
                (((BROProxy) (Thread.currentThread ())).getScon ()).setTimeout (10);
                // generate confirmation
                outMessage = new Message (Message.ACK);        
                break;
        }
        return outMessage;
    }

}