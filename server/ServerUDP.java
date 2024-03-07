package server;
import java.net.*;
import java.io.*;
import java.util.Arrays;
import java.util.zip.Adler32;
import java.util.zip.CRC32;
import java.util.zip.Checksum;
import java.util.logging.Logger;

/**
 * This represents a UDP server which receives datagram requests, validates them and sends responses
 * to the client. This class extends the abstract server class which has the common implementation
 * to handle key value requests.
 */
public class ServerUDP extends AbstractServer {

  private Logger logger;
  public ServerUDP(Logger logger){
    this.logger=logger;
  }
  @Override
  public void startAndListen(int portNumber) {

    // Server socket creation on specified port number.
    try (DatagramSocket aSocket = new DatagramSocket(portNumber)) {
      System.out.println("Server active on port " + portNumber);
      logger.info("Server active on port " + portNumber);

      while (true) {
        byte[] buffer = new byte[1000];

        // Receiving datagram request
        DatagramPacket request = new DatagramPacket(buffer,
          buffer.length);
        aSocket.receive(request);

        // Checking the request packet on server and validating packet.
        if (!validRequest(request)) {
          String response = "Couldn't process request.";
          logger.warning(" - Received Malformed request from " + request.getAddress() + " of length " + request.getLength());
          DatagramPacket reply = new DatagramPacket(response.getBytes(),
            response.getBytes().length, request.getAddress(), request.getPort());
          aSocket.send(reply);
          continue;
        }

        // processing request
        String msg = new String(request.getData(), 0, request.getLength());
        logger.info(" - Request from : " + request.getAddress() + ": "+msg);

        // process request from the key value store
        String response = processClientRequest(msg);

        // sending response back to client

        // for GET ALL requests
        if(response.contains("?")){
          int max_size = aSocket.getSendBufferSize();
          String[] tokens = response.split("\\?");
          StringBuilder maxDataInPacket = new StringBuilder();
          for(String token : tokens){
              if(maxDataInPacket.length()+token.length()<max_size/2) maxDataInPacket.append(token+"?");
              else{
                maxDataInPacket.deleteCharAt(maxDataInPacket.length() - 1);
                DatagramPacket reply = new DatagramPacket(response.getBytes(),
                        response.getBytes().length, request.getAddress(), request.getPort());
                aSocket.send(reply);
                logger.info(" Response to : " + reply.getAddress() + ": " + response);
                maxDataInPacket = new StringBuilder();
              }
          }

          // for the last packet containing data
          maxDataInPacket.deleteCharAt(maxDataInPacket.length() - 1);
          DatagramPacket reply = new DatagramPacket(response.getBytes(),
                  response.getBytes().length, request.getAddress(), request.getPort());
          aSocket.send(reply);
          logger.info(" Response to : " + reply.getAddress() + ": " + response);
        }
        // rest all other requests
        else{
          DatagramPacket reply = new DatagramPacket(response.getBytes(),
                  response.getBytes().length, request.getAddress(), request.getPort());
          aSocket.send(reply);
          logger.info(" Response to : " + reply.getAddress() + ": " + response);
        }
      }
    } catch (SocketException e) {
      System.out.println("Socket: " + e.getMessage());
    } catch (IOException e) {
      System.out.println("IO: " + e.getMessage());
    }
  }

  /**
   * To generate checksum for the given string.
   * @param requestString String for which checksum needs to be generated.
   * @return checksum value in long format.
   */
  private long generateChecksum(String requestString) {
    byte [] m = requestString.getBytes();
    Adler32 adler32 = new Adler32();
    adler32.update(m, 0, m.length);
    return adler32.getValue();
  }

  /**
   * Validating given datagram packet received from client.
   * @param request Datagram request.
   * @return boolean indicating request is valid or not.
   */
  private boolean validRequest(DatagramPacket request) {

    String requestData = new String(request.getData(), 0, request.getLength());
    String[] parts = requestData.split("\\|");

    if (parts.length < 2) {
      return false;
    }

    if (parts[0].isEmpty() || parts[1].isEmpty()) {
      return false;
    }

    String checkSumId = parts[0].split(":")[0];

    long responseRequestId = Long.parseLong(checkSumId);

    String requestStringWithoutCheckSum = requestData.substring(checkSumId.length()+1);

    // compare checksums, if not equal means malformed request.
    return responseRequestId == generateChecksum(requestStringWithoutCheckSum);
  }
  
}