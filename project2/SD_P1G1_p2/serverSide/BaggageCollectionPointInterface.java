package serverSide;

import comInf.*;
import global.*;

/**
 * This type of data defines the interface to the {@link BaggageCollectionPoint} in a solution to the AIRPORT RHAPSODY' Problem that implements
 * the type 2 client-server model (server replication) with static launch of the shared region threads.
 */
public class BaggageCollectionPointInterface {
    
    /** BaggageCollectionPoint represents the service to be provided */
    private BaggageCollectionPoint baggageCollectionPoint;

    /**
    * Instantiation of the interface of the shared region {@link BaggageCollectionPoint}.
    *
    * @param baggageCollectionPoint baggage collection point
    */
    public BaggageCollectionPointInterface(BaggageCollectionPoint baggageCollectionPoint){
        this.baggageCollectionPoint = baggageCollectionPoint;
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
    public Message ProcessAndReply(Message inMessage) throws MessageException {
       
        Message outMessage = null;

        /* validation of the received message */
        switch(inMessage.getType()){
            case Message.GOCOLLECTBAG: 
                if(inMessage.get_passengerID() < 0 || inMessage.get_passengerID() > Global.NR_PASSENGERS){
                    throw new MessageException ("Invalid passenger ID!", inMessage);
                }
                break;
            case Message.CARRYTOAPPSTORE: 
                if(inMessage.get_Bag() == null){
                    throw new MessageException ("Bag not found!", inMessage);
                }
                break;
            case Message.NO_BAGS_TO_COLLECT:
                break;  
            case Message.SHUT:       
                // server shutdown 
                break;
            default:                              
                throw new MessageException ("Invalid message type!", inMessage);
        }
        /* message processing */
        switch(inMessage.getType()){
            case Message.GOCOLLECTBAG:
                int bag_id = this.baggageCollectionPoint.goCollectABag(inMessage.get_passengerID());
                outMessage = new Message(Message.BAG_COLLECTED, bag_id);
                break;
            case Message.CARRYTOAPPSTORE:
                this.baggageCollectionPoint.carryItToAppropriateStore(inMessage.get_Bag());
                outMessage = new Message(Message.ACK);
                break;
            case Message.NO_BAGS_TO_COLLECT:
                this.baggageCollectionPoint.noMoreBagsToCollect();
                outMessage = new Message(Message.ACK);
                break;       
            case Message.SHUT:       
                // server shutdown                               
                BCPMain.waitConnection = false;
                (((BCPProxy) (Thread.currentThread ())).getScon ()).setTimeout (10);
                // generate confirmation
                outMessage = new Message (Message.ACK);        
                break;     
        }
        return outMessage;
    }
}