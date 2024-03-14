package RMI_Server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DataStore<K,V> extends Remote {

    void put(K key, V value) throws RemoteException;

    V get(K key) throws RemoteException;

    V delete(K key) throws RemoteException;
}
