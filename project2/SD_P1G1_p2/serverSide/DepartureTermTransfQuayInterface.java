package serverSide;

import comInf.*;
import global.Global;

/**
 * This type of data defines the interface to the {@link DepartureTermTransfQuay} in a solution to the AIRPORT RHAPSODY' Problem that implements
 * the type 2 client-server model (server replication) with static launch of the shared region threads.
 */
public class DepartureTermTransfQuayInterface {

    /** DepartureTermTransfQuay represents the service to be provided */
    DepartureTermTransfQuay departureTermTransfQuay;

    /**
    * Instantiation of the interface of the shared region {@link DepartureTermTransfQuay}.
    *
    * @param dttq departure terminal transfer quay
    */
    public DepartureTermTransfQuayInterface(DepartureTermTransfQuay dttq){
        this.departureTermTransfQuay = dttq;
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
            case Message.LEAVINGBUS: 
                if(inMessage.get_passengerID() < 0 || inMessage.get_passengerID() > Global.NR_PASSENGERS){
                    throw new MessageException ("Invalid passenger ID!", inMessage);
                }
                break;
            case Message.GOTO_DTTQ:
                break;
            case Message.PARKBUS:
                break;
            case Message.SHUT:       
                // server shutdown 
                break;
            default:                              
                throw new MessageException ("Invalid message type!", inMessage);
        }
        
        /* message processing */
        switch(inMessage.getType()){
            case Message.LEAVINGBUS: 
                this.departureTermTransfQuay.leaveTheBus(inMessage.get_passengerID());
                outMessage = new Message(Message.ACK);
                break;
            case Message.GOTO_DTTQ:
                this.departureTermTransfQuay.goToDepartureTerminal();
                outMessage = new Message(Message.ACK);
                break;
            case Message.PARKBUS:
                this.departureTermTransfQuay.parkTheBusAndLetPassengerOff(inMessage.get_BusPassengers());
                outMessage = new Message(Message.ACK);
                break;
            case Message.SHUT:       
                // server shutdown                               
                DTTQMain.waitConnection = false;
                (((DTTQProxy) (Thread.currentThread ())).getScon ()).setTimeout (10);
                // generate confirmation
                outMessage = new Message (Message.ACK);        
                break;
        }
        return outMessage;
    }

}