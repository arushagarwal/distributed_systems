package RMI_Server;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ServerRMI {

    public  ServerRMI() {
        super();
    }

    public static void main(String[] args) {
        try {
            KeyValueDataStore<String,String> dataStore = new KeyValueDataStore<>();
            DataStore<String,String> stub = (DataStore<String,String>) UnicastRemoteObject.exportObject(dataStore, 0);
            Registry registry = LocateRegistry.createRegistry(2000);
            registry.rebind("KeyValueDataStore",stub);

            System.out.println("server ready");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}