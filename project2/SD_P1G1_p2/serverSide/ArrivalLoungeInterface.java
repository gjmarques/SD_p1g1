package serverSide;

import comInf.Message;
import comInf.MessageException;
import clientSide.*;

public class ArrivalLoungeInterface {

    private ArrivalLounge arrivalLounge;

    public ArrivalLoungeInterface(ArrivalLounge arrivalLounge){
        this.arrivalLounge = arrivalLounge;
    }
    /**
     * Processing of messages by executing the corresponding task.
     * Generation of a reply message.
     * 
     * @param inMessage incoming message with request
     * @return reply message
     * @throws MessageException if the message with the request is found to be invalid
     */
    public Message processAndReply(Message inMessage) throws MessageException {
       
        Message outMessage = null;
        char res;

        // process message arguments by type and throw MessageException if necessary
        // TODO
        
        // process new inMessage and answer server with new outMessage
        switch(inMessage.getType()){
            case Message.SET_FLIGHT:
                this.arrivalLounge.setFlight(inMessage.get_FlightCount());
                outMessage = new Message(Message.ACK);
                break;
            case Message.WSID:
                res = this.arrivalLounge.whatShouldIDo(inMessage.get_flight(), inMessage.get_passengerID(), inMessage.get_bags(), inMessage.get_destination());
                try{ 
                    switch(res){
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
                    }
                }catch(Exception e){
                        //System.exit(0);
                }
                break;
            case Message.REST:
            
                res = this.arrivalLounge.takeARest();
                switch(res){
                    case 'E':
                        outMessage = new Message(Message.REST_Y);
                        break;
                    case 'W':
                        // reached final destination
                        outMessage= new Message(Message.REST_N);
                        break;
                }
                break;
            case Message.COLLECTBAG_PORTER:
                Bag bag = this.arrivalLounge.tryToCollectBag();
                outMessage = new Message(Message.COLLECTBAG_PORTER, bag);
                break;
            case Message.SHUT:       
                // server shutdown                               
                ALMain.waitConnection = false;
                (((ALProxy) (Thread.currentThread ())).getScon ()).setTimeout (10);
                // generate confirmation
                outMessage = new Message (Message.ACK);        
                break;
        }
        return outMessage;
    }
}
