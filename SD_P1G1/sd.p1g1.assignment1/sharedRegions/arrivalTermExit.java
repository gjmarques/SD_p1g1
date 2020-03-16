package sharedRegions;

import entities.*;
import java.util.Queue;
import java.util.Stack;

public class arrivalTermExit {

    private int[] leavingPass = {0,0,0,0,0};
    private int nPass = 5;

    
    
    //PASSENGER
    /** 
     * @param nPlane
     */
    public synchronized void goHome(int nPlane){
        leavingPass[nPlane]  = leavingPass[nPlane] + 1;
        if(leavingPass[nPlane] == nPass){
            //END FLIGHT
        }

    }

}