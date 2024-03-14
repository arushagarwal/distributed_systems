package RMI_Server;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ServerRMI extends UnicastRemoteObject {

    public  ServerRMI() throws RemoteException {
        super();
    }

    public static void main(String[] args) {
        try {
            KeyValueDataStore<String,String> dataStore = new KeyValueDataStore<>();
            Naming.bind("KeyValueDataStore",dataStore);

            System.out.println("added server to registry");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
