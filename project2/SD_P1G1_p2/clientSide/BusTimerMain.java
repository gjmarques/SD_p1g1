package clientSide;

import global.*;
import java.io.*;

public class BusTimerMain {

    public static void main(String[] args) throws IOException{

        String hostname = null;
        int port = Global.busTime_PORT;

        ArrivalTermTransfQuayStub arrivalTermTransfQuayStub = new ArrivalTermTransfQuayStub(hostname, Global.arrivalLoungeStub_PORT);

        BusTimer timer = new BusTimer(arrivalTermTransfQuayStub);
		timer.start();

        try{
            timer.stopTimer();
            timer.join();
        } catch (Exception e) {
            System.out.println("Thread: " + Thread.currentThread().getName() + " terminated.");
            System.out.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }

            
}