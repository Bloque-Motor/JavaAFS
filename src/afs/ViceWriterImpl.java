package afs;
import java.rmi.server.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.io.*;

public class ViceWriterImpl extends UnicastRemoteObject implements ViceWriter {
    private static final String AFSDir = "AFSDir/";
    VenusCB venusCallback;
    private RandomAccessFile randomAccessFile;
    private ViceImpl vice;
    private ReentrantReadWriteLock reentrantReadWriteLock;
    private String fileName;

    public ViceWriterImpl(String fileName, String mode, ViceImpl vice, ReentrantReadWriteLock reentrantReadWriteLock, VenusCB venusCallback) throws IOException {
        this.fileName = fileName;
        this.vice = vice;
        this.reentrantReadWriteLock = reentrantReadWriteLock;
        this.venusCallback = venusCallback;
        randomAccessFile = new RandomAccessFile(AFSDir+fileName,mode);
        randomAccessFile.seek(0);
    }

    public void setLength(long length) throws IOException{
        randomAccessFile.setLength(length);
        return;
    }

    public void write(byte[] b) throws IOException {
        randomAccessFile.write(b);
        return;
    }

    public void close() throws IOException {
        vice.recorrer(fileName,venusCallback);
        reentrantReadWriteLock.writeLock().unlock();
        vice.lock.unbind(fileName);
        randomAccessFile.close();
        return;
    }
}     

