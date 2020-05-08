package sharedRegions;

import comInf.Message;
import comInf.MessageException;

public class ArrivalLoungeInterface {

    private ArrivalLounge arrivalLounge;

    public ArrivalLoungeInterface(ArrivalLounge arrivalLounge){
        this.arrivalLounge = arrivalLounge;
    }

    public Message ProcessAndReply(Message inMessage, int passengerID) throws MessageException {
        Message outMessage = null;
        // process new inMessage, treat all exception

        // process new inMessage and answer server with new outMessage
        switch(inMessage.getType()){
            case Message.WSID:
                char res = this.arrivalLounge.whatShouldIDo(inMessage.get_nr_flight(), inMessage.get_passengerID(), inMessage.get_bags(), inMessage.get_finalDestination());
                switch(res){
                    try{
                        case 'a':
                            outMessage = new Message(Message.GOHOME);
                            break;
                        case 'b':
                            // reached final destination
                            outMessage= new Message(Message.COLLECTBAG);
                            break;
                        case 'c':
                            outMessage = new Message(Message.TAKEBUS);
                            break;
                     catch(Exception e){
                         e.exit(0);
                     }

                }
        }

    }
}
