package afs;
import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.io.*;

public class ViceImpl extends UnicastRemoteObject implements Vice {

	private HashMap<String, ArrayList<VenusCB>> fileList;
	private LockManager lockManager;

	public ViceImpl() throws RemoteException {
		fileList = new HashMap<>();
		lockManager = new LockManager();
	}

	public synchronized ViceReader download(String fileName, String mode, VenusCB venusCallback) throws RemoteException, FileNotFoundException {
		ReentrantReadWriteLock reentrantReadWriteLock = lockManager.bind(fileName);
		ViceReader viceReader = new ViceReaderImpl(fileName, mode, this ,reentrantReadWriteLock);
		reentrantReadWriteLock.readLock().lock();
		addFile(fileName, venusCallback);
		return viceReader;
	}

	public ViceWriter upload(String fileName,String mode,VenusCB venusCallback) throws IOException {
		ReentrantReadWriteLock reentrantReadWriteLock = lockManager.bind(fileName);
		ViceWriter viceWriter = new ViceWriterImpl(fileName, mode,this,reentrantReadWriteLock, venusCallback);
		reentrantReadWriteLock.writeLock().lock();
		return viceWriter;
	}

	public void addFile (String fileName, VenusCB venusCallback){
		if(fileList.get(fileName)==null){
			fileList.put(fileName, new ArrayList<>());
		}
		fileList.get(fileName).add(venusCallback);
	}

	public void close(String fileName, VenusCB venusCallback) throws RemoteException{
		if(!fileList.containsKey(fileName)) return;
		ArrayList<VenusCB> entry = fileList.get(fileName);
		Iterator it = entry.iterator();
		while (it.hasNext())
		{
			VenusCB venusCB = (VenusCB) it.next();
			if(!venusCB.equals(venusCallback)){
				venusCB.invalidate(fileName);
				it.remove();
			}
		}
	}

	public LockManager getLockManager() {
		return lockManager;
	}
}