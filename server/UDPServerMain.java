package server;

import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * This class starts a UDP server by creating an instance of a UDP server.
 */
public class UDPServerMain {
    private static final Logger logger = Logger.getLogger(UDPServerMain.class.getName());
    public static void main(String[] args) {
      if (args.length != 1) {
        System.out.println("Usage: java Server <udp-port>");
        System.exit(1);
      }

      int udpPortNumber = Integer.parseInt(args[0]);
        try{
            FileHandler fileHandler = new FileHandler("serverUDP.log");
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);

            AbstractServer server = new ServerUDP(logger);
            server.startAndListen(udpPortNumber);
        }
        catch (Exception e){

        }

    }
}
