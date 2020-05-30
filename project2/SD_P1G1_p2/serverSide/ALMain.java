package serverSide;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;

import global.Global;
import clientSide.*;

public class ALMain {

    /**
     * activity signaling
     */
    public static boolean waitConnection;  

    public static void main(String[] args) throws IOException{

        /**
         * Represents the service to be provided
         */
        ArrivalLounge al;                  
        /**
         * Arrival Lounge Interface
         */
        ArrivalLoungeInterface alInt;           
        /**
         * Communication channels
         */
        ServerCom scon, sconi;
        /**
         * Service provider thread
         */                          
        ALProxy alProxy;          
        /**
         * General Information Repository Stub
         */
        GenInfoRepoStub repoStub;
       
        
        /* estabelecimento do servico */
        //Creation of the listening channel and its association with the public address
        scon = new ServerCom (Global.arrivalLoungeStub_PORT);                    
        scon.start ();   

        repoStub = new GenInfoRepoStub("localhost", Global.genRepo_PORT);
        // service activation                                    
        al = new ArrivalLounge(repoStub);                           // activação do serviço
        // activation of the interface with the service
        alInt = new ArrivalLoungeInterface(al);       
        System.out.println("The service has been established!!");
        System.out.println("The server is listening.");

         /* processamento de pedidos */
        waitConnection = true;
        while (waitConnection)
            try{ 
                // entry into listening process
                sconi = scon.accept ();                         
                // launch of the service provider
                alProxy = new ALProxy(sconi, alInt);
                alProxy.start ();
            }catch (SocketTimeoutException e){ }
            // termination of operations
            scon.end ();                                         

        System.out.println("The server has been disabled.");
    }

}