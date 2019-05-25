package client;

import interfaces.Vice;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Venus {

    private static Vice stub = null;

    public static void main(String[] args) throws RemoteException {

        Registry registry = LocateRegistry.getRegistry();
        try {
            stub = (Vice) registry.lookup("Prism");
        } catch (NotBoundException e) {

        }

        //TODO: lo que haga el cliente
    }

}
