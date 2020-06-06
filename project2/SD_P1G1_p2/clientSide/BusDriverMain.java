package clientSide;

import global.*;
import serverSide.DepartureTermTransfQuayInterface;

import java.io.*;

public class BusDriverMain {
    public static void main(String[] args) throws IOException{

        String hostname = Global.busTime_HOSTNAME;
        int port = Global.passenger_PORT;

		/**
         * Inicialization Stub Areas
         */
        ArrivalTermTransfQuayStub arrivalTermTransfQuayStub = new ArrivalTermTransfQuayStub(Global.arrivalTermTransfQuayStub_HOSTNAME , Global.arrivalTermTransfQuayStub_PORT);
        DepartureTermTransfQuayStub departureTermTransfQuayStub = new DepartureTermTransfQuayStub(Global.departureTermTransfQuayStub_HOSTNAME , Global.departureTermTransfQuayStub_PORT);
        GenInfoRepoStub repStub = new GenInfoRepoStub(Global.genRepo_HOSTNAME , Global.genRepo_PORT);

        /** Creation and start threads/simulation */
        BusDriver driver = new BusDriver(arrivalTermTransfQuayStub, departureTermTransfQuayStub, repStub);
        BusTimer timer = new BusTimer(arrivalTermTransfQuayStub);
        timer.start();
        //BusDriver driver = new BusDriver(arrivalTermTransfQuayStub, departureTermTransfQuayStub, repStub);
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
        System.out.println("departureTermTransfQuayStub.shutdown();");
        arrivalTermTransfQuayStub.shutdown();
        System.out.println("arrivalTermTransfQuayStub.shutdown();");
        System.out.println("RAN SUCCESSFULLY");
    }
}