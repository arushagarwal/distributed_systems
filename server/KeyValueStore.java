package server;

import java.util.*;

/**
 * This class represents the actual key value data store with all the associated operations that
 * can be performed on it.
 */
class KeyValueStore {
  private final HashMap<String, String> store;

  /**
   * Initializes the key value data store.
   */
  public KeyValueStore() {
    this.store = new HashMap<>();
  }

  /**
   * Inserts a given key and assigns it a given value in the data store.
   *
   * @param key The key to be stored.
   * @param value The associated value to be stored.
   */
  public synchronized void put(String key, String value) {
    store.put(key, value);
  }

  /**
   * Gets the value for a given key from the data store.
   * @param key the key whose value is to be fetched.
   * @return A message containing the corresponding value stored against the key in the data store.
   */
  public synchronized String get(String key) {
    return store.get(key);
  }

  /**
   * Deletes the given key and its corresponding value from the data store.
   * @param key the key to be deleted.
   * @return A message indicating whether the operation is successful or not.
   */
  public synchronized String delete(String key) {
    return store.remove(key);
  }

  /**
   * Gets the size of the data store
   * @return A message containing size of the data store.
   */
  public synchronized int size() { return store.size();}

  /**
   * Deletes all the data of the data store
   * @return Nothing.
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
