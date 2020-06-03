package serverSide;

import comInf.*;

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
                switch(res){
                    case 'E':
                        // work days ended for bus driver
                        outMessage = new Message(Message.WORK_ENDED);
                        break;
                    case 'W':
                        // work days are not over
                        outMessage = new Message(Message.WORK_NOT_ENDED);
                        break;
                    case 'z':
                        //something went wrong
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
                (((ATTQProxy) (Thread.currentThread ())).getScon ()).setTimeout (10);
                // generate confirmation
                outMessage = new Message (Message.ACK);
                break;  
        }
        return outMessage;
    }

}