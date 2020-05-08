package comInf;

import java.io.*;

public class Message implements Serializable {

    /**
     *  Chave de serialização
    */
    private static final long serialVersionUID = 1001L;

    /* Tipos das mensagens */
    /**
     *  What should I do request
    */
    public static final int WSID  =  -1;

    public static final int GOHOME  =  2;
    public static final int TAKEBUS  =  3;
    public static final int COLLECTBAG  =  4;
    
    /* Other variables */
    /**
     * What should I do option
     */
    public static final char WSID_ANSWER = 'z';



    /* Campos das mensagens */
    /**
     *  Message type
    */
    private int msgType;
    /**
     * Passenger identification
     */
    private int passengerID;

    /**
     *  Message type 1
     *
     *    @param type message type
     *    @param id passenger identification
     */ 
    public Message (int type, int id)
    {
        msgType = type;
        if ((msgType == WSID)){
            this.passengerID = id;
        }
    }


    /**
     * Get message type
     * @return int msgType
     */
    public int getType (){
        return (msgType);
    }

    /**
     * Get passengerID
     * @return int passengerID
     */
    public int get_passengerID(){
        return(passengerID);
    }

    /**
     * Get what should i do answer
     * @return int what should i do option
     */
    public char get_WSID (){
        return (WSID_ANSWER);
    }

}

