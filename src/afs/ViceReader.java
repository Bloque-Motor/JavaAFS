// Interfaz de servidor que define los métodos remotos
// para completar la descarga de un fichero
package afs;
import java.rmi.*;

public interface ViceReader extends Remote {
    public byte[] read(int tam) throws RemoteException;
    public void close() throws RemoteException;
    /* añada los métodos remotos que requiera */
}       

