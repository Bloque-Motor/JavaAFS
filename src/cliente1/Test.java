package cliente1;
import java.util.*;
import java.io.*;
import afs.*;
import java.rmi.*;

class Test {
    static byte val = 'A';
    static HashMap<Integer, VenusFile> descriptorMap = new HashMap<Integer, VenusFile>();
    static Venus venus;
    static int desc=0;

    static private boolean doOpen(Scanner ent) throws IOException, RemoteException {
        try {
            System.out.println("Introduzca nombre de fichero y modo: r (defecto) o rw");
            if (!ent.hasNextLine()) return false;
            String lin = ent.nextLine();
            Scanner s = new Scanner(lin);
            if (!s.hasNext()) return false;
            String fich = s.next();
            String modo ="r";
            if ((s.hasNext()) && s.next().equals("rw")) modo ="rw";
            VenusFile f = new VenusFile(venus, fich, modo);
            descriptorMap.put(desc, f);
            System.out.println("Fichero abierto ID " + desc++);
        }
        catch (FileNotFoundException e) {
            System.out.println("Fichero no existente");
        }
        return true;
    }

    static private boolean doRead(Scanner ent) throws RemoteException {
        boolean res = false;
        try {
            System.out.println("Introduzca ID de fichero y cantidad a leer");
            if (!ent.hasNextLine()) return false;
            String lin = ent.nextLine();
            Scanner s = new Scanner(lin);
            if (s.hasNextInt()) {
                int ID = s.nextInt();
                if (s.hasNextInt()) {
                    int tam = s.nextInt();
                    res = true;
                    VenusFile f = descriptorMap.get(ID);
                    if (f ==null)
                        System.out.println("ID de fichero invalido");
                    else {
                        byte [] buf = new byte[tam];
                        int leido = f.read(buf);
                        System.out.println("Leidos " + leido + " bytes");
                        if (leido>0) {
                            System.out.write(buf, 0, leido);
                            System.out.println();
                        }
                    }
               }
            }
        }
        catch (IOException e) {
            System.out.println("Excepcion de E/S");
        }
        return res;
    }

    static private boolean doWrite(Scanner ent) throws RemoteException {
        boolean res = false;
        try {
            System.out.println("Introduzca ID de fichero, cantidad a escribir y caracter que se escribira en el fichero");
            if (!ent.hasNextLine()) return false;
            String lin = ent.nextLine();
            Scanner s = new Scanner(lin);
            if (s.hasNextInt()) {
                int ID = s.nextInt();
                if (s.hasNextInt()) {
                    int tam = s.nextInt();
                    if (s.hasNext()) {
                        String cad = s.next();
                        res = true;
                        VenusFile f = descriptorMap.get(ID);
                        if (f ==null)
                            System.out.println("ID de fichero invalido");
                        else {
                            char c = cad.charAt(0);
                            byte [] buf = new byte[tam];
                            for (int i=0; i<tam; i++) buf[i] = (byte)c;
                            f.write(buf);
                            System.out.println("Escritos " +tam+ " bytes con valor: " + c);
                        }
                   }
               }
            }
        }
        catch (IOException e) {
            System.out.println("Excepcion de E/S");
        }
        return res;
    }
    static private boolean doSeek(Scanner ent) throws RemoteException {
        boolean res = false;
        try {
            System.out.println("Introduzca ID de fichero y nueva posicion");
            if (!ent.hasNextLine()) return false;
            String lin = ent.nextLine();
            Scanner s = new Scanner(lin);
            if (s.hasNextInt()) {
                int ID = s.nextInt();
                if (s.hasNextInt()) {
                    int pos = s.nextInt();
                    res = true;
                    VenusFile f = descriptorMap.get(ID);
                    if (f ==null)
                        System.out.println("ID de fichero invalido");
                    else {
                        f.seek(pos);
                        System.out.println("Puntero colocado en posicion " + pos);
                    }
               }
            }
        }
        catch (IOException e) {
            System.out.println("Excepcion de E/S");
        }
        return res;
    }

    static private boolean doSetLength(Scanner ent) throws RemoteException {
        boolean res = false;
        try {
            System.out.println("Introduzca ID de fichero y nuevo tamano");
            if (!ent.hasNextLine()) return false;
            String lin = ent.nextLine();
            Scanner s = new Scanner(lin);
            if (s.hasNextInt()) {
                int ID = s.nextInt();
                if (s.hasNextInt()) {
                    int tam = s.nextInt();
                    res = true;
                    VenusFile f = descriptorMap.get(ID);
                    if (f ==null)
                        System.out.println("ID de fichero invalido");
                    else {
                        f.setLength(tam);
                        System.out.println("Nuevo tamano del fichero: " + tam);
                    }
               }
            }
        }
        catch (IOException e) {
            System.out.println("Excepcion de E/S");
        }
        return res;
    }

    static private boolean doClose(Scanner ent) throws RemoteException {
        boolean res = false;
        try {
            System.out.println("Introduzca ID de fichero");
            if (!ent.hasNextLine()) return false;
            String lin = ent.nextLine();
            Scanner s = new Scanner(lin);
            if (s.hasNextInt()) {
                int ID = s.nextInt();
                res = true;
                VenusFile f = descriptorMap.get(ID);
                if (f ==null)
                    System.out.println("ID de fichero invalido");
                else {
                    f.close();
                    descriptorMap.remove(ID);
                    System.out.println("Fichero cerrado");
                }
             }
        }
        catch (IOException e) {
            System.out.println("Excepcion de E/S");
        }
        return res;
    }

    static private void prompt() {
        System.out.println("Seleccione operacion");
        System.out.println("\t0 open|1 read|2 write|3 seek|4 setLength|5 close (Ctrl-D para terminar)");
    }
    static private void limpiarCache() {
        File cache = new File("Cache");
        for(File f: cache.listFiles())
            f.delete();
        System.out.println("Borrando Cache...");
        return;
    }

    static public void main(String args[]) {
        if (System.getSecurityManager() == null)
            System.setSecurityManager(new SecurityManager());

        try {
            limpiarCache();
            venus = new Venus();
            while (true) {
                boolean formatoOK = false;
                Scanner ent = new Scanner(System.in);
                prompt();
                if (!ent.hasNextLine()) return;
                String lin = ent.nextLine();
                Scanner s = new Scanner(lin);
                if (s.hasNextInt()) {
                   int op = s.nextInt();
                   switch (op) {
                       case 0: formatoOK = doOpen(ent); break;
                       case 1: formatoOK = doRead(ent); break;
                       case 2: formatoOK = doWrite(ent); break;
                       case 3: formatoOK = doSeek(ent); break;
                       case 4: formatoOK = doSetLength(ent); break;
                       case 5: formatoOK = doClose(ent); break;
                    }
                }
                if (!formatoOK)
                     System.err.println("Error en formato de operacion");
            }
        }
        catch (Exception e) {
            System.err.println("Excepcion en Test:");
            e.printStackTrace();
        }
        finally {
            System.exit(0);
        }

    }
}
