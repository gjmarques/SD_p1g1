package serverSide;

import comInf.*;

public class GenInfoRepoInterface {

    GenInfoRepo genRepo;

    public GenInfoRepoInterface(GenInfoRepo genRepo){
        this.genRepo = genRepo;
    }


    public Message processAndReply(Message inMessage) throws MessageException{

        Message outMessage = null;

        switch(inMessage.getType()){
            case Message.BAGS_PL: 
                this.genRepo.nrBagsPlanesHold(inMessage.get_nrBagsPerFlight());
                outMessage = new Message(Message.ACK);
            case Message.BAGS_P_FLIGHT:
                this.genRepo.nrBagsPlanesHold(inMessage.get_nrBagsPerFlight());
                outMessage = new Message(Message.ACK);
            case Message.DEST:
                this.genRepo.countDest(inMessage.get_destination());
                outMessage = new Message(Message.ACK);
            case Message.INITP:
                this.genRepo.initPassenger(inMessage.get_flight(), inMessage.get_passengerID());
                outMessage = new Message(Message.ACK);
            case Message.LESSBAGS:
                this.genRepo.lessBagsOnPlanesHold(inMessage.get_Bag());
                outMessage = new Message(Message.ACK);
            case Message.PSGR_STATE:
                this.genRepo.passengerState(inMessage.get_flight(), inMessage.get_passengerID(), 
                    inMessage.get_passengerState(), inMessage.get_destination(), inMessage.get_nrBags());
                outMessage = new Message(Message.ACK);
            case Message.PSGR_UPDATE_STATE:
                this.genRepo.passengerState(inMessage.get_passengerID(), inMessage.get_passengerState());
                outMessage = new Message(Message.ACK);
            case Message.PSGR_UPDATE_STATE_ATE:
                this.genRepo.passengerState(inMessage.get_flight(), inMessage.get_passengerID(), inMessage.get_passengerState());
                outMessage = new Message(Message.ACK);
             case Message.PORTER_STATE:
                this.genRepo.porterState(inMessage.get_porterState());
                outMessage = new Message(Message.ACK);
            case Message.LEAVINGBUS:
                this.genRepo.leaveBus(inMessage.get_passengerID());
                outMessage = new Message(Message.ACK);
            case Message.CARRYTOAPPSTORE:
                this.genRepo.bagAtStoreRoom(inMessage.get_Bag());
                outMessage = new Message(Message.ACK);
            case Message.BUS_WAITNG_LINE:
                this.genRepo.busWaitingLine(inMessage.get_passengerID());
                outMessage = new Message(Message.ACK);
            case Message.BUS_SITTING:
                this.genRepo.busSitting(inMessage.get_passengerID());
                outMessage = new Message(Message.ACK);
            case Message.PSGR_COLLECTED_BAGS:
                this.genRepo.passengerCollectedBags(inMessage.get_passengerID(), inMessage.get_nrBags());
                outMessage = new Message(Message.ACK);
            case Message.COLLECTIONMAT_CONVBELT:
                this.genRepo.collectionMatConveyorBelt(inMessage.get_nrBags());
                outMessage = new Message(Message.ACK);
                
        }

        return outMessage;

    }

}