package clientSide;

import global.*;

import java.io.*;

public class PorterMain {

    public static void main(String[] args) throws IOException{

        String hostname = Global.porter_HOSTNAME;
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
        System.out.println("tempStorageAreaStub.shutdown();");
        System.out.println("RAN SUCCESSFULLY");
    }
}