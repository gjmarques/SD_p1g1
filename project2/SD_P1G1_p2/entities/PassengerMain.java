
package entities;

import mainProgram.*;
import sharedRegions.*;

import java.util.*;

public class PassengerMain{

    public PassengerMain(){
        String hostname = Global.passenger_HOSTNAME;
        int port = Global.passenger_PORT;

        /**
         * Inicialization Stub Areas
         */
        ArrivalLoungeStub arrivalLoungeStub = new ArrivalLoungeStub(hostname, Global.arrivalLoungeStub_PORT);
        ArrivalTermTransfQuayStub arrivalTermTransfQuayStub = new ArrivalTermTransfQuayStub(hostname, Global.arrivalTerminalExitStub_PORT);
        DepartureTermTransfQuayStub departureTermTransfQuayStub = new DepartureTermTransfQuayStub(hostname, Global.departureTermTransfQuayStub_PORT);
        BaggageCollectionPointStub baggageCollectionPointStub = new BaggageCollectionPointStub(hostname, Global.baggageCollectionPointStub_PORT);
        BaggageReclaimOfficeStub baggageReclaimOfficeStub = new BaggageReclaimOfficeStub(hostname, Global.baggageReclaimOfficeStub_PORT);
        ArrivalTerminalExitStub arrivalTerminalExitStub = new ArrivalTerminalExitStub(hostname, Global.arrivalTerminalExitStub_PORT);
        DepartureTerminalEntranceStub departureTerminalEntranceStub = new DepartureTerminalEntranceStub(hostname, Global.departureTermTransfQuayStub_PORT);
        GenInfoRepoStub repoStub = new GenInfoRepoStub(hostname, Global.genRepo_PORT);



        /**
         * List of every {@link Bag} of every flight occurring in this airport.
         */
        List<List<Integer>> bags = generateBags(repoStub, Global.NR_PASSENGERS, Global.NR_FLIGHTS, Global.MAX_BAGS);
        
        /**
         * List of Passengers
         */
        Passenger[] passengers = new Passenger[Global.NR_PASSENGERS];
        for (int i = 0; i < Global.NR_PASSENGERS; i++) {
            passengers[i] = new Passenger(i, bags.get(i), arrivalLoungeStub, arrivalTermTransfQuayStub, departureTermTransfQuayStub,
                    baggageCollectionPointStub, baggageReclaimOfficeStub, arrivalTerminalExitStub, departureTerminalEntranceStub, repoStub);
            passengers[i].start();
        }

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
    public static List<List<Integer>> generateBags(GenInfoRepo genInfoRepo, int nrPassengers, int nrFlights, int maxBags) {


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
        // genInfoRepo.nrBagsPlanesHold(bagsPerFlight);
        // send bagsPerFlight to GeneralRepo

        return bagsPerPassenger;
    }
}