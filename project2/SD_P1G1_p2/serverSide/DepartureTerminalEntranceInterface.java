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
            
        }
        return outMessage;
    }
}