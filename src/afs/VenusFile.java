package afs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

public class VenusFile {

    private static final String CACHE_DIR = "Cache/";
    private RandomAccessFile randomAccessFile;
    private String mode;
    private Venus venus;
    private String fileName;
    private long fileSize;
    private boolean written;


    public VenusFile(Venus venus, String fileName, String mode) throws IOException {
        ViceReader viceReader;
        this.venus = venus;
        this.mode = mode;
        this.fileName = fileName;
        this.written = false;
        randomAccessFile = new RandomAccessFile(CACHE_DIR + fileName, mode);
        viceReader = venus.getVice().download(fileName, mode, this.venus.getVenusCB());
        try{
            if(mode.equals("rw")){
                if(!search(this.fileName)){
                    byte [] readBytes;
                    while((readBytes = viceReader.read(venus.getSize())) != null){
                        randomAccessFile.write(readBytes);
                    }
                    randomAccessFile.seek(0);
                }
                this.fileSize = randomAccessFile.length();
            }
        }
        catch(FileNotFoundException e){
            randomAccessFile = new RandomAccessFile(CACHE_DIR + fileName,"rw");
            byte [] readBytes;
            while((readBytes = viceReader.read(venus.getSize())) != null){
                randomAccessFile.write(readBytes);
            }
            randomAccessFile.close();
            randomAccessFile = new RandomAccessFile(CACHE_DIR + fileName, "r");
        }
        viceReader.close();
    }

    private boolean search(String filename){
        try{
            File cacheFiles = new File(CACHE_DIR);
            if (cacheFiles.isDirectory()) {
                for (File file: cacheFiles.listFiles()){
                    if(filename.equals(file.getName())){
                        return true;
                    }
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
    }

    public void seek(long position) throws IOException {
        randomAccessFile.seek(position);
    }

    public void setLength(long length) throws IOException {
        randomAccessFile.setLength(length);
    }

    public void close() throws IOException {
        if(mode.equals("rw")){
            ViceWriter viceWriter = null;
            if(written){
                randomAccessFile.seek(0);
                byte [] read = new byte[venus.getSize()];
                int readCount;
                viceWriter = venus.getVice().upload(fileName, mode, venus.getVenusCB());
                while((readCount = randomAccessFile.read(read)) != -1){
                    if(venus.getSize() > readCount){
                        viceWriter.write(Arrays.copyOf(read, readCount));
                    } else{
                        viceWriter.write(read);
                    }
                }
                if(randomAccessFile.length() != fileSize){
                    viceWriter.setLength(randomAccessFile.length());
                }
            }
            else if(randomAccessFile.length() != fileSize){
                viceWriter = venus.getVice().upload(fileName, mode, venus.getVenusCB());
                viceWriter.setLength(randomAccessFile.length());
            }
            if (viceWriter != null) viceWriter.close();
        }
        randomAccessFile.close();
    }
}