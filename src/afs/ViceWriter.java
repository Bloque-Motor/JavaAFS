// Interfaz de servidor que define los métodos remotos
// para completar la carga de un fichero
package afs;
import java.rmi.*;
import java.io.*;

public interface ViceWriter extends Remote {
    public void write(byte [] b) throws RemoteException,IOException;
    public void close() throws RemoteException,IOException;
    public void setLength(long len) throws RemoteException,IOException;
    /* añada los métodos remotos que requiera */
}   
