package serverSide;

import comInf.*;
import global.Global;

/**
 * This type of data defines the interface to the {@link GenInfoRepo} in a
 * solution to the AIRPORT RHAPSODY' Problem that implements the type 2
 * client-server model (server replication) with static launch of the shared
 * region threads.
 */
public class GenInfoRepoInterface {

    /** GenInfoRepo represents the service to be provided */
    GenInfoRepo genRepo;

    /**
     * Instantiation of the interface of the shared region {@link GenInfoRepo}.
     *
     * @param genRepo general information repository
     */
    public GenInfoRepoInterface(GenInfoRepo genRepo) {
        this.genRepo = genRepo;
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

        /* validation of the received message */
        switch (inMessage.getType()) {
            case Message.BAGS_P_FLIGHT:
                break;
            case Message.DEST:
                if (inMessage.get_flight() < 0 || inMessage.get_flight() > Global.MAX_FLIGHTS) {
                    throw new MessageException("Invalid flight number!", inMessage);
                } else if (inMessage.get_passengerID() < 0 || inMessage.get_passengerID() > Global.NR_PASSENGERS) {
                    throw new MessageException("Invalid passenger ID!", inMessage);
                }
                break;
            case Message.INITP:
                break;
            case Message.LESSBAGS:
                if (inMessage.get_Bag() == null) {
                    throw new MessageException("Bag not found!", inMessage);
                }
                break;
            case Message.PSGR_STATE:
                if (inMessage.get_flight() < 0 || inMessage.get_flight() > Global.MAX_FLIGHTS) {
                    throw new MessageException("Invalid flight number!", inMessage);
                } else if (inMessage.get_passengerID() < 0 || inMessage.get_passengerID() > Global.NR_PASSENGERS) {
                    throw new MessageException("Invalid passenger ID!", inMessage);
                } else if (inMessage.get_nrBags() < 0) {
                    throw new MessageException("Invalid number of bags!", inMessage);
                }
                break;
            case Message.PSGR_UPDATE_STATE:
                if (inMessage.get_passengerID() > Global.NR_PASSENGERS || inMessage.get_passengerID() < 0) {
                    throw new MessageException("Invalid passenger ID!", inMessage);
                }
                break;
            case Message.PSGR_UPDATE_STATE_ATE:
                if (inMessage.get_flight() < 0 || inMessage.get_flight() > Global.MAX_FLIGHTS) {
                    throw new MessageException("Invalid flight number!", inMessage);
                } else if (inMessage.get_passengerID() < 0 || inMessage.get_passengerID() > Global.NR_PASSENGERS) {
                    throw new MessageException("Invalid passenger ID!", inMessage);
                }
                break;
            case Message.PORTER_STATE:
                break;
            case Message.LEAVINGBUS:
                if (inMessage.get_passengerID() < 0 || inMessage.get_passengerID() > Global.NR_PASSENGERS) {
                    throw new MessageException("Invalid passenger ID!", inMessage);
                }
                break;
            case Message.CARRYTOAPPSTORE:
                if (inMessage.get_Bag() == null) {
                    throw new MessageException("Bag not found!", inMessage);
                }
                break;
            case Message.BUS_WAITNG_LINE:
                if (inMessage.get_passengerID() < 0 || inMessage.get_passengerID() > Global.NR_PASSENGERS) {
                    throw new MessageException("Invalid passenger ID!", inMessage);
                }
                break;
            case Message.BUS_SITTING:
                if (inMessage.get_passengerID() < 0 || inMessage.get_passengerID() > Global.NR_PASSENGERS) {
                    throw new MessageException("Invalid passenger ID!", inMessage);
                }
                break;
            case Message.PSGR_COLLECTED_BAGS:
                if (inMessage.get_passengerID() < 0 || inMessage.get_passengerID() > Global.NR_PASSENGERS) {
                    throw new MessageException("Invalid passenger ID!", inMessage);
                } else if (inMessage.get_nrBags() < 0) {
                    throw new MessageException("Invalid number of bags collected!", inMessage);
                }
                break;
            case Message.COLLECTIONMAT_CONVBELT:
                if (inMessage.get_nrBags() < 0) {
                    throw new MessageException("Invalid number of bags!", inMessage);
                }
                break;
            case Message.BUSDRIVER_STATE:
                this.genRepo.busDriverState(inMessage.get_busDriverState());
                outMessage = new Message(Message.ACK);
                break;
            case Message.REPORT_MISSING_GIR:
                if (inMessage.get_nrBags() < 0) {
                    throw new MessageException("Invalid number of bags collected", inMessage);
                } else if (inMessage.get_passengerID() < 0 || inMessage.get_passengerID() > Global.NR_PASSENGERS) {
                    throw new MessageException("Invalid passenger ID!", inMessage);
                }
                break;
            case Message.SHUT:
                // shutdown do servidor
                break;
            default:
                throw new MessageException("Tipo inválido!", inMessage);
        }

        /* message processing */
        switch (inMessage.getType()) {
            case Message.BAGS_P_FLIGHT:
                this.genRepo.nrBagsPlanesHold(inMessage.get_nrBagsPerFlight());
                outMessage = new Message(Message.ACK);
                break;
            case Message.DEST:
                this.genRepo.countDest(inMessage.get_destination_passenger());
                outMessage = new Message(Message.ACK);
                break;
            case Message.INITP:
                this.genRepo.initPassenger(inMessage.get_flight(), inMessage.get_passengerID());
                outMessage = new Message(Message.ACK);
                break;
            case Message.LESSBAGS:
                this.genRepo.lessBagsOnPlanesHold(inMessage.get_Bag());
                outMessage = new Message(Message.ACK);
                break;
            case Message.PSGR_STATE:
                this.genRepo.passengerState(inMessage.get_flight(), inMessage.get_passengerID(),
                        inMessage.get_passengerState(), inMessage.get_FinalDestREPO(), inMessage.get_nrBags());
                System.out.println("GIR INT final destination passenger init: " + inMessage.get_FinalDestREPO());
                outMessage = new Message(Message.ACK);
                break;
            case Message.PSGR_UPDATE_STATE:
                this.genRepo.passengerState(inMessage.get_passengerID(), inMessage.get_passengerState());
                outMessage = new Message(Message.ACK);
                break;
            case Message.PSGR_UPDATE_STATE_ATE:
                this.genRepo.passengerState(inMessage.get_flight(), inMessage.get_passengerID(),
                        inMessage.get_passengerState());
                outMessage = new Message(Message.ACK);
                break;
            case Message.PORTER_STATE:
                this.genRepo.porterState(inMessage.get_porterState());
                outMessage = new Message(Message.ACK);
                break;
            case Message.LEAVINGBUS:
                this.genRepo.leaveBus(inMessage.get_passengerID());
                outMessage = new Message(Message.ACK);
                break;
            case Message.CARRYTOAPPSTORE:
                this.genRepo.bagAtStoreRoom(inMessage.get_Bag());
                outMessage = new Message(Message.ACK);
                break;
            case Message.BUS_WAITNG_LINE:
                this.genRepo.busWaitingLine(inMessage.get_passengerID());
                outMessage = new Message(Message.ACK);
                break;
            case Message.BUS_SITTING:
                this.genRepo.busSitting(inMessage.get_passengerID());
                outMessage = new Message(Message.ACK);
                break;
            case Message.PSGR_COLLECTED_BAGS:
                this.genRepo.passengerCollectedBags(inMessage.get_passengerID(), inMessage.get_nrBags());
                outMessage = new Message(Message.ACK);
                break;
            case Message.COLLECTIONMAT_CONVBELT:
                this.genRepo.collectionMatConveyorBelt(inMessage.get_nrBags());
                outMessage = new Message(Message.ACK);
                break;
            case Message.BUSDRIVER_STATE:
                this.genRepo.busDriverState(inMessage.get_busDriverState());
                outMessage = new Message(Message.ACK);
                break;
            case Message.REPORT_MISSING_GIR:
                this.genRepo.missingBags(inMessage.get_numLostBags(), inMessage.get_passengerID());
                outMessage = new Message(Message.ACK);
                break;
            case Message.SHUT:
                // server shutdown
                GIRMain.waitConnection = false;
                (((GIRProxy) (Thread.currentThread())).getScon()).setTimeout(10);
                // generate confirmation
                outMessage = new Message(Message.ACK);
                break;
        }

        return outMessage;

    }

}