// Interfaz de servidor que define los métodos remotos
// para completar la carga de un fichero
package afs;
import java.rmi.*;

public interface ViceWriter extends Remote {
    public void write(byte [] b) throws RemoteException;
    public void close() throws RemoteException;
    /* añada los métodos remotos que requiera */
}       

