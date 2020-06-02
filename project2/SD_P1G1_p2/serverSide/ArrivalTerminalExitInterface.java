package serverSide;

import comInf.*;

public class ArrivalTerminalExitInterface {

    private ArrivalTerminalExit arrivalTermExit;

    public ArrivalTerminalExitInterface(ArrivalTerminalExit arrivalTermExit){
        this.arrivalTermExit = arrivalTermExit;
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
            case Message.GOINGHOME:
                this.arrivalTermExit.goHome(inMessage.get_flight(), inMessage.get_passengerID(), inMessage.get_passengerState());
                outMessage = new Message(Message.ACK);
                break;
            case Message.SIGNAL_PASSENGER:
                this.arrivalTermExit.signalPassenger();
                outMessage = new Message(Message.ACK);
                break;
            case Message.SIGNAL_COMPLETION:
                this.arrivalTermExit.signalCompletion();
                outMessage = new Message(Message.ACK);
                break;
            case Message.SHUT:       
                // server shutdown                               
                ALMain.waitConnection = false;
                (((ALProxy) (Thread.currentThread ())).getScon ()).setTimeout (10);
                // generate confirmation
                outMessage = new Message (Message.ACK);        
                break;
           
        }
        return outMessage;
    }
}