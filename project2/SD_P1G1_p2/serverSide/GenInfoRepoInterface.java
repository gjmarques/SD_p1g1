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
        }

        return outMessage;

    }

}