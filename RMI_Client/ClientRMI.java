package RMI_Client;

import RMI_Server.DataStore;

import java.util.logging.Logger;

public class ClientRMI {

    private Logger logger;
    private DataStore<String,String> ds;
    ClientRMI(Logger logger, DataStore<String,String> ds){
        this.logger=logger;
        this.ds=ds;
    }

    public void startClient(){

    }
}
