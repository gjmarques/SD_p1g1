package serverSide;

import comInf.*;
import global.Global;

/**
 * This type of data defines the interface to the {@link ArrivalTermTransfQuay}
 * in a solution to the AIRPORT RHAPSODY' Problem that implements the type 2
 * client-server model (server replication) with static launch of the shared
 * region threads.
 */
public class ArrivalTermTransfQuayInterface {

    /** ArrivalTermTransfQuay represents the service to be provided */
    ArrivalTermTransfQuay arrivalTermTransfQuay;

    /**
     * Instantiation of the interface of the shared region
     * {@link ArrivalTermTransfQuay}.
     *
     * @param attq arrival terminal transfer quay
     */
    public ArrivalTermTransfQuayInterface(ArrivalTermTransfQuay attq) {
        this.arrivalTermTransfQuay = attq;
    }

    /**
     * Processing of messages by executing the corresponding task. Generation of a
     * reply message.
     *
     * @param inMessage message with the request
     *
     * @return reply message
     *
     * @throws MessageException if the message with the request is considered
     *                          invalid
     */
    public Message processAndReply(Message inMessage) throws MessageException {

        Message outMessage = null;
        char res;
        int res_int;

        /* validation of the received message */
        switch (inMessage.getType()) {
            case Message.SET_FLIGHT:
                if (inMessage.get_FlightCount() < 0 || inMessage.get_FlightCount() > Global.MAX_FLIGHTS) {
                    throw new MessageException("Invalid flight number!", inMessage);
                }
                break;
            case Message.TAKINGBUS:
                if (inMessage.get_passengerID() < 0 || inMessage.get_passengerID() > Global.NR_PASSENGERS) {
                    throw new MessageException("Invalid passenger ID!", inMessage);
                }
                break;
            case Message.ENTERINGBUS:
                if (inMessage.get_passengerID() < 0 || inMessage.get_passengerID() > Global.NR_PASSENGERS) {
                    throw new MessageException("Invalid passenger ID!", inMessage);
                }
                break;
            case Message.WORK_END:
                break;
            case Message.BUSBOARD:
                break;
            case Message.GOTO_ATTQ:
                break;
            case Message.PARK:
                break;
            case Message.D_TIME:
                break;
            case Message.SHUT: // shutdown do servidor
                break;
            default:
                throw new MessageException("Invalid message type!", inMessage);
        }

        /* message processing */
        switch (inMessage.getType()) {
            case Message.SET_FLIGHT:
                this.arrivalTermTransfQuay.setFlight(inMessage.get_FlightCount());
                outMessage = new Message(Message.ACK);
                break;
            case Message.TAKINGBUS:
                this.arrivalTermTransfQuay.takeABus(inMessage.get_passengerID());
                outMessage = new Message(Message.ACK);
                break;
            case Message.ENTERINGBUS:
                this.arrivalTermTransfQuay.enterTheBus(inMessage.get_passengerID());
                outMessage = new Message(Message.ACK);
                break;
            case Message.WORK_END:
                res = this.arrivalTermTransfQuay.hasDaysWorkEnded();
                switch (res) {
                    case 'E':
                        // work days ended for bus driver
                        outMessage = new Message(Message.WORK_ENDED);
                        break;
                    case 'W':
                        // work days are not over
                        outMessage = new Message(Message.WORK_NOT_ENDED);
                        break;
                    case 'z':
                        // something went wrong
                        outMessage = new Message(Message.WORK_ENDED);
                        break;
                }
                break;
            case Message.BUSBOARD:
                res_int = this.arrivalTermTransfQuay.annoucingBusBoarding();
                outMessage = new Message(Message.BUSBOARD, res_int);
                break;
            case Message.GOTO_ATTQ:
                this.arrivalTermTransfQuay.goToArrivalTerminal();
                outMessage = new Message(Message.ACK);
                break;
            case Message.PARK:
                this.arrivalTermTransfQuay.parkTheBus();
                outMessage = new Message(Message.ACK);
                break;
            case Message.D_TIME:
                this.arrivalTermTransfQuay.departureTime();
                outMessage = new Message(Message.ACK);
                break;
            case Message.SHUT:
                // server shutdown
                ATTQMain.waitConnection = false;
                (((ATTQProxy) (Thread.currentThread())).getScon()).setTimeout(10);
                // generate confirmation
                outMessage = new Message(Message.ACK);
                break;
        }
        return outMessage;
    }

}