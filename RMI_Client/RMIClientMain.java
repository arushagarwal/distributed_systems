package RMI_Client;

import RMI_Server.DataStore;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class RMIClientMain {

    private static DataStore<String, String> ds = null;
    private static final Logger logger = Logger.getLogger(RMIClientMain.class.getName());
    public static void main(String[] args) {
        int portNumber = Integer.parseInt(args[0]);
        try{
            Registry registry = LocateRegistry.getRegistry("localhost", portNumber);
            ds = (DataStore<String, String>) registry.lookup("KeyValueDataStore");

            FileHandler fileHandler = new FileHandler("clientRMI.log");
            fileHandler.setFormatter(new SimpleFormatter());
            Logger parentLog= logger.getParent();
            if (parentLog!=null&&parentLog.getHandlers().length>0) parentLog.removeHandler(parentLog.getHandlers()[0]);
            logger.addHandler(fileHandler);

            ClientRMI client = new ClientRMI(logger,ds);
            client.startClient();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
