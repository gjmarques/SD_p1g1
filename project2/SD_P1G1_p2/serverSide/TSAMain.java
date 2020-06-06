package serverSide;

import java.io.IOException;
import java.net.SocketTimeoutException;

import global.Global;
import clientSide.*;

/**
 * This type of data simulates a server-side solution to the Airport Rapsody' Problem that implements the
 * type 2 client-server model (server replication) with static launch of the shared regions threads.
 * Communication is based on passing messages over sockets using the TCP protocol.
 */
public class TSAMain {

    /**
     * activity signaling
     */
    public static boolean waitConnection;

    public static void main(String[] args) throws IOException {

        /**
         * Represents the service to be provided
         */
        TempStorageArea tsa;
        /**
         * Arrival Lounge Interface
         */
        TempStorageAreaInterface tsaInt;
        /**
         * Communication channels
         */
        ServerCom scon, sconi;
        /**RepoStub
         * Service provider thread
         */
        TSAProxy tsaProxy;
        /**
         * General Information Repository Stub
         */
        GenInfoRepoStub repoStub;

        /* estabelecimento do servico */
        // Creation of the listening channel and its association with the public address
        scon = new ServerCom(Global.tempStorageArea_PORT);
        scon.start();

        repoStub = new GenInfoRepoStub(Global.genRepo_HOSTNAME, Global.genRepo_PORT);
        // service activation
        tsa = new TempStorageArea(repoStub); // activação do serviço
        // activation of the interface with the service
        tsaInt = new TempStorageAreaInterface(tsa);
        System.out.println("The service has been established!!");
        System.out.println("The server is listening.");

        /* processamento de pedidos */
        waitConnection = true;
        while (waitConnection)
            try {

                // entry into listening process
                sconi = scon.accept();
                // launch of the service provider
                tsaProxy = new TSAProxy(sconi, tsaInt);
                tsaProxy.start();
            } catch (SocketTimeoutException e) {
            }
        // termination of operations
        scon.end();

        System.out.println("The server has been disabled.");
    }

}