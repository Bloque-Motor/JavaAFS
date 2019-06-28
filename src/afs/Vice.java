package afs;
import java.rmi.*;
import java.io.*;

public interface Vice extends Remote {

    ViceReader download(String fileName, String mode, VenusCB venusCallback) throws IOException;

    ViceWriter upload(String fileName, String mode, VenusCB venusCallback) throws IOException;

}