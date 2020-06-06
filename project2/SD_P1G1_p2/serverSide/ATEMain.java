package serverSide;

import java.net.SocketTimeoutException;

import clientSide.*;
import global.*;

/**
 * This type of data simulates a server-side solution to the Airport Rapsody' Problem that implements the
 * type 2 client-server model (server replication) with static launch of the shared regions threads.
 * Communication is based on passing messages over sockets using the TCP protocol.
 */
public class ATEMain {

    /**
     * activity signaling
     */
    public static boolean waitConnection;  

    public static void main(String[] args) {

        /**
         * Represents the service to be provided
         */
        ArrivalTerminalExit ate;                  
        /**
         * Arrival Terminal Exit Interface
         */
        ArrivalTerminalExitInterface ateInt;           
        /**
         * Communication channels
         */
        ServerCom scon, sconi;
        /**
         * Service provider thread
         */                          
        ATEProxy ateProxy;          
        /**
         * General Information Repository Stub
         */
        GenInfoRepoStub repoStub;

        /* estabelecimento do servico */
        //Creation of the listening channel and its association with the public address
        scon = new ServerCom (Global.arrivalTerminalExitStub_PORT);                    
        scon.start ();   
        
        repoStub = new GenInfoRepoStub(Global.genRepo_HOSTNAME, Global.genRepo_PORT);
        // service activation                                    
        ate = new ArrivalTerminalExit(Global.NR_PASSENGERS, repoStub);                         // activação do serviço
        DepartureTerminalEntranceStub departureTerminalEntranceStub = new DepartureTerminalEntranceStub(Global.departureTerminalEntranceStub_HOSTNAME, Global.departureTerminalEntranceStub_PORT);
        ate.setDepartureTerminal(departureTerminalEntranceStub);
        // activation of the interface with the service
        ateInt = new ArrivalTerminalExitInterface(ate);       
        System.out.println("The service has been established!!");
        System.out.println("The server is listening.");

         /* processamento de pedidos */
        waitConnection = true;
        while (waitConnection)
            try{ 

                // entry into listening process
                sconi = scon.accept ();                         
                // launch of the service provider
                ateProxy = new ATEProxy(sconi, ateInt);
                ateProxy.start ();
            }catch (SocketTimeoutException e){ }
            // termination of operations
            scon.end ();                                         

        System.out.println("The server has been disabled.");


    }

}