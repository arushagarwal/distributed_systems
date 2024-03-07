package client;

import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Main UDP class that initialises the UDP client.
 */
public class UDPClientMain {

  private static final Logger logger = Logger.getLogger(UDPClientMain.class.getName());
  public static void main(String[] args) {
    if (args.length != 2) {
      System.out.println("please provide 2 arguments <serverIP> and <port>");
      System.exit(1);
    }
    String serverIP = args[0];
    int serverPort = Integer.parseInt(args[1]);

    try{
      FileHandler fileHandler = new FileHandler("clientUDP.log");
      fileHandler.setFormatter(new SimpleFormatter());
      logger.addHandler(fileHandler);

      AbstractClient client = new ClientUDP(logger);
      client.startClient(serverIP, serverPort);
    }
    catch (Exception e){
      System.out.println("cant initialise UDP client");
    }
  }

}
