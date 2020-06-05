package clientSide;

import global.*;
import comInf.*;
import java.io.*;

import java.util.*;

public class PassengerMain{

    public static void main(String[] args) throws IOException{

        String hostname = "localhost";
        int port = Global.passenger_PORT;



		/**
         * Inicialization Stub Areas
         */
        ArrivalLoungeStub arrivalLoungeStub = new ArrivalLoungeStub(hostname, Global.arrivalLoungeStub_PORT);     
        ArrivalTermTransfQuayStub arrivalTermTransfQuayStub = new ArrivalTermTransfQuayStub(hostname, Global.arrivalTermTransfQuayStub_PORT);
        DepartureTermTransfQuayStub departureTermTransfQuayStub = new DepartureTermTransfQuayStub(hostname, Global.departureTermTransfQuayStub_PORT); 
        BaggageCollectionPointStub baggageCollectionPointStub = new BaggageCollectionPointStub(hostname, Global.baggageCollectionPointStub_PORT);
        BaggageReclaimOfficeStub baggageReclaimOfficeStub = new BaggageReclaimOfficeStub(hostname, Global.baggageReclaimOfficeStub_PORT);
        ArrivalTerminalExitStub arrivalTerminalExitStub = new ArrivalTerminalExitStub(hostname, Global.arrivalTerminalExitStub_PORT);
        DepartureTerminalEntranceStub departureTerminalEntranceStub = new DepartureTerminalEntranceStub(hostname, Global.departureTerminalEntranceStub_PORT);
        GenInfoRepoStub repoStub = new GenInfoRepoStub(hostname, Global.genRepo_PORT);

        /**
         * List of every {@link Bag} of every flight occurring in this airport.
         */
        List<List<Integer>> bags = generateBags(repoStub, Global.NR_PASSENGERS, Global.MAX_FLIGHTS, Global.MAX_BAGS, hostname);
        /**
         * List of Passengers
         */
        Passenger[] passengers = new Passenger[Global.NR_PASSENGERS];
        for (int i = 0; i < Global.NR_PASSENGERS; i++) {
            passengers[i] = new Passenger(i, bags.get(i), arrivalLoungeStub, arrivalTermTransfQuayStub, departureTermTransfQuayStub,
                    baggageCollectionPointStub, baggageReclaimOfficeStub, arrivalTerminalExitStub, departureTerminalEntranceStub, repoStub);
        }
        for (int i = 0; i < Global.NR_PASSENGERS; i++){
            passengers[i].start();
        }
        

        for (int i = 0; i < Global.NR_PASSENGERS; i++) {
            try{
                passengers[i].join();
            } catch (Exception e) {
                System.out.println("Thread: " + Thread.currentThread().getName() + " terminated.");
                System.out.println("Error: " + e.getMessage());
                System.exit(1);
            }
        }
        
        arrivalLoungeStub.shutdown();
        System.out.println("arrivalLoungeStub.shutdown();");
        baggageCollectionPointStub.shutdown();
        System.out.println("baggageCollectionPointStub.shutdown();");
        baggageReclaimOfficeStub.shutdown();
        System.out.println("baggageReclaimOfficeStub.shutdown();");
        arrivalTerminalExitStub.shutdown();
        System.out.println("arrivalTerminalExitStub.shutdown();");
        departureTerminalEntranceStub.shutdown();
        System.out.println("departureTerminalEntranceStub.shutdown();");
        repoStub.shutdown();
        System.out.println("RAN SUCCESSFULLY");
        
    }

    /**
     * Generates a list with a random number of bags for each flight of a passenger
     *
     * @param nrFlights
     * @param maxBags
     * @param nrPassengers
     * @param genInfoRepo
     * @return List<Integer>
     */
    public static List<List<Integer>> generateBags(GenInfoRepoStub repoStub, int nrPassengers, int nrFlights, int maxBags, String hostname) {
        List<List<Integer>> bagsPerPassenger = new ArrayList<List<Integer>>(nrPassengers);
        int[] bagsPerFlight = new int[nrFlights];

        for (int j= 0; j < nrPassengers; j++){
            List<Integer> bags = new ArrayList<Integer>();
            for (int i = 0; i < nrFlights; i++) {
                Random r = new Random();
                int result = r.nextInt(maxBags + 1);
                bags.add(result);
                bagsPerFlight[i] += result;
            }
            bagsPerPassenger.add(bags);

        }
        for (int i=0 ; i< bagsPerFlight.length; i++){
            System.out.println("PASSENGER bagsPerFlight: " + bagsPerFlight[i]);
        }
        repoStub.nrBagsPlanesHold(bagsPerFlight);
        // send bagsPerFlight to GeneralRepo
        //send_info(bagsPerFlight, hostname);

        return bagsPerPassenger;
    }

    /*public static void send_info(int[] bagsPerFlight, String hostname){
        // create connection
        ClientCom con = new ClientCom(hostname, Global.genRepo_PORT);
        Message inMessage, outMessage;
        Thread p_thread = Thread.currentThread();
        while (!con.open ()){
            try{ 
                p_thread.sleep ((long) (10));
            }catch (InterruptedException e) {}
        }
        System.out.println("SEND MSG TO REPOSITORY");
        // send message to arrival lounge interface, and wait for answer
        outMessage = new Message (Message.BAGS_P_FLIGHT, bagsPerFlight);   
        con.writeObject (outMessage);

        // receive new in message, and process it
        inMessage = (Message) con.readObject ();
        System.out.println("Passenger answer from repo" + inMessage);
        if (inMessage.getType () != Message.ACK){ 
            System.out.println ("Thread " + p_thread.getName () + ": Invalid message type!");
            System.out.println (inMessage.toString ());
            System.exit (1);
        }
        con.close ();
    }*/
}