package server;

import interfaces.AFSI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Main {

    public Main() {}

    public static void main(String args[]) {
        try {
            // Instantiating the implementation class
            AFSI obj = new AFS();

            // Exporting the object of implementation class (here we are exporting the remote object to the stub)
            AFSI stub = (AFS) UnicastRemoteObject.exportObject(obj, 0);

            // Binding the remote object (stub) in the registry
            LocateRegistry.createRegistry(1099);
            Registry registry = LocateRegistry.getRegistry();

            registry.bind("AFS", stub);

            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
