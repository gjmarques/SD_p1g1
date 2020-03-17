package entities;

import sharedRegions.*;

public class  BusTimer extends Thread {
    
    private int time = 0; 
    private boolean loop = true;

    private final ArrivalTermTransfQuay arrivalTermTransfQuay;

    public BusTimer(ArrivalTermTransfQuay arrivalTermTransfQuay) {
        this.arrivalTermTransfQuay = arrivalTermTransfQuay;
    }
    
    @Override
    public void run() {
        
        while (loop) {

            try {
                /* System.out.println("TEMPO -> " + time); */
                Thread.sleep(50);
                time += 50;

                if(time % 1000 == 0) {
                    arrivalTermTransfQuay.departureTime();
                }

            } catch (Exception e) {}

        }
        
    }
    
    public void stopTimer(){
        loop = false;
    }
    
}