package sharedRegions;

import comInf.Message;

public class DepartureTerminalEntranceStub{

    private String serverHostName = null;
    private int serverPortNumb;

    public DepartureTerminalEntranceStub(String hostname, int port){
        serverHostName = hostname;
        serverPortNumb = port;
    }
}