package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * This is a common class for TCP and UDP clients with common functionality to create request from the given user input.
 */
public abstract class AbstractClient{

  /**
   * This function creates request to be sent to server based on the input from the user.
   * @param userInput Input provided by the user to create request.
   * @return request string to be sent to server for processing.
   * @throws IOException IOException while reading user input from bufferedReader
   */
  public String createRequestFromUserInput(BufferedReader userInput, Logger logger) throws IOException {
    System.out.println("Which operation do you want to use?");
    System.out.println("(Database is pre-populated with keys from 16-1000)");
    System.out.println("1. PUT");
    System.out.println("2. GET");
    System.out.println("3. DELETE");
    System.out.println("4. SIZE OF DATA");
    System.out.println("5. DELETE ALL DATA");
    System.out.println("6. GET ALL DATA");
    System.out.println("7. EXIT");
    System.out.print("Enter your choice (1-7): ");

    String input = userInput.readLine();

    String requestId = UUID.randomUUID().toString();

    String request = "";
    switch (input) {
      case "1":
        System.out.print("enter the key to put: ");
        String key = userInput.readLine();
        System.out.print("enter the value for the key: ");
        String value = userInput.readLine();
        request = requestId + "|" + "PUT|key"  + key + "|value" + value;
        logger.info("Sending request to server : PUT -> key " + key + " with value " + value);
        break;
      case "2":
        System.out.print("enter the key: ");
        key = userInput.readLine();
        request = requestId + "|" + "GET|key" + key;
        logger.info("Sending request to server : GET -> key " + key);
        break;
      case "3":
        System.out.print("enter the key to delete: ");
        key = userInput.readLine();
        request = requestId + "|" + "DELETE|key" + key;
        logger.info("Sending request to server : DELETE -> key " + key);
        break;
      case "4":
        request = requestId + "|" + "SIZE";
        logger.info("Sending request to server : SIZE ");
        break;
      case "5":
        request = requestId + "|" + "DELETEALL";
        logger.info("Sending request to server : DELETEALL ");
        break;
      case "6":
        request = requestId + "|" + "GETALL";
        logger.info("Sending request to server : GET ALL");
        break;
      case "7":
        System.out.println("bye bye");
        System.exit(0);
      default:
        System.out.println("Invalid choice. Please enter 1, 2, 3, 4, 5 or 6.");
    }
    return request;
  }

  /**
   * This function is to begin client to send requests and receive responses from server at given IP address and port
   * number
   * @param serverIp server's IP address
   * @param portNum server's port number
   */
  abstract void startClient(String serverIp, int portNum);
}
