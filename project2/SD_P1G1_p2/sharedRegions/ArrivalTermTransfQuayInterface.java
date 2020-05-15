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
        char res;
        int  res_int;

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
            case Message.WORK_END:
                res = this.arrivalTermTransfQuay.hasDaysWorkEnded();
                switch(res){
                    case 'E':
                        // work days ended for bus driver
                        outMessage = new Message(Message.WORK_ENDED);
                    case 'W':
                        // work days are not over
                        outMessage = new Message(Message.WORK_NOT_ENDED);
                    default:
                        //something went wrong
                }
                outMessage = new Message(Message.ACK);
            case Message.BUSBOARD:
                res_int = this.arrivalTermTransfQuay.annoucingBusBoarding();
                outMessage = new Message(Message.BUSBOARD, res_int);
            case Message.GOTO_ATTQ:
                this.arrivalTermTransfQuay.goToArrivalTerminal();
                outMessage = new Message(Message.ACK);
            case Message.PARK:
                this.arrivalTermTransfQuay.parkTheBus();
                outMessage = new Message(Message.ACK);
            case Message.D_TIME:
                this.arrivalTermTransfQuay.departureTime();
                outMessage = new Message(Message.ACK);
        }
        return outMessage;
    }

}