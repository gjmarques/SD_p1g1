package entities;

import sharedRegions.*;
import java.io.*;
import mainProgram.*;

public class PorterMain {

    public static void main(String[] args) throws IOException{

        String hostname = null;
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
    }

    
}