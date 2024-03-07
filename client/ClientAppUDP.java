package client;

import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * This class starts an instance of a UDP client.
 */
public class ClientAppUDP {

  private static final Logger logger = Logger.getLogger(ClientAppUDP.class.getName());
  public static void main(String[] args) {
    if (args.length < 2) {
      System.out.println(
        "Usage: java ClientAppUDP <serverIP> <port>");
      System.exit(1);
    }
    String serverIP = args[0];
    int serverPort = Integer.parseInt(args[1]);

    try{
      FileHandler fileHandler = new FileHandler("clientUDP.log");
      fileHandler.setFormatter(new SimpleFormatter());
      logger.addHandler(fileHandler);

      AbstractClient client = new UDPClient(logger);
      client.startClient(serverIP, serverPort);
    }
    catch (Exception e){

    }
  }

}
