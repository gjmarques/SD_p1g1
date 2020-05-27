package clientSide;

import global.*;
import java.io.*;

public class BusDriverMain {
    public static void main(String[] args) throws IOException{

        String hostname = "localhost";
        int port = Global.passenger_PORT;

		/**
         * Inicialization Stub Areas
         */
        ArrivalTermTransfQuayStub arrivalTermTransfQuayStub = new ArrivalTermTransfQuayStub(hostname, Global.arrivalTerminalExitStub_PORT);
        DepartureTermTransfQuayStub departureTermTransfQuayStub = new DepartureTermTransfQuayStub(hostname, Global.departureTermTransfQuayStub_PORT);
        
        /** Creation and start threads/simulation */
        BusDriver driver = new BusDriver(arrivalTermTransfQuayStub, departureTermTransfQuayStub);
        driver.start();

        /** Wait for simulation to start */
        try{
            driver.join();
        } catch (Exception e) {
			System.out.println("Thread: " + Thread.currentThread().getName() + " terminated.");
			System.out.println("Error: " + e.getMessage());
			System.exit(1);
        }
        System.out.println("The Bus Driver has ended his day of work!");
    }

}