package afs;
import java.rmi.*;
import java.io.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ViceReaderImpl extends UnicastRemoteObject implements ViceReader {
    private static final String directory = "AFSDir/";
    private RandomAccessFile randomAccessFile;
    private ViceImpl vice;
    private ReentrantReadWriteLock reentrantReadWriteLock;
    private String fileName;

    public ViceReaderImpl(String fileName, String mode, ViceImpl vice, ReentrantReadWriteLock reentrantReadWriteLock) throws RemoteException,FileNotFoundException {
        this.fileName = fileName;
        this.vice = vice;
        this.reentrantReadWriteLock = reentrantReadWriteLock;
        randomAccessFile = new RandomAccessFile(directory + fileName, mode);
    }

    public byte[] read(int bufferSize) throws IOException {
        byte [] readBuffer = new byte[bufferSize];
        int readCount = randomAccessFile.read(readBuffer);
        if(readCount == -1){
            reentrantReadWriteLock.readLock().unlock();
            return null;
        }
        
        if(readCount < bufferSize){
            return Arrays.copyOf(readBuffer, readCount);
        }
        return readBuffer;
    }

    public void close() throws IOException {
        vice.getLockManager().unbind(this.fileName);
        randomAccessFile.close();
        return;
    }
}   