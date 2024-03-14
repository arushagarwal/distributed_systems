package RMI_Client;

import RMI_Server.DataStore;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientRMI {

    static DataStore<String, String> ds = null;
    public static void main(String[] args) {
        try{
            Registry registry = LocateRegistry.getRegistry("localhost", 2000);
            ds = (DataStore<String, String>) registry.lookup("KeyValueDataStore");
            ds.put("key1","value1");
            System.out.println(ds.get("key1"));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
