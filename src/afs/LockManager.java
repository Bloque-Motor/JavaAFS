// Clase de servidor que implementa cerrojos de lectura/escritura para los ficheros
// NO MODIFICAR
package afs;

import java.util.HashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LockManager {
    private class InfoLock {
        public int count;
	public ReentrantReadWriteLock lock;
	InfoLock(int c, ReentrantReadWriteLock l) {
	    count=c; lock=l;
	}
    }
    private HashMap<String, InfoLock> mapLock;

    public LockManager() {
	    mapLock = new HashMap<String, InfoLock>();
    }
    public synchronized ReentrantReadWriteLock bind(String name) {
	InfoLock iLock;
        ReentrantReadWriteLock lock;
        if ((iLock = mapLock.get(name))==null){
            lock=new ReentrantReadWriteLock(true);
            mapLock.put(name, new InfoLock(1, lock));
	}
	else {
	    iLock.count++;
	    lock=iLock.lock;
	}
	return lock;
    }
    public synchronized void unbind(String name) {
	InfoLock iLock;
        if ((iLock = mapLock.get(name))!=null){
	    if (--iLock.count==0)
		mapLock.remove(name);
	}
    }
}


