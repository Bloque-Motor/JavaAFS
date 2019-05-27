// Implementación de la interfaz de servidor que define los métodos remotos
// para completar la descarga de un fichero
package afs;
import java.rmi.*;
import java.rmi.server.*;

public class ViceReaderImpl extends UnicastRemoteObject implements ViceReader {
    private static final String AFSDir = "AFSDir/";

    public ViceReaderImpl(String fileName /* añada los parámetros que requiera */)
		    throws RemoteException {
    }
    public byte[] read(int tam) throws RemoteException {
        return null;
    }
    public void close() throws RemoteException {
        return;
    }
}       

