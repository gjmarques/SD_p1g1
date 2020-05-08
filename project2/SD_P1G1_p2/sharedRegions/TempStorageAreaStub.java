package sharedRegions;

import comInf.Message;

public class TempStorageAreaStub{

    private String serverHostName = null;
    private int serverPortNumb;

    public TempStorageAreaStub(String hostname, int port){
        serverHostName = hostname;
        serverPortNumb = port;
    }

}