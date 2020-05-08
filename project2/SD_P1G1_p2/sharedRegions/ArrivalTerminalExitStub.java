package sharedRegions;

import comInf.Message;

public class ArrivalTerminalExitStub{

    private String serverHostName = null;
    private int serverPortNumb;

    public ArrivalTerminalExitStub(String hostname, int port){
        serverHostName = hostname;
        serverPortNumb = port;
    }
}