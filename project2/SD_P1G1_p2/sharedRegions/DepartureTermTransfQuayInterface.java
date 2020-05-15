package sharedRegions;

import comInf.*;
import sharedRegions.*;

public class DepartureTermTransfQuayInterface {

    DepartureTermTransfQuay departureTermTransfQuay;

    public DepartureTermTransfQuayInterface(DepartureTermTransfQuay dttq){
        this.departureTermTransfQuay = dttq;
    }


    public Message processAndReply(Message inMessage) throws MessageException{

        Message outMessage = null;

        switch(inMessage.getType()){
            case Message.PNEXTLEG: 
                this.departureTermTransfQuay.leaveTheBus(inMessage.get_passengerID());
                outMessage = new Message(Message.ACK);
            case Message.GOTO_DTTQ:
                this.departureTermTransfQuay.goToDepartureTerminal();
                outMessage = new Message(Message.ACK);
            case Message.PARKBUS:
                this.departureTermTransfQuay.parkTheBusAndLetPassengerOff(inMessage.get_BusPassengers());
                outMessage = new Message(Message.ACK);
            
        }
        return outMessage;
    }

}