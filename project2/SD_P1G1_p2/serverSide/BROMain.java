package serverSide;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;

import global.Global;
import clientSide.*;

public class BROMain {

        /**
         * activity signaling
         */
        public static boolean waitConnection;  

    public static void main(String[] args) throws IOException{

        /**
         * Represents the service to be provided
         */
        BaggageReclaimOffice bro;                  
        /**
         * Baggage Reclaim Office Interface
         */
        BaggageReclaimOfficeInterface broInt;           
        /**
         * Communication channels
         */
        ServerCom scon, sconi;
        /**
         * Service provider thread
         */                          
        BROProxy broProxy;          
        /**
         * General Information Repository Stub
         */
        GenInfoRepoStub repoStub;
       
        
        /* estabelecimento do servico */
        //Creation of the listening channel and its association with the public address
        scon = new ServerCom (Global.baggageReclaimOfficeStub_PORT);                    
        scon.start ();   

        repoStub = new GenInfoRepoStub(Global.genRepo_HOSTNAME, Global.genRepo_PORT);
        // service activation                                    
        bro = new BaggageReclaimOffice(repoStub);                           // activação do serviço
        // activation of the interface with the service
        broInt = new BaggageReclaimOfficeInterface(bro);       
        System.out.println("The service has been established!!");
        System.out.println("The server is listening.");

         /* processamento de pedidos */
        waitConnection = true;
        while (waitConnection)
            try{ 

                // entry into listening process
                sconi = scon.accept ();                         
                // launch of the service provider
                broProxy = new BROProxy(sconi, broInt);
                broProxy.start();
            }catch (SocketTimeoutException e){ }
            // termination of operations
            scon.end ();                                         

        System.out.println("The server has been disabled.");
    }

}