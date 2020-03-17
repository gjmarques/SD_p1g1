
package sharedRegions;

import entities.*;
import java.util.Queue;
import java.util.Stack;

public class DepartureTermEntrance {

    private int[] leavingPass = {0,0,0,0,0};

    private int nPass = 5;

    
    /** 
     * @param nPlane
     */
    //PASSENGER

    public synchronized void goHome(int nPlane){
        leavingPass[nPlane]  = leavingPass[nPlane] + 1;
        if(leavingPass[nPlane] == nPass){
            //END FLIGHT
        }

    }

	
    /** 
     * @param i
     */
    public static void prepareNextLeg(int i) {
	}

}