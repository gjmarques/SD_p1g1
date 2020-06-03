package serverSide;

import comInf.*;

public class BaggageCollectionPointInterface {
    
    private BaggageCollectionPoint baggageCollectionPoint;

    public BaggageCollectionPointInterface(BaggageCollectionPoint baggageCollectionPoint){
        this.baggageCollectionPoint = baggageCollectionPoint;
    }

     /**
     * Processing of messages by executing the corresponding task.
     * Generation of a reply message.
     * 
     * @param inMessage incoming message with request
     * @param passengerID passenger identification
     * @return reply message
     * @throws MessageException if the message with the request is found to be invalid
     */
    public Message ProcessAndReply(Message inMessage) throws MessageException {
       
        Message outMessage = null;

        // process message arguments by type and throw MessageException if necessary
        // TODO
        
        // process new inMessage and answer server with new outMessage
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