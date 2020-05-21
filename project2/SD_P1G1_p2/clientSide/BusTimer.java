package clientSide;


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
    private final ArrivalTermTransfQuayStub arrivalTermTransfQuayStub;

    public BusTimer(ArrivalTermTransfQuayStub arrivalTermTransfQuayStub) {
        this.arrivalTermTransfQuayStub = arrivalTermTransfQuayStub;
    }
    
    @Override
    public void run() {
        while (loop) {
            try {
                Thread.sleep(50);
                time += 50;

                if(time % 1000 == 0) {
                    arrivalTermTransfQuayStub.departureTime();
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