package entities;

import sharedRegions.*;

/** Controlls time of the {@ink BusDriver}. */
public class BusTimer extends Thread {
    
    /**
     * Time
     */
    private int time = 0; 
    /**
     * Determines whether to run timer.
     */
    private boolean loop = true;
    /**
     * Arrival Terminal Transfer Quay
     * {@link sharedRegions.ArrivalTermTransfQuay}
     */
    private final ArrivalTermTransfQuay arrivalTermTransfQuay;

    public BusTimer(ArrivalTermTransfQuay arrivalTermTransfQuay) {
        this.arrivalTermTransfQuay = arrivalTermTransfQuay;
    }
    
    @Override
    public void run() {
        while (loop) {
            try {
                Thread.sleep(50);
                time += 50;

                if(time % 1000 == 0) {
                    arrivalTermTransfQuay.departureTime();
                }
            } catch (Exception e) {
                System.out.println("Thread: " + Thread.currentThread().getName() + " terminated.");
                System.out.println("Error: " + e.getMessage());
                System.exit(1);
            }
        }
    }
    
    public void stopTimer(){
        loop = false;
    }
    
}