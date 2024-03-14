package RMI_Server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

class KeyValueDataStore<K,V>  implements DataStore<K,V> {
    private final ConcurrentMap<K, V> store;

    /**
     * Initializing the key value data store.
     */
    public KeyValueDataStore() throws RemoteException {
        super();
        this.store = new ConcurrentHashMap<>();
    }

    /**
     * Inserts data in the data store with the provided values.
     * @param key key of the pair
     * @param value value of the pair
     */
    public synchronized void put(K key, V value) throws RemoteException {
        store.put(key, value);
    }

    /**
     * Returns corresponding value of the asked key from the data store.
     * @param key asked key for which value is returned.
     * @return value for the asked key, NULL if value not present.
     */
    public synchronized V get(K key) throws RemoteException {
        return store.get(key);
    }

    /**
     * Deletes the key value pair from the data store.
     * @param key key for the deleting pair
     * @return previous value for the deleted key, value pair
     */
    public synchronized V delete(K key) throws RemoteException {
        return store.remove(key);
    }

}
