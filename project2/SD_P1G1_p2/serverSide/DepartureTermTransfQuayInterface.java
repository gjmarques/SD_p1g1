package serverSide;

import comInf.*;

public class DepartureTermTransfQuayInterface {

    DepartureTermTransfQuay departureTermTransfQuay;

    public DepartureTermTransfQuayInterface(DepartureTermTransfQuay dttq){
        this.departureTermTransfQuay = dttq;
    }


    public Message processAndReply(Message inMessage) throws MessageException{

        Message outMessage = null;

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