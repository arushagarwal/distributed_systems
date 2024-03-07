package server;

/**
 * Abstract class which is a common class for TCP and UDP server which contains common functionalities like to
 * process request from client and also provide definition of function to listen to client requests
 */
public abstract class AbstractServer{
  private static final KeyValueDataStore data_store = new KeyValueDataStore();

  /**
   * Processes the input request parsed from handleRequest method.
   * @param inputLine input request as parsed from handleRequest.
   */
  public String processClientRequest(String inputLine) {
    String[] requestComponents = inputLine.split("\\|");
    System.out.println("request : "+inputLine);
    if (requestComponents.length < 2) {
      return "Invalid request format";
    }

    String requestId = requestComponents[0];
    String action = requestComponents[1].toUpperCase();
    String key = requestComponents.length == 2 ? null : requestComponents[2];
    String value = requestComponents.length <= 3 ? null : requestComponents[3];

    switch (action) {
      case "PUT":
        if (key==null || value == null) {
          return requestId + ": Invalid PUT request";
        }
        data_store.put(key, value);
        return requestId + ": Key : " + key + ",Value : " + value + " successfully added to database";
      case "GET":
        String getValue = data_store.get(key);
        if(getValue==null) return requestId+" : Value not found for key : "+key;
        else return requestId + ": Value for the key : "+key+" : "+getValue;
      case "DELETE":
        String deletedValue = data_store.delete(key);
        if(deletedValue == null) return requestId+": Value not found for key : "+key;
        else return requestId+" : Value deleted successfully for key : "+key+" with previous value : "+deletedValue;
      case "SIZE":
          int size = data_store.size();
          return requestId + ": Size of database : "+size;
      case "DELETEALL":
        data_store.deleteAll();
          boolean val = data_store.size()==0;
          return requestId + ": Data Delete All " + (val?"Successful":"Unsuccessful");
      case "GETALL":
          String pairs = data_store.getAll();
          return requestId + ":" +pairs;
      default:
        return requestId + ": "+action+" operation not supported by the server ";
    }
  }

  /**
   * This function starts server and listens for requests from clients.
   */
  abstract void startAndListen(int portNumber);
}
