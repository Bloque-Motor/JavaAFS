package afs;

import java.rmi.*;
import java.rmi.server.*;
import java.io.File;


public class VenusCBImpl extends UnicastRemoteObject implements VenusCB {
    public VenusCBImpl() throws RemoteException {
    }
    public void invalidate(String fileName) {

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