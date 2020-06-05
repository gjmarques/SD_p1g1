package serverSide;

import comInf.*;
import global.Global;

/**
 * This type of data defines the interface to the {@link DepartureTerminalEntrance} in a solution to the AIRPORT RHAPSODY' Problem that implements
 * the type 2 client-server model (server replication) with static launch of the shared region threads.
 */
public class DepartureTerminalEntranceInterface {

    /** DepartureTerminalEntrance represents the service to be provided */
    DepartureTerminalEntrance departureTermEntrance;

    /**
    * Instantiation of the interface of the shared region {@link DepartureTerminalEntrance}.
    *
    * @param dte departure terminal entrance
    */
    public DepartureTerminalEntranceInterface(DepartureTerminalEntrance dte){
        this.departureTermEntrance = dte;
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
            case Message.PNEXTLEG: 
                if(inMessage.get_passengerID() < 0 || inMessage.get_passengerID() > Global.NR_PASSENGERS){
                    throw new MessageException ("Invalid passenger ID!", inMessage);
                }else if(inMessage.get_flight() < 0 || inMessage.get_flight() > Global.MAX_FLIGHTS){
                    throw new MessageException ("Invalid flight number!", inMessage);
                }
                break;
            case Message.SIGNAL_PASSENGER:
                break;
            case Message.SIGNAL_COMPLETION:
                break;
            case Message.SHUT:       
                // server shutdown 
                break;
            default:                              
                throw new MessageException ("Invalid message type!", inMessage);
        }
        /* message processing */
        switch(inMessage.getType()){
            case Message.PNEXTLEG: 
                this.departureTermEntrance.prepareNextLeg(inMessage.get_passengerID(), inMessage.get_flight());
                outMessage = new Message(Message.ACK);
                break;
            case Message.SIGNAL_PASSENGER:
                this.departureTermEntrance.signalPassenger();
                outMessage = new Message(Message.ACK);
                break;
            case Message.SIGNAL_COMPLETION:
                this.departureTermEntrance.signalCompletion();
                outMessage = new Message(Message.ACK);
                break;
            case Message.SHUT:       
                // server shutdown                               
                DTEMain.waitConnection = false;
                (((DTEProxy) (Thread.currentThread ())).getScon ()).setTimeout (10);
                // generate confirmation
                outMessage = new Message (Message.ACK);        
                break;
            
        }
        return outMessage;
    }
}