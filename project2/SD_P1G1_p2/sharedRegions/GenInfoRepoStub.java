package sharedRegions;

import comInf.Message;

public class GenInfoRepoStub{

    private String serverHostName = null;
    private int serverPortNumb;

    public GenInfoRepoStub(String hostname, int port){
        serverHostName = hostname;
        serverPortNumb = port;
    }
}