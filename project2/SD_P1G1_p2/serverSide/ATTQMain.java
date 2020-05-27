package serverSide;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;

import global.Global;
import clientSide.*;

public class ATTQMain {
    
    /**
     * activity signaling
     */
    public static boolean waitConnection;  

    public static void main(String[] args) throws IOException{

        /**
         * Represents the service to be provided
         */
        ArrivalTermTransfQuay attq;                  
        /**
         * Arrival Terminal Tranfer Quay Interface
         */
        ArrivalTermTransfQuayInterface attqInt;           
        /**
         * Communication channels
         */
        ServerCom scon, sconi;
        /**
         * Service provider thread
         */                          
        ATTQProxy attqProxy;    
        /**
         * General Information Repository
         */
        GenInfoRepoStub repoStub;      
       
        
        /* estabelecimento do servico */
        //Creation of the listening channel and its association with the public address
        scon = new ServerCom (Global.attqProxy_PORT);                    
        scon.start ();   

        repoStub = new GenInfoRepoStub(null, Global.genRepo_PORT);
        // service activation                                    
        attq = new ArrivalTermTransfQuay(Global.BUS_SIZE, Global.MAX_FLIGHTS, repoStub);                           // activação do serviço
        // activation of the interface with the service
        attqInt = new ArrivalTermTransfQuayInterface(attq);       
        System.out.println("The service has been established!!");
        System.out.println("he server is listening.");

         /* processamento de pedidos */
        waitConnection = true;
        while (waitConnection)
            try{ 

                // entry into listening process
                sconi = scon.accept ();                         
                // launch of the service provider
                attqProxy = new ATTQProxy(sconi, attqInt);
                attqProxy.start ();
            }catch (SocketTimeoutException e){ }
            // termination of operations
            scon.end ();                                         

        System.out.println("The server has been disabled.");
    }

}