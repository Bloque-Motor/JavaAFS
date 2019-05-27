// Implementación de la interfaz de servidor que define los métodos remotos
// para completar la carga de un fichero
package afs;
import java.rmi.*;
import java.rmi.server.*;

public class ViceWriterImpl extends UnicastRemoteObject implements ViceWriter {
    private static final String AFSDir = "AFSDir/";

    public ViceWriterImpl(String fileName /* añada los parámetros que requiera */)
		    throws RemoteException {
    }
    public void write(byte [] b) throws RemoteException {
        return;
    }
    public void close() throws RemoteException {
        return;
    }
}       

