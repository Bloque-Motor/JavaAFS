// Implementación de la interfaz de servidor que define los métodos remotos
// para completar la descarga de un fichero
package afs;
import java.rmi.*;
import java.rmi.server.*;
import java.io.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ViceReaderImpl extends UnicastRemoteObject implements ViceReader {
    private static final String AFSDir = "AFSDir/";
    RandomAccessFile rnd;
    ViceImpl lock;
    ReentrantReadWriteLock cerr;
    String file;

    public ViceReaderImpl(String fileName, String mode,ViceImpl vc,ReentrantReadWriteLock cerrojo)
		    throws RemoteException,FileNotFoundException {
                rnd = new RandomAccessFile(AFSDir+fileName,mode);
                lock = vc;
                cerr = cerrojo;
                file = fileName;
    }
    public byte[] read(int tam) throws RemoteException,IOException {
        byte [] buf = new byte[tam];
        int leido = rnd.read(buf);
        if(leido == -1){
            cerr.readLock().unlock();
            return null;
        }
        else if(leido < tam){
            byte [] leidos2 = new byte[leido];
            for(int i=0;i<leido;i++) leidos2[i]=buf[i];
            return leidos2;
        }
        return buf;
    }
    public void close() throws RemoteException,IOException {
        //cerr.readLock().unlock();
        lock.lock.unbind(file);
        rnd.close();
        return;
    }
}   