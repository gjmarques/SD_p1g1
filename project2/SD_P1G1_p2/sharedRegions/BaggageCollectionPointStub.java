package sharedRegions;

import comInf.Message;

public class BaggageCollectionPointStub{

    private String serverHostName = null;
    private int serverPortNumb;

    public BaggageCollectionPointStub(String hostname, int port){
        serverHostName = hostname;
        serverPortNumb = port;
    }
}