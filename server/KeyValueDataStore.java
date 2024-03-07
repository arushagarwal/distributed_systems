package server;

import java.util.*;

/**
 * This class is a wrapper for the hashmap which will behave as a key value data store and provides functionality to
 * intreact with the data store.
 */
class KeyValueDataStore {
  private final HashMap<String, String> store;

  /**
   * Initializing the key value data store.
   */
  public KeyValueDataStore() {
    this.store = new HashMap<>();
  }

    /**
     * Inserts data in the data store with the provided values.
     * @param key key of the pair
     * @param value value of the pair
     */
  public synchronized void put(String key, String value) {
    store.put(key, value);
  }

    /**
     * Returns corresponding value of the asked key from the data store.
     * @param key asked key for which value is returned.
     * @return value for the asked key, NULL if value not present.
     */
  public synchronized String get(String key) {
    return store.get(key);
  }

    /**
     * Deletes the key value pair from the data store.
     * @param key key for the deleting pair
     * @return previous value for the deleted key, value pair
     */
  public synchronized String delete(String key) {
    return store.remove(key);
  }

    /**
     * Returns number of key, value pairs in the data store.
     * @return number of key, value pairs
     */
  public synchronized int size() { return store.size();}

  /**
   * Deletes all the data of the data store.
   */

  public synchronized void deleteAll() { store.clear();}

  /**
   * Gets all of the key-value pairs from the data store.
   * @return A message containing all the key-value pairs in the data store.
   */
  public synchronized String getAll() {
      StringBuilder keyValuePairs = new StringBuilder();
      keyValuePairs.append("ALL VALUES IN DATA STORE : ?");
      for(String key : store.keySet()){
          String value = store.get(key);
          String pair = "key : "+key+", value : "+value+"?";
          keyValuePairs.append(pair);
      }
      if(store.keySet().size()==0) keyValuePairs.append("Empty Data Store");
      return keyValuePairs.toString();
  }
}
