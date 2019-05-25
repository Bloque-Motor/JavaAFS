package server;

import interfaces.Vice;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Main {

    public Main() {}

    public static void main(String args[]) {
        try {
            // Instantiating the implementation class
            Vice obj = new ViceImpl();

            // Exporting the object of implementation class (here we are exporting the remote object to the stub)
            Vice stub = (ViceImpl) UnicastRemoteObject.exportObject(obj, 0);

            // Binding the remote object (stub) in the registry
            LocateRegistry.createRegistry(1099);
            Registry registry = LocateRegistry.getRegistry();

            registry.bind("ViceImpl", stub);

            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
