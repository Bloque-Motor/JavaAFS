// Clase de cliente que define la interfaz a las aplicaciones.
// Proporciona la misma API que RandomAccessFile.
package afs;

import java.rmi.*; 
import java.io.*; 

public class VenusFile {

    public static final String cacheDir = "Cache/";

    RandomAccessFile randomAccessFile;
    ViceWriter viceWriter;
    ViceReader viceReader;
    String mode;
    Venus venus;
    String fileName;
    private long sizeB;
    private boolean written;


    //TODO: Refactor
    public VenusFile(Venus venus, String fileName, String mode) throws IOException {

        try{
            this.venus = venus;
            this.mode = mode;
            this.fileName = fileName;
            this.written = false;

            if(mode.equals("rw")){
                
                if(!search(this.fileName)){
                    randomAccessFile = new RandomAccessFile(cacheDir + fileName, mode);
                    viceReader = venus.cl.download(fileName,mode, this.venus.callback);
                    byte [] leidos;
                    
                    while((leidos = viceReader.read(Integer.parseInt(venus.size))) != null){
                        randomAccessFile.write(leidos);
                    }
                    
                    randomAccessFile.seek(0);
                    viceReader.close();
                }
                else{
                    
                    randomAccessFile = new RandomAccessFile(cacheDir+fileName,mode);
                }
                this.sizeB = randomAccessFile.length();
            }
            else{
                
                randomAccessFile = new RandomAccessFile(cacheDir+fileName,mode);
            }
        }
        catch(FileNotFoundException e){
            
            viceReader = venus.cl.download(fileName,mode, this.venus.callback);
            randomAccessFile = new RandomAccessFile(cacheDir+fileName,"rw");
            byte [] leidos;
            
            while((leidos = viceReader.read(Integer.parseInt(venus.size))) != null){
                randomAccessFile.write(leidos);
            }
            
            randomAccessFile.close();
            randomAccessFile = new RandomAccessFile(cacheDir+fileName, "r");
            viceReader.close();
        }
    }

    private boolean search(String filename){
        try{
            File cacheFiles = new File("Cache");
            for (File file: cacheFiles.listFiles()){
                if(filename.equals(file.getName())){
                    return true;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public int read(byte[] output) throws IOException {
        return randomAccessFile.read(output);
    }

    public void write(byte[] input) throws IOException {
        written = true;
        randomAccessFile.write(input);
        return;
    }

    public void seek(long position) throws IOException {
        randomAccessFile.seek(position);
        return;
    }

    public void setLength(long length) throws IOException {
        randomAccessFile.setLength(length);
        return;
    }


    //TODO: Refactor
    public void close() throws IOException {
        if(mode.equals("rw")){
            if(written){
                randomAccessFile.seek(0);
                byte [] read = new byte[Integer.parseInt(venus.size)];
                int readCount = 0;
                viceWriter = venus.cl.upload(fileName, mode, venus.callback);
                while((readCount = randomAccessFile.read(read)) != -1){
                    if(readCount < Integer.parseInt(venus.size)){
                        byte [] leidos2 = new byte[readCount];
                        for(int i=0;i<readCount;i++) leidos2[i]=read[i];
                        viceWriter.write(leidos2);
                    }
                    else{
                        viceWriter.write(read);
                    }
                }
                if(randomAccessFile.length() != this.sizeB){
                    viceWriter.setLength(randomAccessFile.length());
                }
                viceWriter.close();
            }
            else if(randomAccessFile.length() != this.sizeB){
                viceWriter = venus.cl.upload(fileName, mode, venus.callback);
                viceWriter.setLength(randomAccessFile.length());
                viceWriter.close();
            }
        }
        randomAccessFile.close();
    }
}