package server;
import java.net.*;
import java.io.*;
import java.util.Arrays;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

/**
 * This represents a UDP server which receives datagram requests, validates them and sends responses
 * to the client. This class extends the abstract server class which has the common implementation
 * to handle key value requests.
 */
public class UDPServer extends AbstractServer {
  @Override
  public void listen(int portNumber) {

    // Server socket creation on specified port number.
    try (DatagramSocket aSocket = new DatagramSocket(portNumber)) {
      System.out.println("Server active on port " + portNumber);
      serverLogger.log("Server active on port " + portNumber);

      while (true) {
        byte[] buffer = new byte[1000];

        // Receiving datagram request
        DatagramPacket request = new DatagramPacket(buffer,
          buffer.length);
        aSocket.receive(request);

        // Validating requests on server side.
        if (!validRequest(request)) {
          String response = "Couldn't process request.";
          serverLogger.logMalformedRequest(request.getAddress(), request.getLength());
          DatagramPacket reply = new DatagramPacket(response.getBytes(),
            response.getBytes().length, request.getAddress(), request.getPort());
          aSocket.send(reply);
          continue;
        }

        // parsing and processing request
        String msg = new String(request.getData(), 0, request.getLength());
        serverLogger.logRequest(request.getAddress(), msg);

        // process request from the key value store
        String response = processRequest(msg);

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
                serverLogger.logResponse(reply.getAddress(),response);
                maxDataInPacket = new StringBuilder();
              }
          }

          // for the last packet containing data
          maxDataInPacket.deleteCharAt(maxDataInPacket.length() - 1);
          DatagramPacket reply = new DatagramPacket(response.getBytes(),
                  response.getBytes().length, request.getAddress(), request.getPort());
          aSocket.send(reply);
          serverLogger.logResponse(reply.getAddress(),response);
        }
        // rest all other requests
        else{
          DatagramPacket reply = new DatagramPacket(response.getBytes(),
                  response.getBytes().length, request.getAddress(), request.getPort());
          aSocket.send(reply);
          serverLogger.logResponse(reply.getAddress(),response);
        }
      }
    } catch (SocketException e) {
      System.out.println("Socket: " + e.getMessage());
    } catch (IOException e) {
      System.out.println("IO: " + e.getMessage());
    }
  }

  private static long generateChecksum(String[] requestParts) {
    String result = String.join("::", Arrays.copyOfRange(requestParts, 1, requestParts.length));

    byte [] m = result.getBytes();
    Checksum crc32 = new CRC32();
    crc32.update(m, 0, m.length);
    return crc32.getValue();
  }

  /**
   * This function validates a given datagram packet, if it is as per protocol and is not corrupted.
   * @param request Datagram request.
   * @return boolean indicating request is valid or not.
   */
  private boolean validRequest(DatagramPacket request) {

    String requestData = new String(request.getData(), 0, request.getLength());
    String[] parts = requestData.split("::");

    if (parts.length < 3) {
      return false;
    }

    if (parts[0].isEmpty() || parts[1].isEmpty()) {
      return false;
    }

    long responseRequestId = Long.parseLong(parts[0]);

    // compare checksums, if not equal means malformed request.
    return responseRequestId == generateChecksum(parts);
  }
}