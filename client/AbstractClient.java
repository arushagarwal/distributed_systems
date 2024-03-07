package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.UUID;

public abstract class AbstractClient{
  public String generateRequestFromUserChoice(BufferedReader userInput) throws IOException {
    System.out.println("Which operation do you want to use?");
    System.out.println("1. PUT");
    System.out.println("2. GET");
    System.out.println("3. DELETE");
    System.out.println("4. SIZE OF DATA");
    System.out.println("5. DELETE ALL DATA");
    System.out.println("6. GET ALL DATA");
    System.out.print("Enter your choice (1/2/3/4/5/6): ");

    String choice = userInput.readLine();

    String request = "";
    switch (choice) {
      case "1":
        request = generatePutRequest(userInput);
        break;
      case "2":
        request = generateGetRequest(userInput);
        break;
      case "3":
        request = generateDeleteRequest(userInput);
        break;
      case "4":
        request = generateSizeRequest();
        break;
      case "5":
        request = generateDeleteAllRequest();
        break;
      case "6":
        request = generateGetAllRequest();
        break;
      default:
        System.out.println("Invalid choice. Please enter 1, 2, or 3.");
    }
    return request;
  }

  private String generateGetRequest(BufferedReader userInput) throws IOException {
    System.out.print("Please enter the key (integer): ");
    String key = userInput.readLine();
    String requestId = UUID.randomUUID().toString();
    return requestId + "::" + "GET" + "::" + key;
  }

  private String generatePutRequest(BufferedReader userInput) throws IOException {
    System.out.print("Please enter the key: ");
    String key = userInput.readLine();
    System.out.print("Please enter the value for the key: ");
    String value = userInput.readLine();
    String requestId = UUID.randomUUID().toString();
    return requestId + "::" + "PUT" + "::" + key + "::" + value;
  }

  private String generateDeleteRequest(BufferedReader userInput) throws IOException {
    System.out.print("Please enter the key (integer): ");
    String key = userInput.readLine();
    String requestId = UUID.randomUUID().toString();
    return requestId + "::" + "DELETE" + "::" + key;
  }
  private String generateSizeRequest(){
    String requestId = UUID.randomUUID().toString();
    return requestId + "::" + "SIZE";
  }

  private String generateDeleteAllRequest(){
    String requestId = UUID.randomUUID().toString();
    return requestId + "::" + "DELETEALL";
  }

  private String generateGetAllRequest(){
    String requestId = UUID.randomUUID().toString();
    return requestId + "::" + "GETALL";
  }

  /**
   * Starts client process to send request to server on given port and IP address
   * @param serverIp server's IP address to send request.
   * @param portNum server's port number on which it listens to client requests.
   */
  abstract void startClient(String serverIp, int portNum);
}
