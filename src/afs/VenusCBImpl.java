// Implementación de la interfaz de cliente que define los métodos remotos
// para gestionar callbacks
package afs;

import java.rmi.*;
import java.rmi.server.*;
import java.io.File;


public class VenusCBImpl extends UnicastRemoteObject implements VenusCB {
    public VenusCBImpl() throws RemoteException {
    }
    public void invalidate(String fileName /* añada los parámetros que requiera */)
        throws RemoteException {

        try{
                File cacheFiles = new File("Cache");

                for(File fileN: cacheFiles.listFiles()){

                    if(fileN.getName().equals(fileName)){

                        fileN.delete();
                        break;
                    }
                }               
            }catch(Exception e){
                
                e.printStackTrace();
            }

        return;
    }
}