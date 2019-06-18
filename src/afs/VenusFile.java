// Clase de cliente que define la interfaz a las aplicaciones.
// Proporciona la misma API que RandomAccessFile.
package afs;

import java.rmi.*; 
import java.io.*; 

public class VenusFile {
    public static final String cacheDir = "Cache/";
    RandomAccessFile rnd;
    ViceWriter escritor;
    ViceReader lector;
    String modo;
    Venus ven;
    String file;
    private boolean escrito;
    private long tam_mod;
    public VenusFile(Venus venus, String fileName, String mode) throws RemoteException, IOException, FileNotFoundException {
        try{
            this.ven=venus;
            this.modo = mode;
            this.file=fileName;
            this.escrito = false;
            if(mode.equals("rw")){
                if(!buscar(file)){
                    rnd = new RandomAccessFile(cacheDir+fileName,mode);
                    lector = venus.rec.download(fileName,mode,ven.callback);
                    byte [] leidos;
                    while((leidos = lector.read(Integer.parseInt(venus.tam))) != null){
                        rnd.write(leidos);
                    }
                    rnd.seek(0);
                    lector.close();
                }
                else{
                    rnd = new RandomAccessFile(cacheDir+fileName,mode);
                }
                this.tam_mod = rnd.length();
            }
            else{
                rnd = new RandomAccessFile(cacheDir+fileName,mode);
            }
        }
        catch(FileNotFoundException e){
            lector = venus.rec.download(fileName,mode,ven.callback);
            rnd = new RandomAccessFile(cacheDir+fileName,"rw");
            byte [] leidos;
            while((leidos = lector.read(Integer.parseInt(venus.tam))) != null){
                rnd.write(leidos);
            }
            rnd.close();
            rnd = new RandomAccessFile(cacheDir+fileName, "r");
            lector.close();
        }
    }
    private boolean buscar(String filename){
        try{
            File cache = new File("Cache");
            for(File f: cache.listFiles()){
                if(f.getName().equals(filename)){
                    return true;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
    public int read(byte[] b) throws RemoteException, IOException {
        return rnd.read(b);
    }
    public void write(byte[] b) throws RemoteException, IOException {
        escrito = true;
        rnd.write(b);
        return;
    }
    public void seek(long p) throws RemoteException, IOException {
        rnd.seek(p);
        return;
    }
    public void setLength(long l) throws RemoteException, IOException {
        rnd.setLength(l);
        return;
    }
    public void close() throws RemoteException, IOException {
        if(this.modo.equals("rw")){
            if(this.escrito == true){
                rnd.seek(0);
                byte [] leidos = new byte[Integer.parseInt(ven.tam)];
                int leido = 0;
                escritor = ven.rec.upload(file,modo,ven.callback);
                while((leido = rnd.read(leidos)) != -1){
                    if(leido < Integer.parseInt(ven.tam)){
                        byte [] leidos2 = new byte[leido];
                        for(int i=0;i<leido;i++) leidos2[i]=leidos[i];
                        escritor.write(leidos2);
                    }
                    else{
                        escritor.write(leidos);
                    }
                }
                if(rnd.length() != this.tam_mod){
                    escritor.setLength(rnd.length());
                }
                escritor.close();
            }
            else if(rnd.length() != this.tam_mod){
                escritor = ven.rec.upload(file,modo,ven.callback);
                escritor.setLength(rnd.length());
                escritor.close();
            }
        }
        rnd.close();
    }
}

