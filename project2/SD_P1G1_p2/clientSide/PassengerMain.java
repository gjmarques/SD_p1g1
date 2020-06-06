package clientSide;

import global.*;
import java.io.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * This type of data simulates a client-side solution to the Airport Rapsody' Problem that implements the
 * type 2 client-server model (server replication) with static launch of the shared regions threads.
 * Communication is based on passing messages over sockets using the TCP protocol.
 */
public class PassengerMain{

    public static void main(String[] args) throws IOException, InterruptedException {

        /**
        * Name of the computational system where the server is located
        */
        String hostname = "localhost";
        /**
         * Server listening port number
         */
        int port = Global.passenger_PORT;


		/**
         * Inicialization Stub Areas
         */
        ArrivalLoungeStub arrivalLoungeStub = new ArrivalLoungeStub(Global.arrivalLoungeStub_HOSTNAME , Global.arrivalLoungeStub_PORT);     
        ArrivalTermTransfQuayStub arrivalTermTransfQuayStub = new ArrivalTermTransfQuayStub(Global.arrivalTermTransfQuayStub_HOSTNAME , Global.arrivalTermTransfQuayStub_PORT);
        DepartureTermTransfQuayStub departureTermTransfQuayStub = new DepartureTermTransfQuayStub(Global.departureTermTransfQuayStub_HOSTNAME , Global.departureTermTransfQuayStub_PORT); 
        BaggageCollectionPointStub baggageCollectionPointStub = new BaggageCollectionPointStub(Global.baggageCollectionPointStub_HOSTNAME , Global.baggageCollectionPointStub_PORT);
        BaggageReclaimOfficeStub baggageReclaimOfficeStub = new BaggageReclaimOfficeStub(Global.baggageReclaimOfficeStub_HOSTNAME , Global.baggageReclaimOfficeStub_PORT);
        ArrivalTerminalExitStub arrivalTerminalExitStub = new ArrivalTerminalExitStub(Global.arrivalTerminalExitStub_HOSTNAME , Global.arrivalTerminalExitStub_PORT);
        DepartureTerminalEntranceStub departureTerminalEntranceStub = new DepartureTerminalEntranceStub(Global.departureTerminalEntranceStub_HOSTNAME , Global.departureTerminalEntranceStub_PORT);
        GenInfoRepoStub repoStub = new GenInfoRepoStub(Global.genRepo_HOSTNAME , Global.genRepo_PORT);

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
        /**
         * Start Passenger threads
         */
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
        baggageCollectionPointStub.shutdown();
        baggageReclaimOfficeStub.shutdown();
        arrivalTerminalExitStub.shutdown();
        departureTerminalEntranceStub.shutdown();
        TimeUnit.SECONDS.sleep(2);
        repoStub.shutdown();
        System.out.println("RAN SUCCESSFULLY");
        
    }

    /**
     * Generates a list with a random number of bags for each flight of a passenger
     *
     * @param nrFlights maximum number of flights at the airport
     * @param maxBags maximum number of bags per passenger 
     * @param nrPassengers number of passengers at the airport, per flight
     * @param repoStub {@link clientSide.GenInfoRepoStub}
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
        repoStub.nrBagsPlanesHold(bagsPerFlight);
        return bagsPerPassenger;
    }
}