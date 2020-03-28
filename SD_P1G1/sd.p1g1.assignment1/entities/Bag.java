package entities;

public class Bag extends Thread{
    
  /**
   * Bag's destination
   */
  private char destination;
  /**
   * Bag's identification
   */
  private int id;
  /**
   * Bag's flight number
   */
  private int flightNR;

  /**
   * Creates a Bag
   * @param destinationgetPass
   * @param id
   * @param flight
   */
  public Bag (char destination, int id, int flight){
      this.destination = destination;
      this.id = id;
      this.flightNR = flight;
  }


  /** 
   * Gets this bag's destination
   * @return bag's destination
   */
  public char getDestination() {
    return this.destination;
  }
  
  /** 
   * Gets this bag's identitification
   * @return bag's identification
   */
  public int getID() {
    return this.id;
  }

  /** 
   * Gets this bag's flight number
   * @return bag's flight number
   */
  public int getFlightNR() {
    return this.flightNR;
  }

}