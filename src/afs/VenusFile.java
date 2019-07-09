package afs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

public class VenusFile {

    private boolean modified;
    private static final String CACHE_DIR = "Cache/";
    private long fileSize;
    private Venus venus;
    private File file;
    private RandomAccessFile cachedFile;
    private String fileName;
    private String accessMode;

    public VenusFile(Venus venus, String fileName, String accessMode) throws IOException {

        ViceReader reader;
        try {
            this.venus = venus;
            file = new File(CACHE_DIR +fileName);
            this.fileName =fileName;
            this.accessMode = accessMode;
            this.modified = false;
            if(accessMode.equals("rw")){
                if(file.exists()){
                    System.out.println("File "+fileName+" is already on cache.");
                    cachedFile = new RandomAccessFile(file, accessMode);

                }
                else{
                    byte [] bytes;
                    cachedFile = new RandomAccessFile(file, accessMode);
                    reader = venus.getVice().download(fileName, accessMode, venus.getVenusCB());
                    while((bytes = reader.read(venus.getSize()))!= null){
                        cachedFile.write(bytes);
                    }
                    cachedFile.seek(0);
                    reader.close();
                }
                this.fileSize = cachedFile.length();
            }else{
                cachedFile = new RandomAccessFile(file, accessMode);
            }

        }
        catch (FileNotFoundException e) {
            byte [] bytes;
            reader = venus.getVice().download(fileName, accessMode, venus.getVenusCB());
            cachedFile = new RandomAccessFile(file,"rw");

            while((bytes = reader.read(venus.getSize()))!= null){
                cachedFile.write(bytes);
            }

            cachedFile.close();
            cachedFile = new RandomAccessFile(file, "r");

            reader.close();
        }
    }

    public int read(byte[] output) throws IOException {
        return cachedFile.read(output);
    }

    public void write(byte[] input) throws IOException {
        modified = true;
        cachedFile.write(input);
    }

    public void seek(long position) throws IOException {
        cachedFile.seek(position);
    }

    public void setLength(long l) throws IOException {
        cachedFile.setLength(l);
    }

    public void close() throws IOException {

        if(this.accessMode.equals("rw")){
            ViceWriter writer;
            if(!this.modified){
                if(this.fileSize != cachedFile.length()){
                    writer = venus.getVice().upload(fileName, accessMode, venus.getVenusCB());
                    writer.setLength(cachedFile.length());
                    writer.close();
                }
            }
            else{
                cachedFile.seek(0);
                byte [] buffer = new byte[venus.getSize()];
                int readCount = 0;
                writer = venus.getVice().upload(fileName, accessMode, venus.getVenusCB());
                while((readCount = cachedFile.read(buffer)) != -1){
                    if(venus.getSize() > readCount){
                        writer.write(Arrays.copyOf(buffer, readCount));
                    }
                    else{
                        writer.write(buffer);

                    }
                }
                if(this.fileSize != cachedFile.length()){
                    writer.setLength(cachedFile.length());
                }
                writer.close();
            }
        }
        cachedFile.close();
    }

}

