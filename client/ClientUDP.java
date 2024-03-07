package client;

import java.net.*;
import java.io.*;
import java.util.UUID;
import java.util.zip.Adler32;
import java.util.logging.Logger;

/**
 * This represents the UDP client which communicates to the UDP server over a given port and host
 * address.
 */
public class ClientUDP extends AbstractClient {

  private Logger logger;
  static private int TIMEOUT_MS = 5000;
  public ClientUDP(Logger logger){
    this.logger=logger;
  }
  @Override
  public void startClient(String serverIp, int portNum) {
    try (DatagramSocket aSocket = new DatagramSocket();
      BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in))) {
      InetAddress aHost = InetAddress.getByName(serverIp);

      initialOperations(aSocket, aHost, portNum);

      while (true) {
        String request = createRequestFromUserInput(inputReader, logger);
        if(request.length()==0) continue;


        sendRequestAndReceiveResponse(aSocket, request, aHost, portNum);

        System.out.print("Another Operation ?? (Y/N): ");
        String nextOperation = inputReader.readLine();
        if (nextOperation.equals("n") || nextOperation.equals("N")) {
          break;
        }
      }
    } catch (SocketException e) {
      System.out.println("Socket: " + e.getMessage());
    } catch (IOException e) {
      System.out.println("IO: " + e.getMessage());
    } catch (NumberFormatException e) {
      System.out.println("Invalid port number: " + e.getMessage());
    } catch (ArrayIndexOutOfBoundsException e) {
      System.out.println("Invalid arguments: " + e.getMessage());
    }
  }

  private long generateChecksum(String requestString) {
    byte [] m = requestString.getBytes();
    Adler32 adler32 = new Adler32();
    adler32.update(m, 0, m.length);
    return adler32.getValue();
  }

  private void sendRequestAndReceiveResponse(DatagramSocket aSocket, String requestString, InetAddress aHost,
      int serverPort) throws IOException {


    // creating datagram packet
    long checksumId = generateChecksum(requestString);
    requestString = checksumId + ":" + requestString;

    String requestId = requestString.split("\\|")[0];

    byte[] m = requestString.getBytes();
    DatagramPacket request = new DatagramPacket(m, m.length, aHost, serverPort);

    // sending datagram packet
    aSocket.send(request);

    // setting timeout of 5 seconds for udp request and waiting for response from server
    aSocket.setSoTimeout(TIMEOUT_MS);

    int max_size = aSocket.getReceiveBufferSize();

    byte[] buffer = new byte[max_size];
    DatagramPacket reply = new DatagramPacket(buffer, buffer.length);

    try {
      // receiving response from server
      aSocket.receive(reply);
      String response = new String(reply.getData(), 0, reply.getLength());


      // validating malformed responses from server
      if(response.contains(requestId)==false) {
        logger.warning("Received Malformed response for request: " + requestId);
      } else {
        if(response.contains("?")) response=response.replace("?","\n");
        logger.info("Received response " + response);
      }
    } catch(SocketTimeoutException e) {
      logger.warning("Request timed out... no response received from server for request: " + requestId);
    }
  }

  private void initialOperations(DatagramSocket aSocket, InetAddress aHost, int serverPort)
    throws IOException {
    final int KEYS = 1000;

    String requestId;
    // initial 1000 put requests to populate key-value data store
    for (int i = 1; i <= KEYS; i++) {
      requestId = UUID.randomUUID().toString();
      String putString = requestId + "|PUT|key" + i + "|value" + (2*i);
      logger.info("Sending request to server : PUT -> key " + i + " with value " + (2*i));
      sendRequestAndReceiveResponse(aSocket, putString, aHost, serverPort);
    }

    //get all request
    requestId = UUID.randomUUID().toString();
    String getAllString = requestId + "|" + "GETALL";
    logger.info("Sending request to server : GET ALL");
    sendRequestAndReceiveResponse(aSocket, getAllString, aHost, serverPort);

    //get request for all the keys in the data store
    for (int i = 1; i <= KEYS; i++) {
      requestId = UUID.randomUUID().toString();
      String getString = requestId + "|GET|key" + i;
      logger.info("Sending request to server : GET -> key " + i);
      sendRequestAndReceiveResponse(aSocket, getString, aHost, serverPort);
    }

    //delete request for some keys in data store
    for (int i = 1; i <= 5; i++) {
      requestId = UUID.randomUUID().toString();
      String getString = requestId + "|DELETE|key" + i;

      logger.info("Sending request to server : DELETE -> key " + i);
      sendRequestAndReceiveResponse(aSocket, getString, aHost, serverPort);
    }

    // size request to get the size of the data store
    requestId = UUID.randomUUID().toString();
    String sizeString = requestId + "|SIZE";
    logger.info("Sending request to server : SIZE ");
    sendRequestAndReceiveResponse(aSocket, sizeString, aHost, serverPort);
  }
}
