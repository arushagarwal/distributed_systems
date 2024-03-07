package client;

import java.io.IOException;
import java.util.logging.*;

/**
 * This class starts an instance of a TCP client.
 */
public class ClientAppTCP {

  private static final Logger logger = Logger.getLogger(ClientAppTCP.class.getName());
  public static void main(String[] args) {
    if (args.length != 2) {
      System.out.println("Usage: java ClientAppTCP <serverIP> <port>");
      System.exit(1);
    }
    String serverIP = args[0];
    int serverPort = Integer.parseInt(args[1]);

    try{
        FileHandler fileHandler = new FileHandler("clientTCP.log");
        fileHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(fileHandler);

        AbstractClient client = new TCPClient(logger);
        client.startClient(serverIP, serverPort);
    }
    catch (Exception e){

    }
  }
}
