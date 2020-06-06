package clientSide;

import global.*;

import java.io.*;

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

        ArrivalLoungeStub arrivalLoungeStub = new ArrivalLoungeStub(hostname, Global.arrivalLoungeStub_PORT);
        BaggageCollectionPointStub baggageCollectionPointStub = new BaggageCollectionPointStub(hostname, Global.baggageCollectionPointStub_PORT);
        GenInfoRepoStub repoStub = new GenInfoRepoStub(hostname, Global.genRepo_PORT);
        TempStorageAreaStub tempStorageAreaStub = new TempStorageAreaStub(hostname, Global.tempStorageArea_PORT);
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