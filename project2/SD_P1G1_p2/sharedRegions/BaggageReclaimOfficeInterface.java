package sharedRegions;

import comInf.*;
import sharedRegions.*;

public class BaggageReclaimOfficeInterface {

    BaggageReclaimOffice bro;

    public BaggageReclaimOfficeInterface(BaggageReclaimOffice bro){
        this.bro = bro;
    }


    public Message processAndReply(Message inMessage) throws MessageException{

        Message outMessage = null;

        switch(inMessage.getType()){
            case Message.REPORT_MISSING: 
                this.bro.reportMissingBags(inMessage.get_flight(), inMessage.get_passengerID());
                outMessage = new Message(Message.ACK);
            
        }
        return outMessage;
    }

}