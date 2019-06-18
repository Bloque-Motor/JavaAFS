// Implementación de la interfaz de servidor que define los métodos remotos
// para completar la carga de un fichero
package afs;
import java.rmi.*;
import java.rmi.server.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.io.*;

public class ViceWriterImpl extends UnicastRemoteObject implements ViceWriter {
    private static final String AFSDir = "AFSDir/";
    RandomAccessFile rnd;
    ViceImpl lock;
    ReentrantReadWriteLock cerr;
    String file;
    VenusCB callb;

    public ViceWriterImpl(String fileName, String mode,ViceImpl vc,ReentrantReadWriteLock cerrojo,VenusCB callback)
		    throws RemoteException,FileNotFoundException,IOException {
                rnd = new RandomAccessFile(AFSDir+fileName,mode);
                lock = vc;
                file = fileName;
                cerr = cerrojo;
                callb = callback;
                rnd.seek(0);
    }
    public void write(byte [] b) throws RemoteException,IOException {
        rnd.write(b);
        return;
    }
    public void setLength(long len) throws RemoteException, IOException{
        rnd.setLength(len);
        return;
    }
    public void close() throws RemoteException,IOException {
        lock.recorrer(file,callb);
        cerr.writeLock().unlock();
        lock.lock.unbind(file);
        rnd.close();
        return;
    }
}     

