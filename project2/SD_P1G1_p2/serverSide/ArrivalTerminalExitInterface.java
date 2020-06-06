package serverSide;

import comInf.*;
import global.*;

/**
 * This type of data defines the interface to the {@link ArrivalTerminalExit} in a solution to the AIRPORT RHAPSODY' Problem that implements
 * the type 2 client-server model (server replication) with static launch of the shared region threads.
 */
public class ArrivalTerminalExitInterface {

    /** ArrivalTerminalExit represents the service to be provided */
    private ArrivalTerminalExit arrivalTermExit;

    /**
    * Instantiation of the interface of the shared region {@link ArrivalTerminalExit}.
    *
    * @param arrivalTermExit arrival terminal exit
    */
    public ArrivalTerminalExitInterface(ArrivalTerminalExit arrivalTermExit){
        this.arrivalTermExit = arrivalTermExit;
    }
    /**
     * Processing of messages by executing the corresponding task.
     * Generation of a reply message.
     * 
     * @param inMessage incoming message with request
     * @return reply message
     * @throws MessageException if the message with the request is found to be invalid
     */
    public Message ProcessAndReply(Message inMessage) throws MessageException {
       
        Message outMessage = null;
        
        /* validation of the received message */
        switch (inMessage.getType ()){ 
            case Message.GOINGHOME: 
                if(inMessage.get_flight() < 0 || inMessage.get_flight() > Global.MAX_FLIGHTS){
                    throw new MessageException ("Invalid flight number!", inMessage);
                }else if(inMessage.get_passengerID() < 0 || inMessage.get_passengerID() > Global.NR_PASSENGERS){
                    throw new MessageException ("Invalid passenger ID!", inMessage);
                }
                break;
            case Message.SIGNAL_PASSENGER:
                break;
            case Message.SIGNAL_COMPLETION:
                break;
            case Message.SHUT:                                                        // shutdown do servidor
                break;
            default:               
                throw new MessageException ("Invalid message type!", inMessage);
        }
        
        /* message processing */
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
                ATEMain.waitConnection = false;
                (((ATEProxy) (Thread.currentThread ())).getScon ()).setTimeout (10);
                // generate confirmation
                outMessage = new Message (Message.ACK);        
                break;
           
        }
        return outMessage;
    }
}