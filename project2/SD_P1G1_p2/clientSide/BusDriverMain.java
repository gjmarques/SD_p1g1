package clientSide;

import global.*;

import java.io.*;

/**
 * This type of data simulates a client-side solution to the Airport Rapsody' Problem that implements the
 * type 2 client-server model (server replication) with static launch of the shared regions threads.
 * Communication is based on passing messages over sockets using the TCP protocol.
 */
public class BusDriverMain {
    public static void main(String[] args) throws IOException{

        /**
        * Name of the computational system where the server is located
        */
        String hostname = "localhost";
        /**
         * Server listening port number
         */
        int port = Global.passenger_PORT;

        //Initialization of the stub areas
        /**
         * Instantiation of the stub to the arrivael terminal transfer quay
         */
        ArrivalTermTransfQuayStub arrivalTermTransfQuayStub = new ArrivalTermTransfQuayStub(hostname, Global.arrivalTermTransfQuayStub_PORT);
        /**
         * Instantiation of the stub to the Departure Terminal Tranfer Quay
         */
        DepartureTermTransfQuayStub departureTermTransfQuayStub = new DepartureTermTransfQuayStub(hostname, Global.departureTermTransfQuayStub_PORT);
        /**
         * Instantiation of the stub to the Generic Information Repository
         */
        GenInfoRepoStub repStub = new GenInfoRepoStub(hostname, Global.genRepo_PORT);

        /** Creation and start threads/simulation */
        /** Instantiation Bus Driver */
        BusDriver driver = new BusDriver(arrivalTermTransfQuayStub, departureTermTransfQuayStub, repStub);
        /** Instantiation Bus Timer */
        BusTimer timer = new BusTimer(arrivalTermTransfQuayStub);
        /** Start timer thread */
        timer.start();
        /** Start driver thread */
        driver.start();
        

        /** Wait for simulation to start */
        try{
            driver.join();
            timer.stopTimer();
            timer.join();
        } catch (Exception e) {
			System.out.println("Thread: " + Thread.currentThread().getName() + " terminated.");
			System.out.println("Error: " + e.getMessage());
			System.exit(1);
        }
        departureTermTransfQuayStub.shutdown();
        arrivalTermTransfQuayStub.shutdown();
        System.out.println("RAN SUCCESSFULLY");
    }
}