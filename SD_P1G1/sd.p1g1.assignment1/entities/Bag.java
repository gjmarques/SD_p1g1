package entities;

public class Bag extends Thread{
    
    private char destination;
    private int id;

    public Bag (char destination, int id){
        this.destination = destination;
        this.id = id;
    }

	public char getDestination() {
		return this.destination;
	}

}