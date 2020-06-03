package serverSide;

import comInf.*;

public class TempStorageAreaInterface {
    
    private TempStorageArea tempStorageArea;

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
        //char res;

        // process message arguments by type and throw MessageException if necessary
        // TODO
        
        // process new inMessage and answer server with new outMessage
        switch(inMessage.getType()){
            case Message.CARRYTOAPPSTORE:
                this.tempStorageArea.carryItToAppropriateStore(inMessage.get_Bag());
                outMessage = new Message(Message.ACK);

        }
        return outMessage;
    }
}