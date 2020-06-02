package serverSide;

import comInf.*;

public class DepartureTerminalEntranceInterface {

    DepartureTerminalEntrance departureTermEntrance;

    public DepartureTerminalEntranceInterface(DepartureTerminalEntrance dte){
        this.departureTermEntrance = dte;
    }


    public Message processAndReply(Message inMessage) throws MessageException{

        Message outMessage = null;

        switch(inMessage.getType()){
            case Message.PNEXTLEG: 
                this.departureTermEntrance.prepareNextLeg(inMessage.get_passengerID(), inMessage.get_flight());
                outMessage = new Message(Message.ACK);
                break;
            case Message.SIGNAL_PASSENGER:
                this.departureTermEntrance.signalPassenger();
                outMessage = new Message(Message.ACK);
                break;
            case Message.SIGNAL_COMPLETION:
                this.departureTermEntrance.signalCompletion();
                outMessage = new Message(Message.ACK);
                break;
            case Message.SHUT:       
                // server shutdown                               
                DTEMain.waitConnection = false;
                (((DTEProxy) (Thread.currentThread ())).getScon ()).setTimeout (10);
                // generate confirmation
                outMessage = new Message (Message.ACK);        
                break;
            
        }
        return outMessage;
    }
}