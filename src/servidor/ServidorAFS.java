package servidor;// Clase de servidor que instancia y registra el servicio remoto
// de acceso a ficheros.
// NO MODIFICAR
import java.rmi.*;
import java.rmi.server.*;
import afs.*;

class ServidorAFS  {

    static public void main (String args[])  {
        if (args.length!=1) {
            System.err.println("Uso: ServidorAFS numPuertoRegistro");
            return;
        }
        if (System.getSecurityManager() == null)
            System.setSecurityManager(new SecurityManager());

        try {
            Vice srv = new ViceImpl();
            Naming.rebind("rmi://localhost:" + args[0] + "/AFS", srv);
        }
        catch (RemoteException e) {
            System.err.println("Error de comunicacion: " + e.toString());
            System.exit(1);
        }
        catch (Exception e) {
            System.err.println("Excepcion en ServidorAFS:");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
