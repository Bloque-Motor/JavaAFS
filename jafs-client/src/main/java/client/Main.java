package client;

import interfaces.AFSI;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {

    private static AFSI stub = null;

    public static void main(String[] args) throws RemoteException {

        Registry registry = LocateRegistry.getRegistry();
        try {
            stub = (AFSI) registry.lookup("Prism");
        } catch (NotBoundException e) {

        }

        //TODO: lo que haga el cliente
    }

}
