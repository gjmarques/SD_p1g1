package sharedRegions;

import comInf.*;
import sharedRegions.*;

public class ArrivalTermTransfQuayInterface {

    ArrivalTermTransfQuay arrivalTermTransfQuay;

    public ArrivalTermTransfQuayInterface(ArrivalTermTransfQuay attq){
        this.arrivalTermTransfQuay = attq;
    }


    public Message processAndReply(Message inMessage) throws MessageException{

        Message outMessage = null;

        switch(inMessage.getType()){
            case Message.SET_FLIGHT_attq: 
                this.arrivalTermTransfQuay.setFlight(inMessage.get_setFlightCount_attq());
                outMessage = new Message(Message.ACK);
            case Message.TAKINGBUS:
                this.arrivalTermTransfQuay.takeABus(inMessage.get_passengerID());
                outMessage = new Message(Message.ACK);
            case Message.ENTERINGBUS:
                this.arrivalTermTransfQuay.enterTheBus(inMessage.get_passengerID());
                outMessage = new Message(Message.ACK);
        }
        return outMessage;
    }

}