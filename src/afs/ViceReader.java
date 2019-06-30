package afs;
import java.io.IOException;
import java.rmi.*;

public interface ViceReader extends Remote {
    byte[] read(int size) throws IOException;
    void close() throws IOException;
}       

