package server;

import client.AbstractClient;
import client.ClientAppTCP;
import client.TCPClient;

import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * This class starts a TCP server by creating an instance of a TCP server.
 */
public class ServerAppTCP {

  private static final Logger logger = Logger.getLogger(ServerAppTCP.class.getName());
  public static void main(String[] args) {
    if (args.length != 1) {
      System.out.println("Usage: java Server <tcp-port>");
      System.exit(1);
    }

    int tcpPortNumber = Integer.parseInt(args[0]);

    try{
      FileHandler fileHandler = new FileHandler("serverTCP.log");
      fileHandler.setFormatter(new SimpleFormatter());
      logger.addHandler(fileHandler);

      IServer tcpServer = new TCPServer(logger);
      tcpServer.listen(tcpPortNumber);
    }
    catch (Exception e){

    }
  }
}
