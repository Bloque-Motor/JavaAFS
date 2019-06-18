// Implementación de la interfaz de servidor que define los métodos remotos
// para iniciar la carga y descarga de ficheros
package afs;
import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.io.*;

public class ViceImpl extends UnicastRemoteObject implements Vice {
	private HashMap<String,ArrayList<VenusCB>> lista;
	public LockManager lock;

    public ViceImpl() throws RemoteException {
		lista = new HashMap<String,ArrayList<VenusCB>>();
		lock = new LockManager();
    }
    public synchronized ViceReader download(String fileName,String mode,VenusCB callback)
          throws RemoteException,FileNotFoundException {
			  ReentrantReadWriteLock cerrojo = lock.bind(fileName);
			  ViceReader res = new ViceReaderImpl(fileName,mode,this,cerrojo);
			  cerrojo.readLock().lock();
			  add(fileName, callback);
              return res;
    }
    public ViceWriter upload(String fileName,String mode,VenusCB callback)
          throws RemoteException,FileNotFoundException,IOException {
			ReentrantReadWriteLock cerrojo = lock.bind(fileName);
			  ViceWriter res = new ViceWriterImpl(fileName,mode,this,cerrojo,callback);
			  cerrojo.writeLock().lock();
              return res;
	}
	public void add(String file,VenusCB callb){
		if(lista.get(file)==null){
			lista.put(file,new ArrayList<VenusCB>());
		}
		lista.get(file).add(callb);
	}
	public void recorrer(String file,VenusCB me) throws RemoteException{
		for (Map.Entry<String,ArrayList<VenusCB>> entry : lista.entrySet()) {
			if(entry.getKey().equals(file)){
				for(int i=0;i<entry.getValue().size();i++){
					if(!entry.getValue().get(i).equals(me)){
						entry.getValue().get(i).invalidate(file);
						entry.getValue().remove(i);
						i--;
					}
				}
			}
		}
	}
}