// Interfaz de servidor que define los métodos remotos
// para completar la descarga de un fichero
package afs;
import java.io.IOException;
import java.rmi.*;

public interface ViceReader extends Remote {
    byte[] read(int tam) throws IOException;
    void close() throws IOException;
}       

