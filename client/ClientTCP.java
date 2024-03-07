package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Main TCP class that initialises the TCP client.
 */
public class ClientTCP extends AbstractClient {

    static private int TIMEOUT_MS = 5000;
    private Logger logger;
    public ClientTCP(Logger logger){
        this.logger=logger;
    }
    public void startClient(String serverIP, int serverPortNum) {
        Socket socket = null;
        try {
            socket = new Socket(serverIP, serverPortNum);
            socket.setSoTimeout(TIMEOUT_MS);
            System.out.println("Connected to the server...");
            logger.info("Connected to the server...");
        } catch (IOException e) {
            System.out.println("Wrong IP address or port provided, couldn't connect");
            logger.info("Wrong IP address or port provided, couldn't connect");
            System.exit(1);
        }

        try (
                BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            initialOperations(in, out);

            while (true) {
                String request = createRequestFromUserInput(inputReader);
                if(request.length()==0) continue;

                System.out.println("********************* request : "+request+" ********************");

                sendRequestAndReceiveResponse(out, in, request);

                System.out.print("Another Operation ?? (Y/N): ");
                String nextOperation = inputReader.readLine();
                if (nextOperation.equals("n") || nextOperation.equals("N")) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendRequestAndReceiveResponse(PrintWriter out, BufferedReader in, String request) throws IOException {
        try{
            // Send request to server
            out.println(request);
            // Receive response from server

            String responseFromServer = in.readLine();

            if(responseFromServer.contains("?")){
                responseFromServer=responseFromServer.replace("?","\n");
            }
            logger.info("response from server: " + responseFromServer);

        } catch (SocketTimeoutException e){
            String[] strArr = request.split("|");
            String requestId = strArr[0];
            logger.warning("No response received from the server for request id : "+requestId);
        }
    }

    private void initialOperations(BufferedReader in, PrintWriter out) {
        final int KEYS = 1000;
        try {
            String requestId;
            // initial 1000 put requests to populate key-value data store
            for (int i = 1; i <= KEYS; i++) {
                requestId = UUID.randomUUID().toString();
                String key = Integer.toString(i);
                String value = Integer.toString(2*i);
                String putString = requestId + "|PUT|key" + key + "|value" + value;

                logger.info("Sending request to server : PUT -> key " + key + " with value " + value);
                sendRequestAndReceiveResponse(out, in, putString);
            }
            //get all request
            requestId = UUID.randomUUID().toString();
            String getAllString = requestId + "|" + "GETALL";
            logger.info("Sending request to server : GET ALL");
            sendRequestAndReceiveResponse(out, in, getAllString);

            //get request for all the keys in the data store
            for (int i = 1; i <= KEYS; i++) {
                requestId = UUID.randomUUID().toString();
                String key = Integer.toString(i);
                String getString = requestId + "|GET|key" + key;

                logger.info("Sending request to server : GET -> key " + key);
                sendRequestAndReceiveResponse(out, in, getString);
            }
            //delete request for some keys in data store
            for (int i = 1; i <= 5; i++) {
                requestId = UUID.randomUUID().toString();
                String key = Integer.toString(i);
                String deleteString = requestId + "|DELETE|key" + key;

                logger.info("Sending request to server : DELETE -> key " + key);
                sendRequestAndReceiveResponse(out, in, deleteString);
            }
            // size request to get the size of the data store
            requestId = UUID.randomUUID().toString();
            String sizeString = requestId + "|SIZE";
            logger.info("Sending request to server : SIZE ");
            sendRequestAndReceiveResponse(out, in, sizeString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
