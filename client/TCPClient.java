package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

/**
 * This is a TCP Client class, that interacts with the server.
 */
public class TCPClient extends AbstractClient {

    private Logger logger;
    public TCPClient(Logger logger){
        this.logger=logger;
    }
    public void startClient(String serverIP, int serverPort) {
        Socket socket = null;
        try {
            socket = new Socket(serverIP, serverPort);
            socket.setSoTimeout(5000);
            System.out.println("Connected to the server");
            logger.info("Connected to the server");
        } catch (IOException e) {
            System.out.println("Couldn't connect to server at mentioned IP and port");
            logger.info("Couldn't connect to server at mentioned IP and port\"");
            System.exit(1);
        }

        try (
                BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            populateKeyValues(in, out);

            while (true) {
                String request = generateRequestFromUserChoice(userInput);
                if(request.isEmpty()) {
                    continue;
                }
                sendRequest(out, in, request);

                System.out.print("Do you want to perform another operation? (yes/no): ");
                String anotherOperation = userInput.readLine().toLowerCase();
                if (!anotherOperation.equals("yes")) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private long generateChecksum(String requestString) {
        byte [] m = requestString.getBytes();
        Checksum crc32 = new CRC32();
        crc32.update(m, 0, m.length);
        return crc32.getValue();
    }

    private void sendRequest(PrintWriter out, BufferedReader in, String request) throws IOException {
        try{
            request = generateChecksum(request) + "::" + request;
            // Send request to server
            out.println(request);
            // Receive response from server

            String responseFromServer = in.readLine();

            if(responseFromServer.contains("?")){
                responseFromServer=responseFromServer.replace("?","\n");
            }
            System.out.println(responseFromServer);
            // Log response
            logger.info("Response from server: " + responseFromServer);

        } catch (SocketTimeoutException e){
            String[] strArr = request.split("::");
            String requestId = strArr[0];
            System.out.println("Received no response from the server for request id : "+requestId);
            logger.info("Received no response from the server for the request id : "+requestId);
        }
    }

    private void populateKeyValues(BufferedReader in, PrintWriter out) {
        final int NUM_KEYS = 10;
        try {
            // PUT requests
            for (int i = 1; i <= NUM_KEYS*2; i++) {
                UUID uuid = UUID.randomUUID();
                String requestId = uuid.toString();
                String key = Integer.toString(i);
                String value = Integer.toString(i * 10);
                String putString = requestId + "::PUT::key" + key + "::value" + value;

                sendRequest(out, in, putString);
                System.out.println("Pre-populated key" + key + " with value " + value);
                logger.info("Pre-populated key" + key + " with value " + value);
            }
            //GET ALL request
            String requestId = UUID.randomUUID().toString();
            String getAllString = requestId + "::" + "GETALL";
            sendRequest(out, in, getAllString);
            System.out.println("*****GET ALL values*****");
            logger.info("*****GET ALL values*****");

            //GET requests
            for (int i = 1; i <= NUM_KEYS*2; i++) {
                UUID uuid = UUID.randomUUID();
                requestId = uuid.toString();
                String key = Integer.toString(i);
                String getString = requestId + "::GET::key" + key;

                sendRequest(out, in, getString);
                System.out.println("GET key" + key);
                logger.info("GET key" + key);
            }
            //DELETE requests
            for (int i = 5; i <= NUM_KEYS*2; i++) {
                UUID uuid = UUID.randomUUID();
                requestId = uuid.toString();
                String key = Integer.toString(i);
                String deleteString = requestId + "::DELETE::key" + key;

                sendRequest(out, in, deleteString);
                System.out.println("DELETE key" + key);
                logger.info("DELETE key" + key);
            }
            // SIZE request
            UUID uuid = UUID.randomUUID();
            requestId = uuid.toString();
            String sizeString = requestId + "::SIZE";
            sendRequest(out, in, sizeString);
            System.out.println("SIZE request");
            logger.info("SIZE request");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
