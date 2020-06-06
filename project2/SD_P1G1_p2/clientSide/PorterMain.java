package clientSide;

import global.*;

import java.io.*;

/**
 * This type of data simulates a client-side solution to the Airport Rapsody' Problem that implements the
 * type 2 client-server model (server replication) with static launch of the shared regions threads.
 * Communication is based on passing messages over sockets using the TCP protocol.
 */
public class PorterMain {

    public static void main(String[] args) throws IOException{

        /**
        * Name of the computational system where the server is located
        */
        String hostname = "localhost";
        /**
         * Server listening port number
         */
        int port = Global.porter_PORT;

        ArrivalLoungeStub arrivalLoungeStub = new ArrivalLoungeStub(Global.arrivalLoungeStub_HOSTNAME , Global.arrivalLoungeStub_PORT);
        BaggageCollectionPointStub baggageCollectionPointStub = new BaggageCollectionPointStub(Global.baggageCollectionPointStub_HOSTNAME , Global.baggageCollectionPointStub_PORT);
        GenInfoRepoStub repoStub = new GenInfoRepoStub(Global.genRepo_HOSTNAME , Global.genRepo_PORT);
        TempStorageAreaStub tempStorageAreaStub = new TempStorageAreaStub(Global.tempStorageArea_HOSTNAME , Global.tempStorageArea_PORT);
        Porter porter = new Porter(arrivalLoungeStub, tempStorageAreaStub, baggageCollectionPointStub, repoStub);
        porter.start();

        try{
            porter.join();
        } catch (Exception e) {
            System.out.println("Thread: " + Thread.currentThread().getName() + " terminated.");
            System.out.println("Error: " + e.getMessage());
            System.exit(1);
        }
        tempStorageAreaStub.shutdown();
        System.out.println("RAN SUCCESSFULLY");
    }
}