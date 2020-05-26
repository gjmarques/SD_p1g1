package serverSide;

public class GIRMain {

    /**
     * General Information Repository - service to be provided
     */
    GenInfoRepo repo;
    /**
     * General Information Repository Interface
     */
    GenInfoRepoInterface repoInt;
    /**
     * Communication channels
     */
    ServerCom scon, sconi;
     /**
     * Service provider thread
     */  
    GIRProxy repoProxy;


}