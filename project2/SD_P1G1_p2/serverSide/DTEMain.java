package serverSide;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;

import global.Global;
import clientSide.*;

public class DTEMain {

        /**
         * activity signaling
         */
        public static boolean waitConnection;  

    public static void main(String[] args) throws IOException{

        /**
         * Represents the service to be provided
         */
        DepartureTerminalEntrance dte;                  
        /**
         * Arrival Lounge Interface
         */
        DepartureTerminalEntranceInterface dteInt;           
        /**
         * Communication channels
         */
        ServerCom scon, sconi;
        /**
         * Service provider thread
         */                          
        DTEProxy dteProxy;          
        /**
         * General Information Repository Stub
         */
        GenInfoRepoStub repoStub;
       
        
        /* estabelecimento do servico */
        //Creation of the listening channel and its association with the public address
        scon = new ServerCom (Global.departureTerminalEntranceStub_PORT);                    
        scon.start ();   

        repoStub = new GenInfoRepoStub("localhost", Global.genRepo_PORT);
        // service activation                                    
        dte = new DepartureTerminalEntrance(Global.NR_PASSENGERS,repoStub);                           // activação do serviço
        // activation of the interface with the service
        dteInt = new DepartureTerminalEntranceInterface(dte);       
        System.out.println("The service has been established!!");
        System.out.println("The server is listening.");

         /* processamento de pedidos */
        waitConnection = true;
        while (waitConnection)
            try{ 

                // entry into listening process
                sconi = scon.accept ();                         
                // launch of the service provider
                dteProxy = new DTEProxy(sconi, dteInt);
                dteProxy.start();
            }catch (SocketTimeoutException e){ }
            // termination of operations
            scon.end ();                                         

        System.out.println("The server has been disabled.");
    }

}