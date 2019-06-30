package afs;
import java.rmi.*;
import java.io.*;

public interface ViceWriter extends Remote {
    void write(byte[] input) throws IOException;
    void close() throws IOException;
    void setLength(long len) throws IOException;
}   
