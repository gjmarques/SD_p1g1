package serverSide;

import comInf.*;

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
                break;
            case Message.SHUT:       
                // server shutdown                               
                BROMain.waitConnection = false;
                (((BROProxy) (Thread.currentThread ())).getScon ()).setTimeout (10);
                // generate confirmation
                outMessage = new Message (Message.ACK);        
                break;
        }
        return outMessage;
    }

}