package client;

import java.util.logging.*;

/**
 * Main TCP class that initialises the TCP client.
 */
public class TCPClientMain {

  private static final Logger logger = Logger.getLogger(TCPClientMain.class.getName());
  public static void main(String[] args) {
    if (args.length != 2) {
        System.out.println("please provide 2 arguments <serverIP> and <port>");
        System.exit(1);
    }
    String serverIP = args[0];
    int serverPort = Integer.parseInt(args[1]);

    try{

        FileHandler fileHandler = new FileHandler("clientTCP.log");
        fileHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(fileHandler);

        AbstractClient client = new ClientTCP(logger);
        client.startClient(serverIP, serverPort);
    }
    catch (Exception e){
        System.out.println("cant initialise TCP client");
    }
  }
}
