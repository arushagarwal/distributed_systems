package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * This represents a TCP based server which listens at a given port number for TCP client requests.
 */
public class TCPServer extends AbstractServer {

    private Logger logger;
    public TCPServer(Logger logger){
        this.logger=logger;
    }
    @Override
    public void listen(int portNumber) {
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {

            System.out.println("Server is listening on port " + portNumber);
            logger.info("Server is listening on port " + portNumber);

            while (true) {
                // Start listening to client requests and creating client socket
                Socket clientSocket = serverSocket.accept();
                System.out.println("client.Client connected: " + clientSocket.getInetAddress());
                logger.info(" - Request from : " + clientSocket.getInetAddress() + ": Client connected");

                try {
                    handleRequest(clientSocket);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    // log information when client closes connection
                    clientSocket.close();
                    System.out.println("client.Client disconnected");
                    logger.info("Client disconnected: " + clientSocket.getInetAddress());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleRequest(Socket clientSocket) throws IOException {
        try (
          BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
          PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received from client: " + inputLine);
                // log client request information
                logger.info(" - Request from : " + clientSocket.getInetAddress() + ": "+inputLine);

                // get information from the key value store
                String response = processRequest(inputLine);

                // write back the response to the client
                out.println(response);

                // log the response information
                if(response.contains("?")){
                    response = response.replace("?","\n");
                }
                logger.info(" - Response to : " + clientSocket.getInetAddress() + ": " + response);
            }
        } catch (IOException e) {
            // Log information about timed out requests.
            System.err.println("Timeout occurred. Server did not respond within the specified time.");
            logger.warning(" - Received Malformed request from " + clientSocket.getInetAddress());

        }
    }
}
 