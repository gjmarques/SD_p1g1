package serverSide;

import comInf.Message;
import comInf.MessageException;
import clientSide.*;
import global.Global;

/**
 * This type of data defines the interface to the {@link ArrivalLounge} in a
 * solution to the AIRPORT RHAPSODY' Problem that implements the type 2
 * client-server model (server replication) with static launch of the shared
 * region threads.
 */
public class ArrivalLoungeInterface {

    /** ArrivalLounge represents the service to be provided */
    private ArrivalLounge arrivalLounge;

    /**
     * Instantiation of the interface of the shared region {@link ArrivalLounge}.
     *
     * @param ArrivalLounge arrival lounge
     */
    public ArrivalLoungeInterface(ArrivalLounge arrivalLounge) {
        this.arrivalLounge = arrivalLounge;
    }

    /**
     * Processing of messages by executing the corresponding task. Generation of a
     * reply message.
     * 
     * @param inMessage incoming message with request
     * @return reply message
     * @throws MessageException if the message with the request is found to be
     *                          invalid
     */
    public Message processAndReply(Message inMessage) throws MessageException {

        Message outMessage = null;
        char res;

        /* validation of the received message */

        switch (inMessage.getType()) {
            case Message.SET_FLIGHT:
                if (inMessage.get_FlightCount() < 0 || inMessage.get_FlightCount() > Global.MAX_FLIGHTS) {
                    throw new MessageException("Invalid flight number!", inMessage);
                }
                break;
            case Message.WSID:
                if (inMessage.get_passengerID() < 0 || inMessage.get_passengerID() > Global.NR_PASSENGERS) {
                    throw new MessageException("Invalid passenger ID!", inMessage);
                } else if (inMessage.get_flight() < 0 || inMessage.get_flight() > Global.MAX_FLIGHTS) {
                    throw new MessageException("Invalid flight number!", inMessage);
                }
                break;
            case Message.REST:
                break;
            case Message.COLLECTBAG_PORTER:
                break;
            case Message.SHUT:
                break;
            default:
                throw new MessageException("Invalid message type!", inMessage);
        }

        /* message processing */
        switch (inMessage.getType()) {
            case Message.SET_FLIGHT:
                this.arrivalLounge.setFlight(inMessage.get_FlightCount());
                outMessage = new Message(Message.ACK);
                break;
            case Message.WSID:
                res = this.arrivalLounge.whatShouldIDo(inMessage.get_flight(), inMessage.get_passengerID(),
                        inMessage.get_bags(), inMessage.get_destination());
                switch (res) {
                    case 'a':
                        outMessage = new Message(Message.GOHOME);
                        break;
                    case 'b':
                        // reached final destination
                        outMessage = new Message(Message.COLLECTBAG);
                        break;
                    case 'c':
                        outMessage = new Message(Message.TAKEBUS);
                        break;
                }
                break;
            case Message.REST:
                res = this.arrivalLounge.takeARest();
                switch (res) {
                    case 'E':
                        outMessage = new Message(Message.REST_Y);
                        break;
                    case 'W':
                        // reached final destination
                        outMessage = new Message(Message.REST_N);
                        break;
                }
                break;
            case Message.COLLECTBAG_PORTER:
                Bag bag = this.arrivalLounge.tryToCollectBag();
                outMessage = new Message(Message.COLLECTBAG_PORTER, bag);
                break;
            case Message.SHUT:
                // server shutdown
                ALMain.waitConnection = false;
                (((ALProxy) (Thread.currentThread())).getScon()).setTimeout(10);
                // generate confirmation
                outMessage = new Message(Message.ACK);
                break;
        }
        return outMessage;
    }
}