package afs;

import java.rmi.*;

public interface VenusCB extends Remote {
    void invalidate(String fileName)
        throws RemoteException;
}

