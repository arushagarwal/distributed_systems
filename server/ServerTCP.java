package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * TCP server is a class which will behave as a server listening at given port number for the client requests.
 */
public class ServerTCP extends AbstractServer {

    private Logger logger;
    public ServerTCP(Logger logger){
        this.logger=logger;
    }
    @Override
    public void startAndListen(int portNumber) {
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {

            System.out.println("Server listening on port number" + portNumber);
            logger.info("Server listening on port number" + portNumber);

            while (true) {

                Socket clientSocket = serverSocket.accept();
                System.out.println("client connected at address : " + clientSocket.getInetAddress());
                logger.info("Request from : " + clientSocket.getInetAddress() + ": Client connected");

                try {
                    handleClientRequest(clientSocket);
                } finally {
                    // log information when client closes connection
                    clientSocket.close();
                    System.out.println("Client disconnected");
                    logger.info("Client disconnected from IP address : " + clientSocket.getInetAddress());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void handleClientRequest(Socket clientSocket){
        try (
          BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
          PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            String inputLine = null;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received from client: " + inputLine);
                // logging the request received from client
                logger.info(" - Request from : " + clientSocket.getInetAddress() + ": "+inputLine);

                // processing client request and getting response
                String response = processClientRequest(inputLine);

                // sending response to client
                out.println(response);

                //for the get all requests
                if(response.contains("?")){
                    response = response.replace("?","\n");
                }

                // logging the response info
                logger.info(" - Response to : " + clientSocket.getInetAddress() + ": " + response);
            }
        } catch (IOException e) {
            // Log information about timed out requests.
            System.err.println("Timeout occurred. Server did not respond within the specified time.");
            logger.warning(" - Received Malformed request from " + clientSocket.getInetAddress());

        }
    }
}
 