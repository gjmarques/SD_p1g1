package clientSide;

import global.*;
import comInf.Message;

/**
 * This class defines the stub of the {@link serverSide.ArrivalTermTransfQuay} in the AIRPORT RAPSODY that implements the 
 * client-server model (type 2) with static launch of the threads
 */
public class ArrivalTermTransfQuayStub{

    /**
     * Name of the computational system where the server is located
     */
    private String serverHostName = "localhost";
    /**
     * Server listening port number
     */
    private int serverPortNumb;

    /**
     * Inntantiation of the stub to the Departure Terminal Tranfer Quay
     * @param hostname name of the computer system where the server is located
     * @param port ort server listening port number
     */
    public ArrivalTermTransfQuayStub(String hostname, int port){
        serverHostName = hostname;
        serverPortNumb = port;
    }

    /**
     * This method updates internal attq flight count (Service request)
     * @param flight_number flight number
     */
	public void setFlight(int flight_number){
		// create connection
        ClientCom con = new ClientCom("localhost", Global.arrivalTermTransfQuayStub_PORT);
        Message inMessage, outMessage;
        Passenger p_thread = (Passenger) Thread.currentThread();
        while (!con.open ()){
            try{ 
                p_thread.sleep ((long) (10));
            }catch (InterruptedException e) {}
        }
        // send message to arrival lounge interface, and wait for answer
        outMessage = new Message (Message.SET_FLIGHT, flight_number);   
        con.writeObject (outMessage);
 
        // receive new in message, and process it
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.ACK){ 
            System.out.println ("Thread " + p_thread.getName () + ": Invalid message type!");
            System.out.println (inMessage.toString ());
            System.exit (1);
        }
        con.close ();
    }
    /**
     * Signal that passenger is taking a bus (service request)
     * @param passengerID passenger identification
     */
    public void takeABus(int passengerID) {
        // create connection
        ClientCom con = new ClientCom(serverHostName, Global.arrivalTermTransfQuayStub_PORT);
        Message inMessage, outMessage;
        Passenger p_thread = (Passenger) Thread.currentThread();
        while (!con.open ()){
            try{ 
                p_thread.sleep ((long) (10));
            }catch (InterruptedException e) {}
        }
        // send message to arrival lounge interface, and wait for answer
        outMessage = new Message (Message.TAKINGBUS, passengerID);   
        con.writeObject (outMessage);
 
        // receive new in message, and process it
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.ACK){ 
            System.out.println ("Thread " + p_thread.getName () + ": Invalid message type!");
            System.out.println (inMessage.toString ());
            System.exit (1);
        }
        con.close ();
    }
    /**
     * Signal that passenger is entering the bus (Service request)
     * @param passengerID passenger identification
     */
    public void enterTheBus(int passengerID) {
        // create connection
        ClientCom con = new ClientCom(serverHostName, Global.arrivalTermTransfQuayStub_PORT);
        Message inMessage, outMessage;
        Passenger p_thread = (Passenger) Thread.currentThread();
        while (!con.open ()){
            try{ 
                p_thread.sleep ((long) (10));
            }catch (InterruptedException e) {}
        }
        // send message to arrival lounge interface, and wait for answer
        outMessage = new Message (Message.ENTERINGBUS, passengerID);   
        con.writeObject (outMessage);
 
        // receive new in message, and process it
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.ACK){ 
            System.out.println ("Thread " + p_thread.getName () + ": Invalid message type!");
            System.out.println (inMessage.toString ());
            System.exit (1);
        }
        con.close ();
    }
    /**
     * Signal Bus Driver that is work days has ended (service request)
     * @return <li> 'E' if bus driver work days have ended
     *         <li> 'W' otherwise
     *         <li> 'z' if it is not possible to say
     */
    public char hasDaysWorkEnded() {
        // create connection
        ClientCom con = new ClientCom(serverHostName, Global.arrivalTermTransfQuayStub_PORT);
        Message inMessage, outMessage;
        BusDriver p_thread = (BusDriver) Thread.currentThread();
        while (!con.open ()){
            try{ 
                p_thread.sleep ((long) (10));
            }catch (InterruptedException e) {}
        }
        // send message to arrival lounge interface, and wait for answer
        outMessage = new Message (Message.WORK_END);   
        con.writeObject (outMessage);
        // receive new in message, and process it
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.WORK_ENDED && inMessage.getType () != Message.WORK_NOT_ENDED){ 
            System.out.println ("Thread " + p_thread.getName () + ": Invalid message type!");
            System.out.println (inMessage.toString ());
            System.exit (1);
        }
        char res = 'z';
        if(inMessage.getType() == Message.WORK_ENDED) res = 'E';
        else if(inMessage.getType() == Message.WORK_NOT_ENDED) res = 'W';

        con.close ();
        return res;
    }
    /**
     * Bus Driver announces that passengers can now board the bus (service request)
     * @return int number of bus boarding the bus
     */
    public int annoucingBusBoarding() {
        // create connection
        ClientCom con = new ClientCom(serverHostName, Global.arrivalTermTransfQuayStub_PORT);
        Message inMessage, outMessage;
        BusDriver p_thread = (BusDriver) Thread.currentThread();
        while (!con.open ()){
            try{ 
                p_thread.sleep ((long) (10));
            }catch (InterruptedException e) {}
        }
        // send message to arrival lounge interface, and wait for answer
        outMessage = new Message (Message.BUSBOARD);   
        con.writeObject (outMessage);
        // receive new in message, and process it
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.BUSBOARD){ 
            System.out.println ("Thread " + p_thread.getName () + ": Invalid message type!");
            System.out.println (inMessage.toString ());
            System.exit (1);
        }
        con.close ();
        return inMessage.get_Bus_numPassengers_boarding();
    }
    /**
     * Bus driver announces that he is oing to the arrival terminal (request service)
     */
    public void goToArrivalTerminal(){
        ClientCom con = new ClientCom(serverHostName, Global.arrivalTermTransfQuayStub_PORT);
        Message inMessage, outMessage;
        BusDriver p_thread = (BusDriver) Thread.currentThread();
        while (!con.open ()){
            try{ 
                p_thread.sleep ((long) (10));
            }catch (InterruptedException e) {}
        }
        // send message to arrival lounge interface, and wait for answer
        outMessage = new Message (Message.GOTO_ATTQ);   
        con.writeObject (outMessage);
 
        // receive new in message, and process it
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.ACK){ 
            System.out.println ("Thread " + p_thread.getName () + ": Invalid message type!");
            System.out.println (inMessage.toString ());
            System.exit (1);
        }
        con.close ();
    }
    /**
     * Bus Driver announces that he has parked the bus (request service)
     */
    public void parkTheBus() {
        ClientCom con = new ClientCom(serverHostName, Global.arrivalTermTransfQuayStub_PORT);
        Message inMessage, outMessage;
        BusDriver p_thread = (BusDriver) Thread.currentThread();
        while (!con.open ()){
            try{ 
                p_thread.sleep ((long) (10));
            }catch (InterruptedException e) {}
        }
        // send message to arrival lounge interface, and wait for answer
        outMessage = new Message (Message.PARK);   
        con.writeObject (outMessage);
 
        // receive new in message, and process it
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.ACK){ 
            System.out.println ("Thread " + p_thread.getName () + ": Invalid message type!");
            System.out.println (inMessage.toString ());
            System.exit (1);
        }
        con.close ();
    }
    /**
     * The Bus Timer signals that is now time for the Bus Driver to departure (Service request)
     */
    public void departureTime() {
        ClientCom con = new ClientCom(serverHostName, Global.arrivalTermTransfQuayStub_PORT);
        Message inMessage, outMessage;
        BusTimer p_thread = (BusTimer) Thread.currentThread();
        while (!con.open ()){
            try{ 
                p_thread.sleep ((long) (10));
            }catch (InterruptedException e) {}
        }
        // send message to arrival lounge interface, and wait for answer
        outMessage = new Message (Message.D_TIME);   
        con.writeObject (outMessage);
 
        // receive new in message, and process it
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.ACK){ 
            System.out.println ("Thread " + p_thread.getName () + ": Invalid message type!");
            System.out.println (inMessage.toString ());
            System.exit (1);
        }
        con.close ();
    }
    /**
     * Shutdown of the server (service request)
     */
    public void shutdown() {
        ClientCom con = new ClientCom(serverHostName, Global.arrivalTermTransfQuayStub_PORT);
        Message inMessage, outMessage;
        Thread p_thread = Thread.currentThread();
        while (!con.open ()){
            try{ 
                p_thread.sleep ((long) (10));
            }catch (InterruptedException e) {}
        }
        // send message to arrival lounge interface, and wait for answer
        outMessage = new Message (Message.SHUT);   
        con.writeObject (outMessage);
 
        // receive new in message, and process it
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.ACK){ 
            System.out.println ("Thread " + p_thread.getName () + ": Invalid message type!");
            System.out.println (inMessage.toString ());
            System.exit (1);
        }
        con.close ();
    }
}