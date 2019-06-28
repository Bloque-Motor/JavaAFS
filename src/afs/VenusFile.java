// Clase de cliente que define la interfaz a las aplicaciones.
// Proporciona la misma API que RandomAccessFile.
package afs;

import java.rmi.*; 
import java.io.*; 

public class VenusFile {

    public static final String cacheDir = "Cache/";

    RandomAccessFile raf;
    ViceWriter writer;
    ViceReader reader;
    String modo;
    Venus ven;
    String file;
    private long sizeB;
    private boolean escrito;

    public VenusFile(Venus venus, String fileName, String mode) throws RemoteException, IOException, FileNotFoundException {
        
        try{
            
            this.ven = venus;
            this.modo = mode;
            this.file = fileName;
            this.escrito = false;

            if(mode.equals("rw")){
                
                if(!buscar(file)){

                    raf = new RandomAccessFile(cacheDir+fileName,mode);
                    reader = venus.cl.download(fileName,mode,ven.callback);
                    byte [] leidos;
                    
                    while((leidos = reader.read(Integer.parseInt(venus.size))) != null){
                        raf.write(leidos);
                    }
                    
                    raf.seek(0);
                    reader.close();
                }
                else{
                    
                    raf = new RandomAccessFile(cacheDir+fileName,mode);
                }
                this.sizeB = raf.length();
            }
            else{
                
                raf = new RandomAccessFile(cacheDir+fileName,mode);
            }
        }
        catch(FileNotFoundException e){
            
            reader = venus.cl.download(fileName,mode,ven.callback);
            raf = new RandomAccessFile(cacheDir+fileName,"rw");
            byte [] leidos;
            
            while((leidos = reader.read(Integer.parseInt(venus.size))) != null){
                raf.write(leidos);
            }
            
            raf.close();
            raf = new RandomAccessFile(cacheDir+fileName, "r");
            reader.close();
        }
    }

    private boolean search(String filename){

        try{

            File cacheFiles = new File("Cache");

            for(File fileN: cacheFiles.listFiles()){

                if(fileN.getName().equals(filename)){

                    return true;
                }
            }
        }catch(Exception e){

            e.printStackTrace();
        }

        return false;
    }

    public int read(byte[] b) throws RemoteException, IOException {

        return raf.read(b);
    }

    public void write(byte[] b) throws RemoteException, IOException {

        escrito = true;
        raf.write(b);
        return;
    }

    public void seek(long p) throws RemoteException, IOException {

        raf.seek(p);
        return;
    }

    public void setLength(long l) throws RemoteException, IOException {

        raf.setLength(l);
        return;
    }

    public void close() throws RemoteException, IOException {

        if(this.modo.equals("rw")){
            if(this.escrito == true){
                raf.seek(0);
                byte [] leidos = new byte[Integer.parseInt(ven.size)];
                int leido = 0;
                writer = ven.cl.upload(file,modo,ven.callback);
                while((leido = raf.read(leidos)) != -1){
                    if(leido < Integer.parseInt(ven.size)){
                        byte [] leidos2 = new byte[leido];
                        for(int i=0;i<leido;i++) leidos2[i]=leidos[i];
                        writer.write(leidos2);
                    }
                    else{
                        writer.write(leidos);
                    }
                }
                if(raf.length() != this.sizeB){
                    writer.setLength(raf.length());
                }
                writer.close();
            }
            else if(raf.length() != this.sizeB){
                writer = ven.cl.upload(file,modo,ven.callback);
                writer.setLength(raf.length());
                writer.close();
            }
        }
        raf.close();
    }
}