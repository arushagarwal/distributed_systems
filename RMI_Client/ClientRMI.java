package RMI_Client;

import RMI_Server.DataStore;

import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.logging.Logger;

public class ClientRMI {

    private Logger logger;
    private DataStore<String,String> ds;
    private Scanner scan;
    ClientRMI(Logger logger, DataStore<String,String> ds){
        this.logger=logger;
        this.ds=ds;
        this.scan = new Scanner(System.in);
    }

    public void startClient(){
        initialOperations();
        System.out.println("Initial operation logged in the log file with 100 puts, 100 gets and 5 deletes, currently, database has the data with keys from 6-100");
        try{
            while (true){
                createMenu();
                System.out.print("Another Operation ?? (Y/N): ");
                String nextOperation = scan.nextLine();
                if (nextOperation.equals("n") || nextOperation.equals("N")) {
                    break;
                }
            }
        }
        catch (RemoteException e){
            e.printStackTrace();
        }
    }

    private void initialOperations(){
        final int KEYS = 1000;
        try {
            // initial 1000 put requests to populate key-value data store
            for (int i = 1; i <= KEYS; i++) {
                String key = Integer.toString(i);
                String value = Integer.toString(2*i);

                logger.info("Sending request to server : PUT -> key " + key + " with value " + value);
                ds.put(key,value);
                logger.info("Successfully added the pair ("+key+", "+value+") in the database.");
            }

            //get request for all the keys in the data store
            for (int i = 1; i <= KEYS; i++) {
                String key = Integer.toString(i);
                logger.info("Sending request to server : GET -> key " + key);
                String val = ds.get(key);
                logger.info("Value received from server for key "+key+" : "+val);
            }
            //delete request for some keys in data store
            for (int i = 1; i <= 15; i++) {
                String key = Integer.toString(i);

                logger.info("Sending request to server : DELETE -> key " + key);
                String val = ds.delete(key);
                logger.info("Value deleted for key "+key+", with previous value : "+val);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void createMenu() throws RemoteException{
        System.out.println("Which operation do you want to use?");
        System.out.println("(Database is pre-populated with keys from 16-1000)");
        System.out.println("1. PUT");
        System.out.println("2. GET");
        System.out.println("3. DELETE");
        System.out.println("4. EXIT");
        System.out.print("Enter your choice (1-4): ");

        String input = scan.nextLine();

        switch (input){
            case "1" :
                System.out.print("enter the key to put: ");
                String key = scan.nextLine();
                System.out.print("enter the value for the key: ");
                String value = scan.nextLine();
                logger.info("Sending request to server : PUT -> key " + key + " with value " + value);
                ds.put(key,value);
                logger.info("Successfully added the pair ("+key+", "+value+") in the database.");
                System.out.println("Successfully added the pair ("+key+", "+value+") in the database.");
                break;
            case "2" :
                System.out.print("enter the key: ");
                key = scan.nextLine();
                logger.info("Sending request to server : GET -> key " + key);
                String val = ds.get(key);
                if(val==null){
                    logger.info("No value received from server for key "+key);
                    System.out.println("No value received from server for key "+key);
                }
                else{
                    logger.info("Value not found in server for key "+key+" : "+val);
                    System.out.println("Value received in server for key "+key+" : "+val);
                }

                break;
            case "3" :
                System.out.print("enter the key to delete: ");
                key = scan.nextLine();
                logger.info("Sending request to server : DELETE -> key " + key);
                val = ds.delete(key);
                if(val==null){
                    logger.info("Value not found in server for key "+key+" : "+val);
                    System.out.println("Value received in server for key "+key+" : "+val);
                }
                else{
                    logger.info("Value deleted for key "+key+", with previous value : "+val);
                    System.out.println("Value deleted for key "+key+", with previous value : "+val);
                }
                break;
            case "4" :
                System.out.println("bye bye");
                System.exit(0);
            default:
                System.out.println("Invalid choice. Please enter 1, 2, 3,or 4.");
        }
    }
}
