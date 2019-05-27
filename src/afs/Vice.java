// Interfaz de servidor que define los métodos remotos para iniciar
// la carga y descarga de ficheros
package afs;
import java.rmi.*;

public interface Vice extends Remote {
    public ViceReader download(String fileName /* añada los parámetros que requiera */)
          throws RemoteException;
    public ViceWriter upload(String fileName /* añada los parámetros que requiera */)
          throws RemoteException;

    /* añada los métodos remotos que requiera */
}
       

