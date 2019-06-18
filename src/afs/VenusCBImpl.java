// Implementación de la interfaz de cliente que define los métodos remotos
// para gestionar callbacks
package afs;

import java.io.File;
import java.rmi.*;
import java.rmi.server.*;

public class VenusCBImpl extends UnicastRemoteObject implements VenusCB {
    public VenusCBImpl() throws RemoteException {
    }
    public void invalidate(String fileName /* añada los parámetros que requiera */)
        throws RemoteException {
            try{
                File cache = new File("Cache");
                for(File f: cache.listFiles()){
                    if(f.getName().equals(fileName)){
                        f.delete();
                        break;
                    }
                }               
            }catch(Exception e){
                
                e.printStackTrace();
            }
        return;
    }
}
