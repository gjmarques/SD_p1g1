package entities;

public class Bag extends Thread{
    
    private char destination;
    private int id;
    private int flightNR;

    public Bag (char destination, int id, int flight){
        this.destination = destination;
        this.id = id;
        this.flightNR = flight;
    }

	
    /** 
     * @return char
     */
    public char getDestination() {
		return this.destination;
    }
    
    public int getID() {
		  return this.id;
	  }

    public int getFlightNR() {
		  return this.flightNR;
	  }

}